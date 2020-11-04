package best_food_catering.order;

import best_food_catering.kitchen.KitchenService;
import best_food_catering.kitchen.Meal;
import best_food_catering.user.UserService;
import best_food_catering.user.company.Company;
import best_food_catering.user.company.CompanyRepository;
import best_food_catering.user.company.CompanyType;
import best_food_catering.user.customer.Customer;
import com.mysema.commons.lang.Assert;
import org.salespointframework.order.OrderLine;
import org.salespointframework.order.OrderManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@Transactional
public class OrderService {

	private final MealOrderRepository orders;
	private final KitchenService kitchenService;
	private final OrderManager<MealOrder> orderManager;
	private final ReportRepository reportRepository;
	private final UserService userService;
	private final CompanyRepository companyRepository;
	private final OOSReportEntryRepository oosReportEntryRepository;
	// == constructor ==

	/**
	 * @param mealOrderRepository      the repository containing all meal orders
	 * @param orderManager             salespoints' built-in order manager
	 * @param kitchenService           the KitchenService Bean
	 * @param companyRepository        the CompanyRepository Bean
	 * @param oosReportEntryRepository the OOSReportEntryRepository Bean
	 * @param userService              the UserService Bean
	 * @param reportRepository         the repository containing all reports
	 */
	public OrderService(MealOrderRepository mealOrderRepository, OrderManager<MealOrder> orderManager,
						ReportRepository reportRepository, UserService userService,
						CompanyRepository companyRepository, OOSReportEntryRepository oosReportEntryRepository,
						KitchenService kitchenService) {

		Assert.notNull(mealOrderRepository, "OrderRepository must not be null!");
		Assert.notNull(orderManager, "OrderManager must not be null!");
		Assert.notNull(oosReportEntryRepository, "oosReportUSW not be null!");
		this.orders = mealOrderRepository;
		this.orderManager = orderManager;
		this.userService = userService;
		this.companyRepository = companyRepository;
		this.reportRepository = reportRepository;
		this.oosReportEntryRepository = oosReportEntryRepository;
		this.kitchenService = kitchenService;
	}

	/**
	 * @param order a meal order
	 * @return the order gets saved in the orderRepository
	 */
	public MealOrder createOrder(MealOrder order) {
		orders.save(order);
		return orderManager.save(order);
	}

	/**
	 * executed every monday at 1 second after midnight
	 * closes all groupOrders
	 */
	@Scheduled(cron = "1 0 0 * * 1")
	public void closeAllGroupOrders() {

		// finish all groupOrders
		for (MealOrder m : orders.findAll()) {
			if (!m.getAttachedOrders().isEmpty()) {
				orderManager.payOrder(m);
				orderManager.completeOrder(m);
			}
		}
	}


	/**
	 * executed every monday at midnight + 2 seconds
	 * creates an OOS-Report for the kitchen
	 */
	@Scheduled(cron = "2 0 0 * * 1")
	public void makeReport() {

		// new report for this week - starting from Monday
		LocalDate date = LocalDate.now();
		TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
		int weekNumber = date.get(woy);
		int dateLastWeek = this.setDate();

		OOSReport oosReport = new OOSReport(weekNumber, date.getYear());

		// Gehe durch alle bestellungen von letzer woche
		for (MealOrder mealOrder : orders.findByDate(dateLastWeek)) {

			// Alle bestellungen habe OrderLines, welche ProductIdentifier enthalten
			for (OrderLine orderLine : mealOrder.getOrderLines()) {

				// Wir holen uns das Menu von letzter Woche und haben eine Liste aus 3 HashMaps mit jeweils 5 Gerichten
				for (Map<DayOfWeek, Meal> allMenuMeals : kitchenService.getMenuByDate(dateLastWeek).getMeals()) {

					// Wir gehen durch die 5 Meals aus den 3 Maps einzeln durch
					for (Map.Entry<DayOfWeek, Meal> entries : allMenuMeals.entrySet()) {

						// Wenn das Meal aus dem Menu mit dem meal aus der Orderline übereinstimmt, dann wurde dieses
						// Meal bestellt und wir müssen es dem OOSReport hinzufügen.
						if (entries.getValue().getId().equals(orderLine.getProductIdentifier())) {

							// Update den OOSReport mit dem Meal was bestellt wurde, dessen Größe und die Anzahl wie
							// oft es bestellt wurde
							oosReport = this.fillOOSReportMealAmount(entries.getKey(), oosReport, entries.getValue(),
									orderLine, mealOrder.getAttachedCustomerName());
						}

					}

				}

			}
		}
		reportRepository.save(oosReport);
	}

