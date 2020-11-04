package best_food_catering.Purchase;

import best_food_catering.kitchen.KitchenService;
import best_food_catering.kitchen.Menu;
import best_food_catering.purchase.DebitsCardForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PurchaseControllerTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	private KitchenService kitchenService;

	@Test
	@WithMockUser(username = "user user", roles = ("USER"))
	void testGroupBilling() throws Exception{
		mvc.perform(get("/singleBilling")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "user user", roles = ("USER"))
	@Transactional
	void testCart() throws Exception{
		// Needs to have an UserAccount, throws 400 Bad Request otherwise
		mvc.perform(get("/cart")).andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(username = "user user", roles = ("USER"))
	void testMenu() throws Exception{
		Menu menu = kitchenService.getMenuForNextWeek();

		assertNotEquals(menu, "Menu is null");
		//assertTrue(menu.getSortedNormalMeals().size() == 5, "Size has to be 5");
		//assertTrue(menu.getSortedDietMeals().size() == 5, "Size has to be 5");
		//assertTrue(menu.getSortedSpecialMeals().size() == 5, "Size has to be 5");

		mvc.perform(get("/menu")).andExpect(status().isOk());
	}

	@Test
	void singleBillingTest(){
		DebitsCardForm debitsCardForm = new DebitsCardForm(
			"test",
			"asdasd",
			"2342341234",
			"asdasd",
			"asdddw",
			LocalDate.now(),
			LocalDate.MAX,
			"asdasdjasd",
			50
		);


	}
}