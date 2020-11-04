package best_food_catering.kitchen;

import best_food_catering.order.OOSReport;
import best_food_catering.order.OOSReportEntry;
import best_food_catering.order.ReportRepository;
import best_food_catering.storage.Ingredient;
import best_food_catering.storage.IngredientRepository;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Quantity;
import org.springframework.data.util.Streamable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.*;
import java.time.temporal.IsoFields;
import java.util.*;

import static org.salespointframework.core.Currencies.EURO;

/**
 * @author Jonas Bohmann
 */

@Service
public class KitchenService {

	private final MenuRepository menuRepository;
	private final Catalog<Meal> mealCatalog;
	private final UniqueInventory<UniqueInventoryItem> mealInventory;
	private final KitchenReportRepository kitchenReportRepository;
	private final IngredientRepository ingredientRepository;
	private final ReportRepository reportRepository;

	/**
	 * A Service to interact with all Kitchen-related Repositories and Catalogs
	 *
	 * @param menuRepository          the MenuRepository that holds all Menus
	 * @param mealCatalog             the MealCatalog that holds all our {@link org.salespointframework.catalog.Product} Meals
	 * @param mealInventory           the MealInventory that holds all our Meals with their Quantitiy
	 * @param ingredientRepository    the IngredientRepository that holds all Ingredients
	 * @param reportRepository        the ReportRepository that holds all reports from the OOS
	 * @param kitchenReportRepository the KitchenReportRepository that holds all reports from the Kitchen to the Storage
	 */
	public KitchenService(MenuRepository menuRepository, Catalog<Meal> mealCatalog,
						  UniqueInventory<UniqueInventoryItem> mealInventory,
						  IngredientRepository ingredientRepository,
						  ReportRepository reportRepository,
						  KitchenReportRepository kitchenReportRepository) {
		this.kitchenReportRepository = kitchenReportRepository;
		this.ingredientRepository = ingredientRepository;
		this.mealCatalog = mealCatalog;
		this.mealInventory = mealInventory;
		this.menuRepository = menuRepository;
		this.reportRepository = reportRepository;
	}


	/**
	 * @return All unique Meals (compared by their name) in the MealCatalog.
	 */
	public List<Meal> getAllUniqueMeals() {
		List<Meal> mealList = new ArrayList<>();

		for (Meal meal : mealCatalog.findAll()) {
			if (!mealList.contains(meal)) {
				mealList.add(meal);
			}
		}

		return mealList;
	}

	/**
	 * @return All Meals in the MealCatalog.
	 */
	public List<Meal> getAllMeals() {
		List<Meal> mealList = new ArrayList<>();

		for (Meal meal : mealCatalog.findAll()) {
			mealList.add(meal);
		}

		return mealList;
	}

	/**
	 * @return All Ingredients in the IngredientRepository.
	 */
	public List<Ingredient> getAllIngredients() {
		List<Ingredient> ingredientList = new ArrayList<>();

		for (Ingredient ingredient : ingredientRepository.findAll()) {
			ingredientList.add(ingredient);
		}

		return ingredientList;

	}

	/**
	 * @return All Menus in the MenuRepository.
	 */
	public List<Menu> getAllMenus() {
		List<Menu> menuList = new ArrayList<>();

		for (Menu menu : menuRepository.findAll()) {
			menuList.add(menu);
		}

		return menuList;

	}

	/**
	 * Creates a new Meal from an {@link AddMealForm}, saves that into the MealCatalog and creates 100 quantities for
	 * the Meal in the MealInventory.
	 *
	 * @param addMealForm a valid form with inputted data for the new Meal
	 * @return the Meal that was created
	 */
	public Meal createNewMeal(AddMealForm addMealForm) {

		Map<Ingredient, Quantity> ingredients = new HashMap<>();

		if (!mealCatalog.findByName(addMealForm.getName()).isEmpty()) {
			throw new IllegalArgumentException("Meal existiert bereits!");
		}

		for (AddMealRow row : addMealForm.getRows()) {
			if (ingredientRepository.findById(row.getIngredientID()).isPresent()) {
				Ingredient ingredient = ingredientRepository.findById(row.getIngredientID()).get();
				if (!ingredients.containsKey(ingredient)) {
					ingredients.put(ingredient, Quantity.of(row.getQuantityAmount()));
				}
			}
		}

		Meal meal = new Meal(addMealForm.getName(),
				addMealForm.getDescription(),
				Money.of(addMealForm.getMoney(), EURO),
				ingredients,
				addMealForm.getRecipe(),
				addMealForm.convertMealTypeFromHTML(addMealForm.getType()));

		mealCatalog.save(meal);
		putMealInventory(meal, 500);
		return meal;

	}