	/**
	 * @return sets the date in the format YYYYWW
	 */
	private int setDate() {
		LocalDate dateNow = LocalDate.now().minusDays(1);
		TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
		int weekNumber = dateNow.get(woy);
		return Integer.parseInt(LocalDate.now().getYear() + String.format("%02d", weekNumber));
	}


	/**
	 * Gets the meal size from a UserAccount. Every user that belongs to a KINDERGARTEN will have Meal.Size.SMALL,
	 * every other user will have Meal.Size.BIG.
	 *
	 * @param userAccountName the name of the UserAccount to get the Company from
	 * @return the meal size
	 */
	public Meal.Size getMealSizeFromUser(String userAccountName) {
		CompanyType companyType = this.getCompanyTypeFromUserName(userAccountName);

		switch (companyType) {

			case SCHOOL:
			case KINDERGARTEN:
				return Meal.Size.SMALL;

			case COMPANY:
			default:
				return Meal.Size.BIG;
		}
	}

	/**
	 * Fills a new OOSReport with a Meal that was ordered on a specific week day and its amount of times that the meal
	 * was ordered.
	 * <p>
	 * If the meal is already in the OOSReport, only the amount of times it was ordered will be updated. Otherwise, a
	 * new {@link OOSReportEntry} will be created for it.
	 *
	 * @param weekDay         the week day when this Meal was ordered
	 * @param oosReport       the OOSReport that will be updated
	 * @param meal            the Meal that was ordered
	 * @param orderLine       the OrderLine that holds the amount of times the meal was ordered
	 * @param userAccountName the name of the UserAccount that belongs to this OrderLine and Meal
	 * @return the updated OOSReport
	 */
	@Transactional
	public OOSReport fillOOSReportMealAmount(DayOfWeek weekDay, OOSReport oosReport, Meal meal, OrderLine orderLine,
											 String userAccountName) {

		boolean isMealAlreadyInReport = false;
		Map.Entry<OOSReportEntry, Integer> entryWithAlreadyPresentMeal = null;

		// Check if the meal is already in the OOSReport
		for (Map.Entry<OOSReportEntry, Integer> mapEntry : oosReport.getMealsPerDay(weekDay).entrySet()) {
			if (mapEntry.getKey().getMeal() == meal && mapEntry.getKey().getSize() == getMealSizeFromUser(userAccountName)) {
				isMealAlreadyInReport = true;
				entryWithAlreadyPresentMeal = mapEntry;
				break;
			}
		}

		// If the meal is already in the OOSReport, only update the amount of times it was ordered
		if (isMealAlreadyInReport) {
			oosReport.getMealsPerDay(weekDay).put(entryWithAlreadyPresentMeal.getKey(),
					entryWithAlreadyPresentMeal.getValue() + orderLine.getQuantity().getAmount().intValue());

			// Create a new OOSReportEntry out of the meal
		} else {
			oosReport.getMealsPerDay(weekDay).put(oosReportEntryRepository.save(new OOSReportEntry(meal,
					getMealSizeFromUser(userAccountName))), orderLine.getQuantity().getAmount().intValue());
		}

		return oosReport;
	}

	/**
	 * @return returns all orders as a list
	 */
	public List<MealOrder> getAllOrders() {
		List<MealOrder> allOrders = new LinkedList<MealOrder>();
		for (MealOrder m : orders.findAll()) {
			allOrders.add(m);
		}
		return allOrders;
	}

	/**
	 * Extracts the {@link CompanyType} from the username of a UserAccount.
	 *
	 * @param userAccountName the username of a UserAccount
	 * @return the CompanyType associated with a UserAccount
	 */
	public CompanyType getCompanyTypeFromUserName(String userAccountName) {
		Customer cu = null;
		Company company = null;

		for (Customer c : userService.findAllCustomers()) {
			if (c.getUserAccount().getUsername().equals(userAccountName)) {
				cu = c;
			}
		}

		if (!(cu == null)) {
			for (Company c : companyRepository.findAll()) {
				if (c.getCompanyName().equals(cu.getAssociatedCompanyName())) {
					company = c;
				}
			}
		}

		return company == null ? null : company.getType();
	}

	public MealOrderRepository getOrders() {
		return this.orders;
	}
}

