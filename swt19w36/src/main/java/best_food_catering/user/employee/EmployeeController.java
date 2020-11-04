package best_food_catering.user.employee;

import best_food_catering.user.UserService;
import best_food_catering.user.customer.CustomerSettingsForm;
import com.mysema.commons.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

/**
 * @author David Schneider
 * @author Ignacio
 */
@Controller
public class EmployeeController {

	@Autowired
	private UserService userService;

	// == constructors ==

	/**
	 * @param userService manages the users
	 */
	public EmployeeController(UserService userService) {
		Assert.notNull(userService, "UserService must not be null!");

		this.userService = userService;
	}

	/**
	 * Handles GET-Mapping
	 *
	 * @param model                    contains data
	 * @param employeeRegistrationForm stores input data for employees registration
	 * @param employeeSettingsForm     stores input data for employees changes
	 * @return shows the admin panel with its three Forms and the employeeList
	 */
	// == public methods ==
	@GetMapping("/admin")
	@PreAuthorize("hasAnyRole('ADMIN', 'BOSS')")
	public String adminPanel(Model model,
							 EmployeeRegistrationForm employeeRegistrationForm,
							 EmployeeSettingsForm employeeSettingsForm) {
		model.addAttribute("pageTitle", "Admin-Panel");
		model.addAttribute("employeeRegistrationForm", employeeRegistrationForm);
		model.addAttribute("employeeList", userService.findAllEmployees());
		model.addAttribute("employeeChangeForm", employeeSettingsForm);

		return "admin";
	}


	/**
	 * Handles POST-Mapping
	 *
	 * @param form   stores input data for employees
	 * @param result all errors
	 * @return register new employee and redirect to admin panel
	 */
	@PostMapping("/registerEmployee")
	@PreAuthorize("hasAnyRole('ADMIN', 'BOSS')")
	public String registerEmployee(@Valid EmployeeRegistrationForm form, Errors result) {
		if (result.hasErrors()) {
			return "admin";
		}

		userService.createEmployee(form);
		return "redirect:/admin";
	}

	/**
	 * Handles POST-Mapping
	 *
	 * @param form   stores employees new information
	 * @param result all errors
	 * @return changes employees information and redirects to admin panel
	 */
	@PostMapping("/changeEmployee")
	@PreAuthorize("hasAnyRole('ADMIN', 'BOSS')")
	public String changeEmployee(EmployeeSettingsForm form, Errors result) {
		if (result.hasErrors()) {
			return "admin";
		}

		CustomerSettingsForm helpForm = new CustomerSettingsForm(
				form.getForename(),
				form.getSurname(),
				form.getEmail()
		);

		userService.changeEmployee(helpForm, Long.parseLong(form.getId()));

		return "redirect:/admin";
	}


}