	/**
	 * Creates a new Menu and saves that into the MenuRepository.
	 *
	 * @param addMenuForm a valid form with inputted data for the new Menu
	 * @return the Menu that was created.
	 */
	public Menu createNewMenu(AddMenuForm addMenuForm) {

		int date = Integer.parseInt(addMenuForm.getYear() + String.format("%02d",
				Integer.parseInt(addMenuForm.getWeek())));

		if (menuRepository.findByDate(date) != null) {
			throw new IllegalArgumentException("Menu existiert bereits!");
		}

		Menu menu = new Menu(Integer.parseInt(addMenuForm.getWeek()), Integer.parseInt(addMenuForm.getYear()));

		menu.addMeal(mealCatalog.findById(addMenuForm.getNormalMonday()).get(), DayOfWeek.MONDAY);
		menu.addMeal(mealCatalog.findById(addMenuForm.getNormalTuesday()).get(), DayOfWeek.TUESDAY);
		menu.addMeal(mealCatalog.findById(addMenuForm.getNormalWednesday()).get(), DayOfWeek.WEDNESDAY);
		menu.addMeal(mealCatalog.findById(addMenuForm.getNormalThursday()).get(), DayOfWeek.THURSDAY);
		menu.addMeal(mealCatalog.findById(addMenuForm.getNormalFriday()).get(), DayOfWeek.FRIDAY);

		menu.addMeal(mealCatalog.findById(addMenuForm.getDietMonday()).get(), DayOfWeek.MONDAY);
		menu.addMeal(mealCatalog.findById(addMenuForm.getDietTuesday()).get(), DayOfWeek.TUESDAY);
		menu.addMeal(mealCatalog.findById(addMenuForm.getDietWednesday()).get(), DayOfWeek.WEDNESDAY);
		menu.addMeal(mealCatalog.findById(addMenuForm.getDietThursday()).get(), DayOfWeek.THURSDAY);
		menu.addMeal(mealCatalog.findById(addMenuForm.getDietFriday()).get(), DayOfWeek.FRIDAY);

		menu.addMeal(mealCatalog.findById(addMenuForm.getSpecialMonday()).get(), DayOfWeek.MONDAY);
		menu.addMeal(mealCatalog.findById(addMenuForm.getSpecialTuesday()).get(), DayOfWeek.TUESDAY);
		menu.addMeal(mealCatalog.findById(addMenuForm.getSpecialWednesday()).get(), DayOfWeek.WEDNESDAY);
		menu.addMeal(mealCatalog.findById(addMenuForm.getSpecialThursday()).get(), DayOfWeek.THURSDAY);
		menu.addMeal(mealCatalog.findById(addMenuForm.getSpecialFriday()).get(), DayOfWeek.FRIDAY);

		menuRepository.save(menu);
		return menu;

	}

	/**
	 * Saves a Meal into the MealCatalog. Used only by the MenuDataInitializer.
	 *
	 * @param meal the Meal to be saved into the MealCatalog
	 */
	public void saveMeal(Meal meal) {
		mealCatalog.save(meal);
	}

	/**
	 * Saves a Menu into the MenuRepository. Used only by the MenuDataInitializer.
	 *
	 * @param menu the Menu to be saved into the MenuRepository
	 */
	public void saveMenu(Menu menu) {
		menuRepository.save(menu);
	}

	/**
	 * Creates an UniqueInventoryItem out of a Meal or adds the quantity to the UniqueInventoryItem
	 * if it already existed.
	 *
	 * @param meal     the Meal to be saved into the MealInventory
	 * @param quantity the wanted amount of Quantity for the the Meal
	 * @return the UniqueInventoryItem that was created or updated
	 */
	public UniqueInventoryItem putMealInventory(Meal meal, int quantity) {
		return mealInventory.findByProduct(meal).orElseGet(() -> mealInventory.save(new
				UniqueInventoryItem(meal, meal.createQuantity(quantity))));

	}

	/**
	 * @return all Meals in the MealCatalog with FoodType.NORMAL
	 */
	public List<Meal> getAllNormalMeals() {
		List<Meal> mealList = new ArrayList<>();

		for (Meal meal : mealCatalog.findAll()) {
			if (meal.getFoodType() == Meal.FoodType.NORMAL) {
				mealList.add(meal);
			}
		}

		return mealList;
	}

	/**
	 * @return all Meals in the MealCatalog with FoodType.SPECIAL
	 */
	public List<Meal> getAllSpecialMeals() {
		List<Meal> mealList = new ArrayList<>();

		for (Meal meal : mealCatalog.findAll()) {
			if (meal.getFoodType() == Meal.FoodType.SPECIAL) {
				mealList.add(meal);
			}
		}

		return mealList;
	}

