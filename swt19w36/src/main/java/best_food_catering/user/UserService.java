package best_food_catering.user;

import best_food_catering.user.company.*;
import best_food_catering.user.customer.Customer;
import best_food_catering.user.customer.CustomerRegistrationForm;
import best_food_catering.user.customer.CustomerRepository;
import best_food_catering.user.customer.CustomerSettingsForm;
import best_food_catering.user.employee.Employee;
import best_food_catering.user.employee.EmployeeRegistrationForm;
import best_food_catering.user.employee.EmployeeRepository;
import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.Optional;

/**
 * @author David Schneider
 * @author Ignacio Vazquez
 */
@Service
@Transactional
public class UserService {

	public static final Role CUSTOMER_ROLE = Role.of(UserRole.CUSTOMER.toString());
	public static final Role COMPANY_ROLE = Role.of(UserRole.COMPANY.toString());

	@Autowired
	private final CustomerRepository customers;
	@Autowired
	private final CompanyRepository companies;
	@Autowired
	private final EmployeeRepository employees;


	private final UserAccountManager userAccountManager;


	// == constructors ==

	/**
	 * @param customers          repository containing all customers
	 * @param employees          repository containing all employees
	 * @param companies          repository containing all companies
	 * @param userAccountManager manages the account of each user
	 */
	public UserService(CustomerRepository customers, EmployeeRepository employees, CompanyRepository companies,
					   UserAccountManager userAccountManager) {
		Assert.notNull(customers, "UserRepository must not be null");
		Assert.notNull(userAccountManager, "UserAccountManager must not be null");

		this.customers = customers;
		this.companies = companies;
		this.employees = employees;
		this.userAccountManager = userAccountManager;
	}

	// == public methods ==
	////////////////////////////////////////////
	// find accounts

	/**
	 * @param name of the user to be found
	 * @return find and return user account by input name
	 */
	public Optional<UserAccount> findUserAccountByName(String name) {
		return userAccountManager.findByUsername(name);
	}

	/**
	 * @param name of the employee to be found
	 * @return returns the searched employee if available
	 */
	public Employee findEmployeeByName(String name) {
		for (Employee e : findAllEmployees()) {
			if (e.getUserAccount().getUsername().equals(name)) {
				return e;
			}
		}
		return null;
	}

	/**
	 * @return all customers
	 */
	public Iterable<Customer> findAllCustomers() {
		return customers.findAll();
	}

	/**
	 * @return all companies
	 */
	public Iterable<Company> findAllCompanies() {
		return companies.findAll();
	}

	/**
	 * @return all employees
	 */
	public Iterable<Employee> findAllEmployees() {
		return employees.findAll();
	}

	/**
	 * @param userAccountName name of the searched user account
	 * @return find and return user by its account name
	 */
	public User findUserByAccountName(String userAccountName) {
		User user = findCompanyByAccountName(userAccountName);
		if (user == null) {
			user = findCustomerByAccountName(userAccountName);
		}
		if (user == null) {
			user = findEmployeeByAccountName(userAccountName);
		}
		return user;
	}

	/**
	 * @param userAccountName name of the searched user account
	 * @return find and return company by its account name
	 */
	public Company findCompanyByAccountName(String userAccountName) {
		for (Company company : findAllCompanies()) {
			if (company.getUserAccount().getUsername().equals(userAccountName)) {
				return company;
			}
		}
		return null;
	}

	/**
	 * @param userAccountName name of the searched user account
	 * @return find and return customer by its account name
	 */
	public Customer findCustomerByAccountName(String userAccountName) {
		for (Customer c : findAllCustomers()) {
			if (c.getUserAccount().getUsername().equals(userAccountName)) {
				return c;
			}
		}
		return null;
	}

	/**
	 * @param userAccountName name of the searched user account
	 * @return find and return employee by its account name
	 */
	public Employee findEmployeeByAccountName(String userAccountName) {
		for (Employee e : findAllEmployees()) {
			if (e.getUserAccount().getUsername().equals(userAccountName)) {
				return e;
			}
		}
		return null;
	}

	////////////////////////////////////////////
	// changeAccounts

