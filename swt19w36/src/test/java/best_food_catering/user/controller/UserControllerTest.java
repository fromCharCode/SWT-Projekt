package best_food_catering.user.controller;

import best_food_catering.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private UserService userServiceMock;

	@Test
	void preventPublicAccess() throws Exception {

		mvc.perform(get("/settings")).andExpect(status().isFound())
				.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/")));

		mvc.perform(get("/registerUser")).andExpect(status().is(405));

	}

	@Test
	@WithAnonymousUser
	void allowRegistrationAccess() throws Exception {
		mvc.perform(get("/register")).andExpect(status().isOk());
	}


}