package best_food_catering.user;


import best_food_catering.user.company.CompanyRegistrationForm;
import best_food_catering.user.company.CompanyRepository;
import best_food_catering.user.company.CompanySettingsForm;
import best_food_catering.user.company.CompanyType;
import best_food_catering.user.customer.CustomerRegistrationForm;
import best_food_catering.user.customer.CustomerRepository;
import best_food_catering.user.customer.CustomerSettingsForm;
import best_food_catering.user.employee.EmployeeRegistrationForm;
import best_food_catering.user.employee.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest
@Transactional
class UserServiceTest {


	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private UserService userService;

	/*
	@BeforeAll
	void setUp() {

	}*/

	// == TEST CREATION ==

	@Test
	void createCustomer(){

		CustomerRepository customerRepository = mock(CustomerRepository.class);
		when(customerRepository.save(any())).then(i -> i.getArgument(0));

		UserAccountManager userAccountManager = mock(UserAccountManager.class);
		when(userAccountManager.create(any(), any(), any())).thenReturn(new UserAccount());

		CustomerRegistrationForm crForm = new CustomerRegistrationForm(
				"hans",
				"wurst",
				"123",
				"a@a.com",
				"Valve Corporation"
		);

		String username = crForm.getForename() + " " + crForm.getSurname();
		userService.createCustomer(crForm);
		var user = userService.findUserByAccountName(username);
		//var user = customerRepository.findById();
		assertEquals(user.getEmail(), "a@a.com");
		assertEquals(user.getUserAccount().getUsername(), "hans wurst");
		assertTrue(user.isCustomer());
		assertTrue(user.isWaiting());

		userService.activateCustomerById(user.getId());
		assertTrue(!user.isLocked() && !user.isWaiting());

		userService.deactivateCustomerById(user.getId());
		assertTrue(user.isLocked());

		//userService.deleteCustomerById(user.getId());
		//assertNull(userService.findCustomerByAccountName(user.getUserAccount().getUsername()));
	}

	@Test
	void createEmployee(){
		EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
		when(employeeRepository.save(any())).then(i -> i.getArgument(0));

		UserAccountManager userAccountManager = mock(UserAccountManager.class);
		when(userAccountManager.create(any(), any(), any())).thenReturn(new UserAccount());

		EmployeeRegistrationForm erForm = new EmployeeRegistrationForm(
			"kitchen",
			"chef",
			"123",
			"a@kitchen.com",
			UserRole.KITCHEN.toString()
		);

		String username = erForm.getForename() + " " + erForm.getSurname();
		userService.createEmployee(erForm);
		var user = userService.findUserByAccountName(username);
		//var user = customerRepository.findById();
		assertEquals(user.getEmail(), "a@kitchen.com");
		assertEquals(user.getUserAccount().getUsername(), "kitchen chef");
		assertTrue(user.isEmployee());
		assertTrue(!user.isLocked() && !user.isWaiting());


	}

	@Test
	void createCompany(){
		CompanyRepository companyRepository = mock(CompanyRepository.class);
		when(companyRepository.save(any())).then(i -> i.getArgument(0));

		UserAccountManager userAccountManager = mock(UserAccountManager.class);
		when(userAccountManager.create(any(), any(), any())).thenReturn(new UserAccount());

		CompanyRegistrationForm companyForm = new CompanyRegistrationForm(
			"best",
			"123",
			"comp@any.com",
			CompanyType.COMPANY.toString()
		);

		String username = companyForm.getCompanyName();
		userService.createCompany(companyForm);
		var user = userService.findUserByAccountName(username);
		//var user = customerRepository.findById();
		assertEquals(user.getEmail(), "comp@any.com");
		assertEquals(user.getUserAccount().getUsername(), "best");
		assertTrue(user.isCompany());
		assertTrue(!user.isLocked() && !user.isWaiting());


		//userService.deleteCompanyById(user.getId());
		//assertNull(userService.findCompanyByAccountName(user.getUserAccount().getUsername()));
	}