	/**
	 * @param form        contains new data
	 * @param userAccount updates company according to the form
	 */
	public void changeCompany(CompanySettingsForm form, @LoggedIn Optional<UserAccount> userAccount) {
		formNotNull(form);

		Company company = findCompanyByAccountName(userAccount.get().getUsername());

		String newName = form.getCompanyName();
		String newEmail = form.getEmail();

		if (!newName.isEmpty() && !newName.isBlank()) {
			company.setCompanyName(newName);
		}
		if (!newEmail.isEmpty() && emailIsFree(newEmail)) {
			company.setEmail(newEmail);
		}

		userAccountManager.save(userAccount.get());
	}

	/**
	 * @param form        contains new information
	 * @param userAccount updates customer according to the form
	 */
	public void changeCustomer(CustomerSettingsForm form, @LoggedIn Optional<UserAccount> userAccount) {
		formNotNull(form);

		String newForeName = form.getForename();
		String newSurName = form.getSurname();
		String newEmail = form.getEmail();

		Customer customer = findCustomerByAccountName(userAccount.get().getUsername());
		if (!newForeName.isEmpty() || !newForeName.isBlank()) {
			customer.setForename(newForeName);
		}
		if (!newSurName.isEmpty() || !newSurName.isBlank()) {
			customer.setSurname(newSurName);
		}
		if (!newEmail.isEmpty() && emailIsFree(newEmail)) {
			customer.setEmail(newEmail);
		}

		userAccountManager.save(userAccount.get());
	}

	/**
	 * @param form contains new information
	 * @param id   to identify the right employee
	 */
	public void changeEmployee(CustomerSettingsForm form, long id) {
		Assert.notNull(form, "CustomerRegistrationForm must not be null");

		String newForeName = form.getForename();
		String newSurName = form.getSurname();
		String newEmail = form.getEmail();

		Employee employee = employees.findById(id).get();

		if (!newForeName.isEmpty() || !newForeName.isBlank()) {
			employee.setForename(newForeName);
		}
		if (!newSurName.isEmpty() || !newSurName.isBlank()) {
			employee.setSurname(newSurName);
		}
		if (!newEmail.isEmpty() && emailIsFree(newEmail)) {
			employee.setEmail(newEmail);
		}

		userAccountManager.save(employee.getUserAccount());
	}

	////////////////////////////////////////////
	// create accounts

	/**
	 * @param form contains new customer information
	 * @return creates a customer according to the form
	 */
	public User createCustomer(CustomerRegistrationForm form) {
		formNotNull(form);

		UnencryptedPassword password = UnencryptedPassword.of(form.getPassword());
		UserAccount userAccount = userAccountManager.create(
				form.getForename() + " " + form.getSurname(),
				password,
				form.getEmail(),
				CUSTOMER_ROLE);

		return customers.save(new Customer(
				userAccount,
				form.getEmail(),
				form.getForename(),
				form.getSurname(),
				form.getAssociatedCompany(),
				LocalDate.MAX)
		);
	}

	/**
	 * @param form contains new company information
	 * @return creates a company according to the form
	 */
	public User createCompany(CompanyRegistrationForm form) {

		CompanyType type;
		switch (form.getCompanyType()) {
			case ("COMPANY"):
				type = CompanyType.COMPANY;
				break;
			case ("KINDERGARTEN"):
				type = CompanyType.KINDERGARTEN;
				break;
			case ("SCHOOL"):
				type = CompanyType.SCHOOL;
				break;
			default:
				type = CompanyType.COMPANY;
				break;
		}

		formNotNull(form);

		UnencryptedPassword password = UnencryptedPassword.of(form.getPassword());
		UserAccount userAccount = userAccountManager.create(form.getCompanyName(),
				password,
				form.getEmail(),
				COMPANY_ROLE);

		return companies.save(new Company(
				userAccount,
				form.getEmail(),
				form.getCompanyName(),
				type)
		);
	}

	/**
	 * @param form contains new employee information
	 * @return creates a (best-food-catering-)employee according to the form
	 */
	public User createEmployee(EmployeeRegistrationForm form) {

		formNotNull(form);

		UnencryptedPassword password = UnencryptedPassword.of(form.getPassword());
		var userAccount = userAccountManager.create(form.getForename() + " " + form.getSurname(),
				password,
				form.getEmail(),
				Role.of(form.getRole()));

		return employees.save(new Employee(
				userAccount,
				form.getEmail(),
				form.getForename(),
				form.getSurname(),
				form.getRole())
		);
	}

