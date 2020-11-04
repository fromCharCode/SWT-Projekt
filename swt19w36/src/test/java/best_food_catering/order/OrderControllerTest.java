package best_food_catering.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

	@Autowired
	MockMvc mvc;

	@Test
	@WithMockUser(username = "Hans", roles = "CUSTOMER")
	void testOrder() throws Exception{
		mvc.perform(get("/myorders"))
				.andExpect(status().is4xxClientError());
	}
}
