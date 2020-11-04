package best_food_catering.user.company;

import best_food_catering.user.UserService;
import best_food_catering.user.customer.Customer;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * @author David Schneider
 */
@Controller
public class CompanyController {

	@Autowired
	private UserService userService;

	// == public methods ==

	/**
	 * Handle GET-Mapping
	 *
	 * @param model          contains all employees
	 * @param authentication get the logged in company
	 * @return shows a page where only the companies employees
	 * are shown
	 */
	@GetMapping("/employees")
	@PreAuthorize("hasAnyRole('COMPANY', 'ADMIN', 'BOSS', 'ACCOUNTING')")
	public String listEmployees(Model model, Authentication authentication, UpdateCustomerDateForm form) {
		String authenticatedCompanyName = authentication.getName();

		List<Customer> customerList = new LinkedList<>();

		for (Customer e : userService.findAllCustomers()) {
			if (e.getAssociatedCompanyName().equals(authenticatedCompanyName)) {
				customerList.add(e);
			}
		}

		model.addAttribute("pageTitle", "Angestellte Verwalten");
		model.addAttribute("users", customerList);
		model.addAttribute("userForm", form);
		return "employees";
	}

	/**
	 * Handle GET-Mapping
	 *
	 * @param account    contains the current logged in account for deletion verification
	 * @param employeeId needed for deletion
	 * @return will delete the employee with employeeId and redirect to employees
	 */
	@GetMapping("deleteEmployee")
	@PreAuthorize("hasAnyRole('COMPANY', 'ADMIN', 'BOSS', 'ACCOUNTING')")
	public String deleteEmployee(@LoggedIn Optional<UserAccount> account, @RequestParam long employeeId) {
		userService.deleteCustomerById(account, employeeId);

		return "redirect:/employees";
	}

	@GetMapping("deleteBFCEmployee")
	@PreAuthorize("hasAnyRole('ADMIN', 'BOSS')")
	public String deleteBFCEmployee(@RequestParam long employeeId) {
		userService.deleteEmployeeById(employeeId);

		return "redirect:/admin";
	}


	/**
	 * Handle GET-Mapping
	 *
	 * @param employeeId for activation
	 * @return will activate the employee with the name
	 */
	@GetMapping("activateEmployee")
	@PreAuthorize("hasAnyRole('COMPANY', 'ADMIN', 'BOSS', 'ACCOUNTING')")
	public String activateEmployee(@RequestParam long employeeId) {
		userService.activateCustomerById(employeeId);

		return "redirect:/employees";
	}

	@PostMapping("setExpireDate")
	@PreAuthorize("hasAnyRole('COMPANY', 'BOSS')")
	public String setExpireDate(@Valid UpdateCustomerDateForm form, @RequestParam long id) {
		userService.expireCustomerByIDOn(id, form.getDate());
		return "redirect:/employees";
	}

}

