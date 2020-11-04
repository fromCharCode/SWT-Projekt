package best_food_catering.user;

import best_food_catering.user.company.CompanyRegistrationForm;
import best_food_catering.user.company.CompanySettingsForm;
import best_food_catering.user.customer.CustomerRegistrationForm;
import best_food_catering.user.customer.CustomerSettingsForm;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Optional;

/**
 * @author David Schneider
 */
@Controller
public class UserController {

	@Autowired
	private final UserService userService;

	// == constructors ==

	/**
	 * @param userService manages users
	 */
	public UserController(UserService userService) {
		Assert.notNull(userService, "CustomerManagement must not be null!");

		this.userService = userService;
	}

	// == public methods ==
	///////////////////// REGISTRATION ///////////////////////

	/**
	 * Handles POST-Mapping
	 *
	 * @param form   stores customers registration data
	 * @param result all errors
	 * @return on success returns to login-page, else to registration page
	 */
	@PostMapping("/registerUser")
	//@PreAuthorize("isAnonymous()")
	public String registerNewUser(@Valid CustomerRegistrationForm form, Errors result) {
		if (result.hasErrors()) {
			return "register";
		}
		userService.createCustomer(form);

		return "index";
	}

	/**
	 * Handles POST-Mapping
	 *
	 * @param form   stores companies registration data
	 * @param result all errors
	 * @return on success returns to login-page, else to registration page
	 */
	@PostMapping("/registerCompany")
	//@PreAuthorize("isAnonymous()")
	public String registerNewCompany(@Valid CompanyRegistrationForm form, Errors result) {
		if (result.hasErrors()) {
			return "register";
		}
		userService.createCompany(form);

		return "index";
	}


	/**
	 * Handles GET-Mapping
	 *
	 * @param model                    contains all forms
	 * @param customerRegistrationForm for customer registration
	 * @param companyRegistrationForm  for company registration
	 * @return go to registration page
	 */
	@GetMapping("/register")
	//@PreAuthorize("isAnonymous()")
	public String register(Model model, CustomerRegistrationForm customerRegistrationForm,
						   CompanyRegistrationForm companyRegistrationForm) {
		model.addAttribute("pageTitle", "Registrieren");
		model.addAttribute("userForm", customerRegistrationForm);
		model.addAttribute("companyForm", companyRegistrationForm);
		model.addAttribute("companies", userService.findAllCompanies());
		return "register";
	}


	///////////////////// SETTINGS ///////////////////////

	/**
	 * Handles GET-Mapping
	 *
	 * @param model                stores forms
	 * @param userAccount          to gain user data
	 * @param companySettingsForm  stores changed information for company
	 * @param customerSettingsForm stores changed information for customers
	 * @return goes to the settings page, so user can change its information
	 */
	@GetMapping("/settings")
	@PreAuthorize("isAuthenticated()")
	public String settings(Model model, @LoggedIn Optional<UserAccount> userAccount,
						   CompanySettingsForm companySettingsForm, CustomerSettingsForm customerSettingsForm) {

		if (userAccount.isEmpty()) {
			return "menu";
		}

		User user = userService.findUserByAccountName(userAccount.get().getUsername());

		model.addAttribute("pageTitle", "Einstellungen");

		// add forms and attributes
		model.addAttribute("companySettingsForm", companySettingsForm);
		model.addAttribute("customerSettingsForm", customerSettingsForm);
		// employees yet do use the customerSettingsForm, because the fields are the same

		model.addAttribute("u", user);
		model.addAttribute("username", user.getUserAccount().getUsername());
		model.addAttribute("userMail", user.getEmail());

		return "settings";
	}

	/**
	 * Handles POST-Mapping
	 *
	 * @param companySettingsForm contains information of company
	 * @param result              all errors
	 * @param userAccount         to save new information
	 * @return save changed company information. on success goes to the menu page,
	 * else back to settings
	 */
	@PostMapping("/saveCompany")
	@PreAuthorize("isAuthenticated()")
	public String saveCompany(@Valid CompanySettingsForm companySettingsForm,
							  Errors result, @LoggedIn Optional<UserAccount> userAccount) {
		if (result.hasErrors()) {
			return "settings";
		}

		userService.changeCompany(companySettingsForm, userAccount);

		return "redirect:/settings";
	}

	/**
	 * Handles POST-Mapping
	 *
	 * @param customerSettingsForm contains information of customer
	 * @param result               all errors
	 * @param userAccount          to save the new information
	 * @return save changed customer information. on success goes to the menu page,
	 * else back to settings
	 */
	@PostMapping("/saveCustomer")
	@PreAuthorize("isAuthenticated()")
	public String saveCustomer(@Valid CustomerSettingsForm customerSettingsForm,
							   Errors result, @LoggedIn Optional<UserAccount> userAccount) {
		if (result.hasErrors()) {
			return "settings";
		}

		userService.changeCustomer(customerSettingsForm, userAccount);

		return "redirect:/settings";
	}
}
