package best_food_catering.kitchen;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class KitchenControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private KitchenController controller;

	@Test
	@WithMockUser(username = "Koch Hubert", roles = ("KITCHEN"))
	void index() throws Exception {
		mvc.perform(get("/kitchen"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("menu"));
	}

    @Test
	@WithMockUser(username = "Koch Hubert", roles = ("KITCHEN"))
	void dailyOrders() throws Exception {
		int dayOfWeek = LocalDate.now().getDayOfWeek().getValue();
		mvc.perform(get("/kitchen/daily"))
				.andExpect(status().isOk())
				.andExpect(model().attributeDoesNotExist("report"))
				.andExpect(model().attribute("day", dayOfWeek));
	}


	@Test
	@WithMockUser(username = "Koch Hubert", roles = ("KITCHEN"))
	void getMeals() throws Exception {
		mvc.perform(get("/kitchen/meals"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("ingredients"))
				.andExpect(model().attributeExists("meals"));
	}

	@Test
	@WithMockUser(username = "Koch Hubert", roles = ("KITCHEN"))
	void newMeal() throws Exception {
		mvc.perform(get("/kitchen/meals/new"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("mealForm"))
				.andExpect(model().attributeExists("ingredients"))
				.andExpect(model().attributeExists("meals"));
	}

    @Test
	@WithMockUser(username = "Koch Hubert", roles = ("KITCHEN"))
	void createNewMeal() throws Exception{
		mvc.perform(post("/kitchen/meals/new/post"))
				.andExpect(status().isForbidden());
    }

	@Test
	@WithMockUser(username = "Koch Hubert", roles = ("KITCHEN"))
	void removeMeal() throws Exception{
		mvc.perform(post("/kitchen/meals/delete"))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "Koch Hubert", roles = ("KITCHEN"))
	void getMenus() throws Exception {
		mvc.perform(get("/kitchen/menus"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("menus"));
	}

	@Test
	@WithMockUser(username = "Koch Hubert", roles = ("KITCHEN"))
	void newMenu() throws Exception {
		mvc.perform(get("/kitchen/menus/new"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("menuForm"))
				.andExpect(model().attributeExists("normals"))
				.andExpect(model().attributeExists("specials"))
				.andExpect(model().attributeExists("diets"));
	}

	@Test
	@WithMockUser(username = "Koch Hubert", roles = ("KITCHEN"))
	void addMealFormRow() throws Exception{
		mvc.perform(post("kitchen/meals/new/post").header("action", "addrow"))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "Koch Hubert", roles = ("KITCHEN"))
	void deleteMealFormRow() throws Exception{
		mvc.perform(post("kitchen/meals/new/post").header("action", "removerow"))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "Koch Hubert", roles = ("KITCHEN"))
	void removeMenu() throws Exception{
		mvc.perform(post("/kitchen/menus/delete"))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "Koch Hubert", roles = ("KITCHEN"))
	void createNewMenu() throws Exception{
		mvc.perform(post("/kitchen/menus/new/post"))
				.andExpect(status().isForbidden());
	}
}