	// == TEST CHANGES ==
	@Test
	void changeCompany(){

		CompanyRepository companyRepository = mock(CompanyRepository.class);
		when(companyRepository.save(any())).then(i -> i.getArgument(0));

		UserAccountManager userAccountManager = mock(UserAccountManager.class);
		when(userAccountManager.create(any(), any(), any())).thenReturn(new UserAccount());

		CompanyRegistrationForm companyForm = new CompanyRegistrationForm(
			"best",
			"123",
			"comp@any.com",
			CompanyType.COMPANY.toString()
		);

		CompanySettingsForm companySettingsForm = new CompanySettingsForm(
			"better",
			"any@comp.com"
		);

		String username = companyForm.getCompanyName();
		userService.createCompany(companyForm);
		var company = userService.findCompanyByAccountName(username);
		assertEquals(company.getUserAccount().getUsername(), "best");
		assertEquals(company.getEmail(), "comp@any.com");

		userService.changeCompany(companySettingsForm, (Optional.of(company.getUserAccount())));
		assertEquals(company.getCompanyName(), "better");
		assertEquals(company.getUserAccount().getEmail(), "any@comp.com");
	}

	@Test
	void changeCustomer(){

		CustomerRepository customerRepository = mock(CustomerRepository.class);
		when(customerRepository.save(any())).then(i -> i.getArgument(0));

		UserAccountManager userAccountManager = mock(UserAccountManager.class);
		when(userAccountManager.create(any(), any(), any())).thenReturn(new UserAccount());

		CustomerRegistrationForm customerRegistrationForm = new CustomerRegistrationForm(
			"hans",
			"wurst",
			"123",
			"a@a.com",
			"Valve Corporation"
		);


		CustomerSettingsForm customerSettingsForm = new CustomerSettingsForm(
			"Peter",
			"Altmaier",
			"b@b.com"
		);

		String username = customerRegistrationForm.getForename() + " " + customerRegistrationForm.getSurname();
		userService.createCustomer(customerRegistrationForm);
		var customer = userService.findCustomerByAccountName(username);
		assertEquals(customer.getUserAccount().getUsername(), "hans wurst");
		assertEquals(customer.getEmail(), "a@a.com");

		userService.changeCustomer(customerSettingsForm, (Optional.of(customer.getUserAccount())));
		assertEquals(customer.getForename(), "Peter");
		assertEquals(customer.getSurname(), "Altmaier");
		assertEquals(customer.getUserAccount().getEmail(), "b@b.com");
	}

	@Test
	void changeEmployee(){

		EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
		when(employeeRepository.save(any())).then(i -> i.getArgument(0));

		UserAccountManager userAccountManager = mock(UserAccountManager.class);
		when(userAccountManager.create(any(), any(), any())).thenReturn(new UserAccount());

		EmployeeRegistrationForm employeeRegistrationForm = new EmployeeRegistrationForm(
			"hans",
			"wurst",
			"123",
			"a@a.com",
			UserRole.ACCOUNTING.getDisplayValue()
		);


		CustomerSettingsForm customerSettingsForm = new CustomerSettingsForm(
			"Peter",
			"Altmaier",
			"b@b.com"
		);

		String username = employeeRegistrationForm.getForename() + " " + employeeRegistrationForm.getSurname();
		userService.createEmployee(employeeRegistrationForm);
		var employee = userService.findEmployeeByAccountName(username);
		assertEquals(employee.getUserAccount().getUsername(), "hans wurst");
		assertEquals(employee.getEmail(), "a@a.com");

		userService.changeEmployee(customerSettingsForm, employee.getId());
		assertEquals(employee.getForename(), "Peter");
		assertEquals(employee.getSurname(), "Altmaier");
		assertEquals(employee.getUserAccount().getEmail(), "b@b.com");
	}

	@Test
	void emailTest(){
		CompanyRepository companyRepository = mock(CompanyRepository.class);
		when(companyRepository.save(any())).then(i -> i.getArgument(0));

		UserAccountManager userAccountManager = mock(UserAccountManager.class);
		when(userAccountManager.create(any(), any(), any())).thenReturn(new UserAccount());

		CustomerRegistrationForm crForm = new CustomerRegistrationForm(
			"hans",
			"wurst",
			"123",
			"a@a.com",
			"Valve Corporation"
		);

		CompanyRegistrationForm companyForm = new CompanyRegistrationForm(
			"best",
			"123",
			"comp@any.com",
			CompanyType.COMPANY.toString()
		);

		CustomerSettingsForm customerSettingsForm = new CustomerSettingsForm(
			"Peter",
			"Altmaier",
			"comp@any.com"
		);


		String username = crForm.getForename() + " " + crForm.getSurname();

		userService.createCompany(companyForm);
		userService.createCustomer(crForm);

		var customer = userService.findCustomerByAccountName(username);

		assertEquals(customer.getEmail(), "a@a.com");

		userService.changeCustomer(customerSettingsForm, (Optional.of(customer.getUserAccount())));

	}
}