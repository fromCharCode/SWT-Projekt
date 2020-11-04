package best_food_catering.accounting;

import best_food_catering.order.MealOrder;
import best_food_catering.order.OrderService;
import best_food_catering.user.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class AccountingController {

	private UserService userService;
	private OrderService orderService;

	public AccountingController(UserService userService, OrderService orderService) {
		this.userService = userService;
		this.orderService = orderService;
	}

	@GetMapping("/accounting")
	@PreAuthorize("hasAnyRole('BOSS', 'ACCOUNTING')")
	public String accounting(Model model) {

		List<MealOrder> allOrders = orderService.getAllOrders();
		model.addAttribute("allOrders", allOrders);

		model.addAttribute("pageTitle", "Buchhaltung");
		model.addAttribute("companies", userService.findAllCompanies());
		model.addAttribute("customers", userService.findAllCustomers());
		return "accounting";
	}

	@GetMapping("/accounting/deactivate")
	@PreAuthorize("hasAnyRole('BOSS', 'ACCOUNTING')")
	public String deactivate(@RequestParam long id) {
		userService.deactivateCustomerById(id);
		return "redirect:/accounting";
	}

	@GetMapping("/accounting/deactivateCompany")
	@PreAuthorize("hasAnyRole('BOSS', 'ACCOUNTING')")
	public String deactivateCompany(@RequestParam long id) {
		userService.deactivateCompanyById(id);
		return "redirect:/accounting";
	}

	@GetMapping("/accounting/activateCustomer")
	@PreAuthorize("hasAnyRole('BOSS', 'ACCOUNTING')")
	public String activateCustomer(@RequestParam long id) {
		userService.activateCustomerById(id);
		return "redirect:/accounting";
	}

	@GetMapping("/accounting/activateCompany")
	@PreAuthorize("hasAnyRole('BOSS', 'ACCOUNTING')")
	public String activateCompany(@RequestParam long id) {
		userService.activateCompanyById(id);
		return "redirect:/accounting";
	}


}