	/**
	 * @return all Meals in the MealCatalog with FoodType.DIET
	 */
	public List<Meal> getAllDietMeals() {
		List<Meal> mealList = new ArrayList<>();

		for (Meal meal : mealCatalog.findAll()) {
			if (meal.getFoodType() == Meal.FoodType.DIET) {
				mealList.add(meal);
			}
		}

		return mealList;
	}

	/**
	 * @return the Menu that is active for the current calender week of the current year.
	 */
	public Menu getMenuForCurrentWeek() {
		return menuRepository.findByDate(getCurrentDate()[2]);

	}

	/**
	 * @return the Menu that is active for the next calender weekr.
	 */
	public Menu getMenuForNextWeek() {
		return menuRepository.findByDate(getDateOfNextWeek()[2]);

	}

	/**
	 * @param date the date identifier to find a Menu by. Example format for a date: 202004, 201952, 202113
	 * @return the Menu that is active for the current calender week and year.
	 */
	public Menu getMenuByDate(int date) {
		return menuRepository.findByDate(date);
	}

	/**
	 * @param name the name to find a Meal by
	 * @return all Meals that were found
	 */
	public Streamable<Meal> getMealByName(String name) {
		return mealCatalog.findByName(name);
	}

	/**
	 * @param id the id to find a Meal by
	 * @return the Meal that was found
	 */
	public Optional<Meal> getMealById(ProductIdentifier id) {
		return mealCatalog.findById(id);
	}

	/**
	 * Deletes a Meal. In order to avoid Database Primary/Foreign Key violations, the Meal first has to be removed from
	 * every existing Menu, OOSReport and all UniqueInventoryItems in the MealInventory.
	 *
	 * @param meal the Meal to delete
	 */
	public void deleteMeal(Meal meal) {
		for (Menu menu : menuRepository.findAll()) {
			switch (meal.getFoodType()) {
				case NORMAL:
					Map<DayOfWeek, Meal> normals = menu.getNormalMeals();
					normals.values().removeIf(value -> value == meal);
					menu.setNormalMeals(normals);
					break;

				case DIET:
					Map<DayOfWeek, Meal> diets = menu.getDietMeals();
					diets.values().removeIf(value -> value == meal);
					menu.setDietMeals(diets);
					break;


				case SPECIAL:
					Map<DayOfWeek, Meal> specials = menu.getSpecialMeals();
					specials.values().removeIf(value -> value == meal);
					menu.setSpecialMeals(specials);
					break;
			}

		}

		for (OOSReport oosReport : reportRepository.findAll()) {
			Map<OOSReportEntry, Integer> mondays = oosReport.getMealsMonday();
			mondays.keySet().removeIf(key -> key.getMeal() == meal);
			oosReport.setMealsMonday(mondays);

			Map<OOSReportEntry, Integer> tuesdays = oosReport.getMealsTuesday();
			tuesdays.keySet().removeIf(key -> key.getMeal() == meal);
			oosReport.setMealsTuesday(tuesdays);

			Map<OOSReportEntry, Integer> wedns = oosReport.getMealsWednesday();
			wedns.keySet().removeIf(key -> key.getMeal() == meal);
			oosReport.setMealsWednesday(wedns);

			Map<OOSReportEntry, Integer> thurs = oosReport.getMealsThursday();
			thurs.keySet().removeIf(key -> key.getMeal() == meal);
			oosReport.setMealsThursday(thurs);

			Map<OOSReportEntry, Integer> fridays = oosReport.getMealsFriday();
			fridays.keySet().removeIf(key -> key.getMeal() == meal);
			oosReport.setMealsFriday(fridays);
		}

		if (mealInventory.findByProduct(meal).isPresent()) {
			UniqueInventoryItem inv = mealInventory.findByProduct(meal).get();
			mealInventory.delete(inv);
		}

		mealCatalog.delete(meal);
	}

	/**
	 * Deletes a Menu out of the MenuRepository.
	 *
	 * @param menu the Menu to delete
	 */
	public void deleteMenu(Menu menu) {
		menuRepository.delete(menu);
	}

	/**
	 * Searches for the OOSReport for the current calender week by the date identifier. Returns null if no OOSReport
	 * with current date exists.
	 *
	 * @return the OOSReport for the current calender week
	 */
	public OOSReport getOOSReportForCurrentWeek() {
		ZonedDateTime now = ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
		int calenderWeek = now.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
		int year = Year.now().getValue();
		int date = Integer.parseInt(year + String.format("%02d", calenderWeek));
		if (!reportRepository.findByDate(getCurrentDate()[2]).isEmpty()) {
			return reportRepository.findByDate(date).get(0);
		}
		return null;
	}

