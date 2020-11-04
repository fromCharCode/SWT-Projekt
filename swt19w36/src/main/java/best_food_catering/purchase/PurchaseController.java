package best_food_catering.purchase;

import best_food_catering.kitchen.KitchenService;
import best_food_catering.kitchen.Meal;
import best_food_catering.kitchen.Menu;
import best_food_catering.order.MealOrder;
import best_food_catering.order.OrderService;
import best_food_catering.purchase.DebitsCardForm;
import best_food_catering.user.User;
import best_food_catering.user.UserService;
import best_food_catering.user.company.CompanyRepository;
import best_food_catering.user.company.CompanyType;
import best_food_catering.user.customer.Customer;
import com.mysema.commons.lang.Assert;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.order.Cart;
import org.salespointframework.order.CartItem;
import org.salespointframework.order.OrderManager;
import org.salespointframework.payment.Cheque;
import org.salespointframework.payment.DebitCard;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountIdentifier;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.money.MonetaryAmount;
import javax.validation.Valid;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Controller
@SessionAttributes("cart")
class PurchaseController {
	// the menu from the kitchen

	private final OrderService orderService;
	private final UserService userService;
	private final KitchenService kitchenService;
	private final OrderManager<MealOrder> orderManager;

	/**
	 * @param orderManager      salespoints' built in order manager
	 * @param kitchenService    the service for handling the kitchen
	 * @param orderService      the service for handling the orders
	 * @param service           the service for handling user-related tasks
	 */
	public PurchaseController(OrderManager<MealOrder> orderManager,
							  KitchenService kitchenService, OrderService orderService, UserService service) {
		Assert.notNull(orderManager, "Order manager can't be null!");
		this.orderManager = orderManager;
		this.kitchenService = kitchenService;
		this.orderService = orderService;
		this.userService = service;
	}

	/**
	 * initializes the cart bean
	 *
	 * @return the Cart
	 */
	@ModelAttribute("cart")
	public Cart initCart() {
		return new Cart();
	}

	/**
	 * Handles GET-Mapping
	 *
	 * @param cart  the cart
	 * @param model the model
	 * @return shows the cart view with all orders in it
	 */
	// shows the cart view
	@GetMapping("/cart")
	@PreAuthorize("isAuthenticated()")
	public String Cart(@ModelAttribute Cart cart, Model model, @LoggedIn UserAccount userAccount) {
		Menu menu = kitchenService.getMenuForNextWeek();
		if (menu != null) {
			model.addAttribute("menu", menu);
			model.addAttribute("normals", menu.getMeals().get(0));
			model.addAttribute("specials", menu.getMeals().get(1));
			model.addAttribute("diets", menu.getMeals().get(2));
		}

		CompanyType companyType = orderService.getCompanyTypeFromUserName(userAccount.getUsername());

		if (companyType != null) {
			model.addAttribute("companyType", companyType);
		}

		model.addAttribute("pageTitle", "Warenkorb");

		return "cart";
	}

	/**
	 * @return return the view for the group billing payment choice
	 */
	@GetMapping("/groupBilling")
	@PreAuthorize("isAuthenticated()")
	public String groupBilling() {
		return "groupBilling";
	}

	/**
	 * Handles GET-Mapping
	 *
	 * @param model          the model
	 * @param debitsCardForm contains the data the user types in the input fields
	 * @param cart           the cart
	 * @return returns the view for the singleBilling payment choice
	 */
	@GetMapping("/singleBilling")
	@PreAuthorize("isAuthenticated()")
	public String singleBilling(Model model, DebitsCardForm debitsCardForm, Cart cart) {
		model.addAttribute("debitsCardForm", debitsCardForm);
		model.addAttribute("cart", cart);
		model.addAttribute("time", LocalDate.now());
		model.addAttribute("bis", LocalDate.now().plusDays(14));
		return "singleBilling";
	}

	/**
	 * Handles GET-Mapping
	 *
	 * @param model the model
	 * @param cart  the cart
	 * @return shows the menu view with all the Meals in this weeks menu
	 */
	@GetMapping("/menu")
	@PreAuthorize("isAuthenticated()")
	public String showMenu(Model model, Cart cart) {

		Menu menu1 = kitchenService.getMenuForNextWeek();
		ArrayList<CartItem> cartItems = new ArrayList<>();
		for (CartItem c : cart) {
			cartItems.add(c);
		}

		model.addAttribute("menu", menu1);
		if (menu1 != null) {
			model.addAttribute("normalList", menu1.getSortedNormalMeals());
			model.addAttribute("dietList", menu1.getSortedDietMeals());
			model.addAttribute("specialList", menu1.getSortedSpecialMeals());
			model.addAttribute("cartItems", cartItems);
		}

		model.addAttribute("pageTitle", "Speiseplan");
		return "menu";
	}

