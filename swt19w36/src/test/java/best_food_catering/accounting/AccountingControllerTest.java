package best_food_catering.accounting;

import ch.qos.logback.core.joran.spi.NoAutoStart;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class AccountingControllerTest {

	@Autowired
	private MockMvc mvc;

	@Test
	@WithMockUser(username = "Accounting", roles = ("ACCOUNTING"))
	void accountingTest() throws Exception {
		mvc.perform(get("/accounting"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("allOrders"))
				.andExpect(model().attributeExists("pageTitle"))
				.andExpect(model().attributeExists("companies"))
				.andExpect(model().attributeExists("customers"))
				.andExpect(view().name("accounting"));

		mvc.perform(get("/accounting/deactivateCompany?id=4"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/accounting"));

		mvc.perform(get("/accounting/deactivate?id=4"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/accounting"));

		mvc.perform(get("/accounting/activateCustomer?id=4"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/accounting"));

		mvc.perform(get("/accounting/activateCompany?id=4"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/accounting"));
	}

}