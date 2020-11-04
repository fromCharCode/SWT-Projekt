package best_food_catering.order;

import best_food_catering.kitchen.KitchenService;
import best_food_catering.kitchen.Meal;
import com.mysema.commons.lang.Assert;
import org.salespointframework.order.OrderIdentifier;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class OrderController {

	private final OrderService orderService;
	private final KitchenService kitchenService;
	private final MealOrderRepository orderRepository;

	/**
	 * @param orderService    the service handling the orders
	 * @param orderRepository the repository for all orders
	 * @param kitchenService  the service for handling the kitchen
	 */
	public OrderController(OrderService orderService, MealOrderRepository orderRepository, KitchenService kitchenService) {
		Assert.notNull(orderService, "OrderService must not be null!");
		Assert.notNull(orderRepository, "OrderRepository must not be null!");
		Assert.notNull(kitchenService, "KitchenService must not be null!");

		this.orderService = orderService;
		this.orderRepository = orderRepository;
		this.kitchenService = kitchenService;
	}

	/**
	 * Handles GET-Mapping
	 *
	 * @param model          the model
	 * @param authentication the users' authentification
	 * @param userAccount    the user account
	 * @return shows the myorders view which displays all the orders of the user
	 */
	@GetMapping("/myorders")
	@PreAuthorize("isAuthenticated()")
	public String listOrders(Model model, Authentication authentication, @LoggedIn UserAccount userAccount) {
		List<MealOrder> myOrders = new ArrayList<>();
		String authUserName = authentication.getName();
		for (MealOrder o : orderRepository.findAll()) {
			// case single order
			if (o.getAttachedCustomerName().equals(authUserName)) {
				myOrders.add(o);
			}

			//case: groupOrder
			if (!o.getAttachedOrders().isEmpty() &&
					o.getAttachedOrders().containsKey(userAccount.getId().toString())) {
				for (String s : o.getAttachedOrders().get(userAccount.getId().toString())) {
					String a = s.split(":")[0];
					String b = new StringBuilder(s).reverse().toString().split(":")[0];
					for (Map<DayOfWeek, Meal> maps : kitchenService.getMenuForNextWeek().getMeals()) {
						for (Map.Entry<DayOfWeek, Meal> entry : maps.entrySet()) {
							if (entry.getValue().getId().toString().equals(a)) {
								MealOrder n = new MealOrder(userAccount, LocalDate.now(), LocalDate.now());
								n.addOrderLine(entry.getValue(), Quantity.of(Integer.parseInt(b)));
								n.activateGroupOrder();
								myOrders.add(n);
							}
						}

					}
				}
			}
		}
		model.addAttribute("pageTitle", "Meine Bestellungen");
		model.addAttribute("orders", myOrders);
		return "myorders";
	}

	/**
	 * @param order the unique identifier of the order
	 * @return enables admin to close an order which was payed by cheque
	 */
	@PostMapping("/accept")
	public String acceptOrder(@RequestParam("order") OrderIdentifier order) {

		for (MealOrder m : orderRepository.findAll()) {
			if (m.getId().equals(order)) {
				m.close(LocalDate.now());
				orderRepository.save(m);
			}
		}
		return "redirect:/accounting";
	}
}