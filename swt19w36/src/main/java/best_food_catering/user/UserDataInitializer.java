package best_food_catering.user;

import best_food_catering.user.company.Company;
import best_food_catering.user.company.CompanyRegistrationForm;
import best_food_catering.user.company.CompanyType;
import best_food_catering.user.customer.Customer;
import best_food_catering.user.customer.CustomerRegistrationForm;
import best_food_catering.user.employee.Employee;
import best_food_catering.user.employee.EmployeeRegistrationForm;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author David Schneider
 */
@Component
public class UserDataInitializer implements DataInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(UserDataInitializer.class);

	private final UserAccountManager userAccountManager;
	private final UserService userService;

	/**
	 * @param userAccountManager manages the user accounts
	 * @param userService        manages the users
	 */
	public UserDataInitializer(UserAccountManager userAccountManager, UserService userService) {
		Assert.notNull(userAccountManager, "UserAccountManager must not be null");
		Assert.notNull(userService, "UserManagement must not be null");

		this.userAccountManager = userAccountManager;
		this.userService = userService;
	}

	/**
	 * initializes our default users for testing
	 */
	@Override
	public void initialize() {

		if (userAccountManager.findByUsername("boss").isPresent()) {
			return;
		}

		// generic password for testing
		var password = "123";

		// create default staff for BEST-FOOD-CATERING
		LOG.info("USERDATA INITIALIZER - Creating default users...");

		userAccountManager.create(
				"boss",
				Password.UnencryptedPassword.of("123"),
				"boss@bfc.com",
				Role.of(UserRole.BOSS.toString()));
		LOG.info("Default User has been created: boss | Login: boss@bfc.com | Password: " + password);


		// create companies
		List.of(
				new CompanyRegistrationForm(
						"Valve Corporation",
						password,
						"gaben@valve.com",
						CompanyType.COMPANY.toString()
				),
				new CompanyRegistrationForm(
						"Kindergarten Pusteblume",
						password,
						"kindergarten@web.de",
						CompanyType.KINDERGARTEN.toString()
				)
		).forEach(userService::createCompany);

		for (Company c : userService.findAllCompanies()) {
			LOG.info(
					"Default User has been created: " + c.getUserAccount().getUsername() +
							"| Login: " + c.getUserAccount().getEmail() +
							"| Password: " + password +
							"| Status: " + c.getStatus()
			);
		}


		//create employees
		List.of(
				new EmployeeRegistrationForm(
						"admin",
						"admin",
						password,
						"admin@bfc.com",
						UserRole.ADMIN.toString()
				),
				new EmployeeRegistrationForm(
						"kitchen",
						"kitchen",
						password,
						"kitchen@bfc.com",
						UserRole.KITCHEN.toString()
				),
				new EmployeeRegistrationForm(
						"storage",
						"storage",
						password,
						"storage@bfc.com",
						UserRole.STORAGE.toString()
				),
				new EmployeeRegistrationForm(
						"accounting",
						"accounting",
						password,
						"accounting@bfc.com",
						UserRole.ACCOUNTING.toString()
				)
		).forEach(userService::createEmployee);

		for (Employee e : userService.findAllEmployees()) {
			LOG.info(
					"Default User has been created: " + e.getUserAccount().getUsername() +
							"| Login: " + e.getUserAccount().getEmail() +
							"| Password: " + password +
							"| Status: " + e.getStatus()
			);
		}


		// create users
		List.of(
				new CustomerRegistrationForm(
						"James",
						"Cook",
						password,
						"j@j.com",
						"Valve Corporation"
				),
				new CustomerRegistrationForm(
						"John",
						"Abuckle",
						password,
						"john@garfield.com",
						"EGMONT"
				),
				new CustomerRegistrationForm(
						"Max",
						"Mueller",
						password,
						"max-mueller@web.de",
						"Kindergarten Pusteblume"
				)
		).forEach(userService::createCustomer);

		for (Customer c : userService.findAllCustomers()) {
			LOG.info(
					"Default User has been created: " + c.getUserAccount().getUsername() +
							"| Login: " + c.getUserAccount().getEmail() +
							"| Password: " + password +
							"| Status: " + c.getStatus() +
							"| Associated company: " + c.getAssociatedCompanyName());
		}
	}
}