	////////////////////////////////////////////
	// activate/deactivate accounts

	/**
	 * @param id deactivates a customer with its ID
	 */
	public void deactivateCustomerById(long id) {
		for (User c : customers.findAll()) {
			if (c.getId() == id) {
				c.setLocked();
			}
		}
	}

	/**
	 * @param id deactivates a company with its ID
	 */
	public void deactivateCompanyById(long id) {
		for (User c : companies.findAll()) {
			if (c.getId() == id) {
				c.setLocked();
			}
		}
	}

	/**
	 * @param id activates a customer with its ID
	 */
	public void activateCustomerById(long id) {
		for (User c : customers.findAll()) {
			if (c.getId() == id) {
				c.setActive();
			}
		}
	}

	/**
	 * @param id activates a company with its ID
	 *           but only if the person is either admin, boss or accounting staff
	 */
	@PreAuthorize("hasAnyRole('ADMIN', 'BOSS', 'ACCOUNTING')")
	public void activateCompanyById(long id) {
		for (User c : companies.findAll()) {
			if (c.getId() == id) {
				c.setActive();
			}
		}
	}

	////////////////////////////////////////////
	// delete accounts

	/**
	 * @param u  contains the logged in userAccount, so companies cannot delete any other users
	 * @param id deletes an employee with its ID
	 */
	public void deleteCustomerById(@LoggedIn Optional<UserAccount> u, long id) {
		var customer = customers.findById(id).get();

		if (u.get().getUsername().equals(customer.getAssociatedCompanyName())) {
			customers.deleteById(id);
			deleteAccount(customer.getUserAccount());
		}
	}

	/**
	 * @param u  contains the logged in userAccount, so companies cannot delete any other companies
	 * @param id deletes the company of the ID and all of its attached customers!
	 */
	public void deleteCompanyById(@LoggedIn Optional<UserAccount> u, long id) {
		var account = companies.findById(id).get().getUserAccount();

		String companyName = companies.findById(id).get().getCompanyName();

		// deletes any account associated to this specific company
		for (Customer e : customers.findAll()) {
			if (e.getAssociatedCompanyName().equals(companyName)) {
				deleteCustomerById(u, e.getId());
			}
		}

		companies.deleteById(id);
		deleteAccount(account);
	}


	///////////////////////////////////////////////////////
	//Bitte nicht löschen sonst geht das Entfernen von Employees kaputt

	public void deleteEmployeeById(long id) {
		var employee = employees.findById(id).get();

		employees.deleteById(id);
		deleteAccount(employee.getUserAccount());

	}
	////////////////////////////////////////

	public boolean emailIsFree(String email) {
		boolean isFree = true;
		for (Customer c : findAllCustomers()) {
			if (c.getEmail().equals(email)) {
				isFree = false;
				break;
			}
		}
		for (Employee e : findAllEmployees()) {
			if (e.getEmail().equals(email)) {
				isFree = false;
				break;
			}
		}
		for (Company c : findAllCompanies()) {
			if (c.getEmail().equals(email)) {
				isFree = false;
				break;
			}
		}
		return isFree;
	}

	////////////////////////////////////////////
	// expire customers
	public void expireCustomerByIDOn(long id, LocalDate expireDate) {
		for (Customer c : findAllCustomers()) {
			if (c.getId() == id) {
				c.setExpireDate(expireDate);
			}
		}
	}

	/**
	 * each day on 0:00 this method is called for locking expired customers
	 */
	@Scheduled(cron = "* * * * * ?")
	public void expire() {
		for (Customer c : findAllCustomers()) {
			c.expire();
		}
	}


	// == private methods ==

	/**
	 * @param account delete account in userAccountManager
	 */
	private void deleteAccount(UserAccount account) {
		userAccountManager.delete(account);
	}

	/**
	 * @param form checks if form is not null
	 */
	private void formNotNull(UserForm form) {
		Assert.notNull(form, form.getClass().getCanonicalName() + " must not be null!");
	}

}