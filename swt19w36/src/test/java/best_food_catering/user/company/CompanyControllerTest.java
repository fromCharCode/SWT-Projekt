package best_food_catering.user.company;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CompanyControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private CompanyController controller;

	@Test
	@WithMockUser(username = "Valve Corporation", roles = ("COMPANY"))
	void allowEmployeesAccess() throws Exception {
		mvc.perform(get("/employees"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("users"));
	}


}