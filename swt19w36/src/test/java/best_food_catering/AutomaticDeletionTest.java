package best_food_catering;


import best_food_catering.user.UserDataInitializer;
import ch.qos.logback.core.joran.spi.NoAutoStart;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class AutomaticDeletionTest {

	@Autowired
	MockMvc mvc;

	@Test
	@WithMockUser(username = "storage@bfc.com", roles = "STORAGE", password = "123")
	void testStorage() {
		try {
			mvc.perform(get("/login"))
					.andExpect(view().name("index"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	@WithMockUser(username = "kitchen@bfc.com", roles = "KITCHEN", password = "123")
	void testKitchen() {
		try {
			mvc.perform(get("/login"))
					.andExpect(view().name("index"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	@WithMockUser(username = "accounting@bfc.com", roles = "ACCOUNTING", password = "123")
	void testAccounting() {
		try {
			mvc.perform(get("/login"))
					.andExpect(view().name("index"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	@WithMockUser(username = "admin@bfc.com", roles = "ADMIN", password = "123")
	void testAdmin() {
		try {
			mvc.perform(get("/login"))
					.andExpect(view().name("index"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}