	/**
	 * Creates a new KitchenReport out of an existing OOSReport.
	 *
	 * @param oosReport the OOSReport of the current week with all Meals that were ordered as basis for the KitchenReport
	 * @return the KitchenReport that was created
	 */
	public KitchenReport createKitchenReportToInventory(@NotNull OOSReport oosReport) {
		KitchenReport kitchenReport = new KitchenReport(oosReport.getCalenderWeek(), oosReport.getYear());
		Map<Ingredient, Quantity> usedIngredients = new HashMap<>();

		for (Map.Entry<OOSReportEntry, Integer> meals : oosReport.getMealsMonday().entrySet()) {
			fillKitchenReport(usedIngredients, meals);
		}

		for (Map.Entry<OOSReportEntry, Integer> meals : oosReport.getMealsTuesday().entrySet()) {
			fillKitchenReport(usedIngredients, meals);
		}

		for (Map.Entry<OOSReportEntry, Integer> meals : oosReport.getMealsWednesday().entrySet()) {
			fillKitchenReport(usedIngredients, meals);
		}

		for (Map.Entry<OOSReportEntry, Integer> meals : oosReport.getMealsThursday().entrySet()) {
			fillKitchenReport(usedIngredients, meals);
		}

		for (Map.Entry<OOSReportEntry, Integer> meals : oosReport.getMealsFriday().entrySet()) {
			fillKitchenReport(usedIngredients, meals);
		}

		kitchenReport.setUsedIngredients(usedIngredients);
		return kitchenReport;

	}

	/**
	 * Fills a KitchenReport with the total amount of Ingredients that were used by the Meals that were ordered in that
	 * calender week.
	 *
	 * @param usedIngredients the Ingredients and their Quantity that were used
	 * @param meals           the Meals and how often they were ordered that week
	 */
	public void fillKitchenReport(Map<Ingredient, Quantity> usedIngredients, Map.Entry<OOSReportEntry, Integer> meals) {
		for (int i = 0; i < meals.getValue(); i++) {
			for (Map.Entry<Ingredient, Quantity> ings : meals.getKey().getMeal().getIngredients().entrySet()) {
				if (usedIngredients.containsKey(ings.getKey())) {
					Quantity oldVal = usedIngredients.get(ings.getKey());
					Quantity newVal = oldVal.add(ings.getValue());
					usedIngredients.replace(ings.getKey(), newVal);
				} else {
					usedIngredients.put(ings.getKey(), ings.getValue());
				}
			}
		}
	}

	/**
	 * Searches for a KitchenReport by date. Returns null if nothing was found.
	 *
	 * @param date the date identifier to search KitchenReports bx
	 * @return the KitchenReport that was found
	 */
	public KitchenReport getKitchenReportByDate(int date) {
		if (!kitchenReportRepository.findByDate(date).isEmpty()) {
			return kitchenReportRepository.findByDate(date).get(0);
		}
		return null;
	}

	/**
	 * A scheduled cron job to create a KitchenReport out of a OOSReport in order to send that to the Storage.
	 * Is called every Saturday at 00:01, after all orders were placed.
	 */
	@Scheduled(cron = "1 0 0 * * 6")
	public void sendReportToInventory() {
		OOSReport oosReport = getOOSReportForCurrentWeek();
		if (oosReport != null) {
			KitchenReport report = createKitchenReportToInventory(oosReport);
			kitchenReportRepository.save(report);
		}

	}

	/**
	 * Converts the current date into the internal date identifier used by Menu's, KitchenReports and OOSReports.
	 * <p>
	 * Example:
	 * Current Date: January 14th, 2020
	 * Output: [3, 2020, 202003]
	 *
	 * @return an array with the current calender week, the year and the converted date identifier used internally
	 */
	public int[] getCurrentDate() {
		ZonedDateTime now = ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
		int calenderWeek = now.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
		int year = Year.now().getValue();
		int date = Integer.parseInt(year + String.format("%02d", calenderWeek));
		return new int[]{calenderWeek, year, date};
	}

	/**
	 * Converts the next week's date into the internal date identifier used by Menu's, KitchenReports and OOSReports.
	 * <p>
	 * Example:
	 * Next Week's Date: January 21th, 2020
	 * Output: [4, 2020, 202004]
	 *
	 * @return an array with the next calender week, the year and the converted date identifier used internally
	 */
	public int[] getDateOfNextWeek() {
		ZonedDateTime now = ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
		int calenderWeek = now.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR) + 1;
		int year = Year.now().getValue();

		if (calenderWeek > 53) {
			calenderWeek = 1;
			year = year + 1;
		}

		int date = Integer.parseInt(year + String.format("%02d", calenderWeek));
		return new int[]{calenderWeek, year, date};
	}

}
