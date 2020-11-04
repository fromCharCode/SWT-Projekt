package best_food_catering.user.employee;


import best_food_catering.user.UserService;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private UserService userServiceMock;

	@Autowired EmployeeController controller;

	@Test
	@WithMockUser(username = "admin admin", roles = ("ADMIN"))
	void accessAdminPanel() throws Exception {
		mvc.perform(get("/admin"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("employeeRegistrationForm", "employeeList", "employeeChangeForm"));
	}

	@Test
	@WithMockUser(username = "admin admin", roles = "ADMIN")
	void testRegisterEmployee() throws Exception{

	}
	/*
	@PostMapping("/registerEmployee")
	@PreAuthorize("isAuthenticated()")
	public String registerEmployee(@Valid EmployeeRegistrationForm form, Errors result){
		if(result.hasErrors()){
			return "login";
		}

		userService.createEmployee(form);
		return "redirect:/admin";
	}
	 */


	@Test
	@WithMockUser(roles = "BOSS")
	void registerEmployee() throws Exception {

		mvc.perform(post("/registerEmployee")) //
			.andExpect(status().is4xxClientError());
	}


//	@Test
//	@WithMockUser(roles = "BOSS")
//	void deleteEmployee() throws Exception {
//
//		mvc.perform(get("/deleteEmployee")) //
//			.andExpect(status().isOk())
//			.andExpect(view().name("redirect:/admin"));
//	}

	@Test
	@WithMockUser(roles = "BOSS")
	void changeEmployee() throws Exception {
		mvc.perform(post("/changeEmployee")) //
			.andExpect(status().is4xxClientError());
	}

	@Test
	void generateEmployee(){

		EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
		when(employeeRepository.save(any())).then(i -> i.getArgument(0));

		UserAccountManager userAccountManager = mock(UserAccountManager.class);
		when(userAccountManager.create(any(), any(), any())).thenReturn(new UserAccount());

		EmployeeRegistrationForm crForm = new EmployeeRegistrationForm(
			"name",
			"nachname",
			"123",
			"a@a.com",
			"ADMIN"

		);

		String username = crForm.getForename() + " " + crForm.getSurname();
		userServiceMock.createEmployee(crForm);
		var user = userServiceMock.findUserByAccountName(username);
		//var user = customerRepository.findById();
		assertTrue(user.getEmail().equals("a@a.com"));


		//assertThrows(IllegalArgumentException.class, () -> new User(null , "mail@exammple.de", UserStatus.WAITING ));
	}

	@Test
	void deleteEmployee(){

		EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
		when(employeeRepository.save(any())).then(i -> i.getArgument(0));

		UserAccountManager userAccountManager = mock(UserAccountManager.class);
		when(userAccountManager.create(any(), any(), any())).thenReturn(new UserAccount());

		EmployeeRegistrationForm erForm = new EmployeeRegistrationForm(
			"howAbout",
			"readTheErrors",
			"123",
			"c@c.com",
			"KITCHEN"
		);

		String username = erForm.getForename() + " " + erForm.getSurname();
		userServiceMock.createEmployee(erForm);
		var user = userServiceMock.findUserByAccountName(username);
		//var user = customerRepository.findById();

//		//Testing delete Employee
		var employeeId = userServiceMock.findEmployeeByName(username).getId();
		userServiceMock.deleteEmployeeById(employeeId);
		assertNull(userServiceMock.findEmployeeByName(username));


	}
}