	/**
	 * Handles POST-Mapping
	 *
	 * @param mealName the name of the meal
	 * @param num      the amount of times the meal is ordered
	 * @param day      the weekday the meal is ordered for (Monday- Friday)
	 * @param cart     the cart
	 * @param model    the model
	 * @return adds the chosen amount of meals to the card when ordering them and
	 * redirects to the menu view
	 */
	@PostMapping("/cart")
	@PreAuthorize("isAuthenticated()")
	public String handleOrder(
			@RequestParam("meal") ProductIdentifier mealName,
			@RequestParam("num") int num,
			@RequestParam("day") String day,
			@ModelAttribute Cart cart, Model model) {

		int number;
		Product currMeal = null;
		Menu menu = kitchenService.getMenuForNextWeek();
		if (num <= 0 || num > 5) {
			number = 1;
		} else {
			number = num;
		}

		for (Map<DayOfWeek, Meal> maps : menu.getMeals()) {
			for (Meal meal : maps.values()) {
				if (meal.getId().equals(mealName)) {
					currMeal = meal;
				}
			}

		}

		for (CartItem c : cart) {
			if (c.getProduct().getId().equals(mealName) &&
					cart.getItem(c.getId()).get().getQuantity().add(Quantity.of(number)).isGreaterThan(Quantity.of(10))) {
				return "redirect:/menu";
			}
		}

		cart.addOrUpdateItem(currMeal, Quantity.of(number));
		return "redirect:/menu";
	}

	/**
	 * Handles POST-Mapping
	 *
	 * @param model       the model
	 * @param cart        the cart
	 * @param userAccount the account of the user
	 * @return shows the confirmOrder view enabling the user to chose between
	 * singleBilling and groupBilling (if he belongs to a kindergarten)
	 */
	@PostMapping("/order")
	@PreAuthorize("isAuthenticated()")
	public String order(Model model,
						@ModelAttribute Cart cart,
						@LoggedIn UserAccount userAccount) {

		Menu menu = kitchenService.getMenuForNextWeek();
		model.addAttribute(cart);
		model.addAttribute(menu);

		CompanyType companyType = orderService.getCompanyTypeFromUserName(userAccount.getUsername());

		if (companyType != null) {
			model.addAttribute("companyType", companyType.toString());
		}

		return "confirmOrder";
	}

	/**
	 * Handles POST-Mapping
	 *
	 * @param mealName the name of the meal
	 * @param cart     the cart
	 * @return removes a meal entry from the cart and redirects to the cart
	 */
	@PostMapping("/remove")
	@PreAuthorize("isAuthenticated()")
	public String removeFromCart(@RequestParam("item") String mealName, @ModelAttribute Cart cart) {
		for (CartItem c : cart) {
			if (c.getProduct().getName().equals(mealName)) {
				cart.removeItem(c.getId());
				break;
			}
		}
		return "redirect:/cart";
	}

	/**
	 * Handles POST-Mapping
	 *
	 * @param form        the debit card form containing the data for paying by debit card
	 * @param userAccount the userAcccount
	 * @param result      the result
	 * @param model       the model
	 * @param cart        the cart
	 * @return executed when customer pays by debit card;
	 * creates a new MealOrder with all the items in the card and debit card as payment method
	 * and closes + finishes this order
	 */
	@PostMapping("/payOrderDC")
	@PreAuthorize("isAuthenticated()")
	public String payOrderDC(@Valid DebitsCardForm form, @LoggedIn Optional<UserAccount> userAccount,
							 Errors result, Model model, Cart cart) {

		if (result.hasErrors()) {
			model.addAttribute("debitsCardForm", form);
			model.addAttribute("cart", cart);
			return "singleBilling";
		}

		LocalDateTime validFromDT = form.getValidFrom().atStartOfDay();
		LocalDateTime expiryDateDT = form.getExpiryDate().atStartOfDay();
		MonetaryAmount withdrawalLimit = Money.of(form.hashCode(), "EUR");

		DebitCard debitCard = new DebitCard(
				form.getCardName(),
				form.getCardAssociationName(),
				form.getCardNumber(),
				form.getNameOnCard(),
				form.getBillingAddress(),
				validFromDT,
				expiryDateDT,
				form.getCardVerificationCode(),
				withdrawalLimit
		);

		return userAccount.map(account -> {
			var order = new MealOrder(
					account,
					debitCard,
					LocalDate.now(),
					LocalDate.now().plusDays(7)
			);
			order.close(LocalDate.now());
			finish(order, cart);
			return "redirect:/myorders";
		}).orElse("redirect:/cart");
	}

	/**
	 * Handles POST-Mapping
	 *
	 * @param cart           the cart
	 * @param user           the user
	 * @param userAccount    the account of the user
	 * @param authentication the authentification of the user
	 * @return creates a new MealOrder with all the items in the card and cheque as payment method
	 * and finishes this order
	 */
	@PostMapping("/payOrderCH")
	@PreAuthorize("isAuthenticated()")
	public String payOrderCH(Cart cart, User user, @LoggedIn Optional<UserAccount> userAccount,
							 Authentication authentication) {

		Random rand = new Random();
		int x = rand.nextInt(1000000);
		String y = Integer.toString(x);
		String id = Long.toString(user.getId());
		LocalDateTime date = LocalDateTime.now().plusDays(14);

		Cheque cheque = new Cheque(authentication.getName(), id, y, "BEST FOODS GMBH", date,
				"BESTBank Dresden",
				"Nöthnitzer Straße 46 01187 Dresden", "8748 45");

		return userAccount.map(account -> {
			var order = new MealOrder(account, cheque, LocalDate.now(), LocalDate.now().plusDays(7));
			finish(order, cart);
			return "redirect:/myorders";
		}).orElse("redirect:/cart");
	}

	/**
	 * @param order the MealOrder which is being processed
	 * @param cart  the cart
	 *              handles the finishing of a MealOrder
	 */
	private void finish(MealOrder order, Cart cart) {
		cart.addItemsTo(order);
		orderManager.payOrder(order);
		orderManager.completeOrder(order);
		orderService.createOrder(order);
		cart.clear();
	}

	/**
	 * Handles POST-Mapping
	 *
	 * @param userAccount the UserAccount
	 * @param cart        the User's Cart
	 * @return handles the groupBilling by checking if a group order of the customers' company already
	 * exists for this week and in this case adding his items to this order or creating a new
	 * order for the company
	 */
	@PostMapping("/groupBilling")
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	public String groupBilling(@LoggedIn UserAccount userAccount, Cart cart) {

		String searchedCompany = "";
		for (Customer c : userService.findAllCustomers()) {
			if (c.getUserAccount().equals(userAccount)) {
				searchedCompany = c.getAssociatedCompanyName();
			}
		}
		var companyAccount = userService.findUserAccountByName(searchedCompany);
		if (!userService.findCompanyByAccountName(companyAccount.get().getUsername())
				.getType().equals(CompanyType.KINDERGARTEN)) {
			return "redirect:/myorders";
		}

		// if there is already a groupOrder from this company
		for (MealOrder mo : orderService.getOrders().findAll()) {
			if (mo.getUserAccount().getUsername().equals(searchedCompany)) {
				return companyAccount.map(account -> {

					cart.addItemsTo(mo);
					mo.activateGroupOrder();
					// Order existing, userAccount already in Order
					if (mo.getAttachedOrders().containsKey(userAccount.getId().toString())) {
						for (CartItem c : cart) {
							mo.getAttachedOrders().get(userAccount.getId().
									toString()).add(c.getProduct().getId().toString() + ":" + c.getQuantity());
						}
					} else {
						mo.getAttachedOrders().put(userAccount.getId().toString(), new ArrayList<>());
						for (CartItem c : cart) {
							mo.getAttachedOrders().get(userAccount.getId().
									toString()).add(c.getProduct().getId().toString() + ":" + c.getQuantity());
						}
					}
					orderManager.save(mo);
					cart.clear();
					return "redirect:/myorders";
				}).orElse("redirect:/cart");
			}
		}


		// there is no groupOrder from this company yet
		Random rand = new Random();
		int x = rand.nextInt(1000000);
		String y = Integer.toString(x);
		Optional<UserAccount> companyUserAccount = userService.findUserAccountByName(searchedCompany);
		UserAccountIdentifier id = companyUserAccount.get().getId();

		Cheque cheque = new Cheque(searchedCompany, id.toString(), y, "BEST FOODS GMBH",
				LocalDateTime.now().plusDays(14), "BESTBank Dresden",
				"Nöthnitzer Straße 46 01187 Dresden", "8748 45");

		return companyAccount.map(account -> {

			var order = new MealOrder(companyAccount.orElse(null), cheque, LocalDate.now(),
					LocalDate.now().plusDays(14));
			cart.addItemsTo(order);
			order.activateGroupOrder();

			order.getAttachedOrders().put(userAccount.getId().toString(), new ArrayList<>());
			for (CartItem c : cart) {
				order.getAttachedOrders().get(userAccount.getId().toString()).add(
						c.getProduct().getId().toString() + ":" + c.getQuantity()
				);
			}
			cart.clear();
			orderService.createOrder(order);
			return "redirect:/myorders";
		}).orElse("redirect:/cart");
	}
}