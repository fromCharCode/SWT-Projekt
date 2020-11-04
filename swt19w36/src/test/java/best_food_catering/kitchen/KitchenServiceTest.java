package best_food_catering.kitchen;

import best_food_catering.storage.Ingredient;
import com.mysema.commons.lang.Assert;
import org.salespointframework.catalog.ProductIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
class KitchenServiceTest {

	@Autowired
	private KitchenService service;

	@Test
	void testGetAllUniqueMeals() {
		List<Meal> mealList = service.getAllUniqueMeals();
		Set<Meal> set = new HashSet<Meal>(mealList);
		Assert.isFalse(mealList.isEmpty(), "No meals were found!");
		Assert.isTrue(mealList.size() == set.size(), "UniqueMeals found duplicate meals!");
	}

	@Test
	void testGetAllMeals() {
		List<Meal> mealList = service.getAllMeals();
		Assert.isFalse(mealList.isEmpty(), "No meals were found!");
	}

	@Test
	void testGetAllMenus() {
		List<Menu> menus = service.getAllMenus();
		Assert.isFalse(menus.isEmpty(), "No menus were found!");
	}

	@Test
	void testGetAllIngredients() {
		List<Ingredient> ings = service.getAllIngredients();
		Assert.isFalse(ings.isEmpty(), "No ingredients were found!");
	}

	@Test
	void testCreateNewMeal() {
		AddMealForm addMealForm = new AddMealForm("Bratwurscht", "Lecker", 5.0F,
				"Braten", "Diät");
		service.createNewMeal(addMealForm);
		Assert.isFalse(service.getMealByName("Bratwurscht").isEmpty(), "Meal was not created");
	}

	@Test
	void testCreateNewMenu() {
		AddMenuForm form = new AddMenuForm("2019-W51");
		ProductIdentifier testMeal = service.getMealByName("Echte italienische Lasagne mit Büffelmozzarella").get().findFirst().get().getId();
		
		form.setNormalMonday(testMeal);
		form.setNormalTuesday(testMeal);
		form.setNormalWednesday(testMeal);
		form.setNormalThursday(testMeal);
		form.setNormalFriday(testMeal);

		form.setDietMonday(testMeal);
		form.setDietTuesday(testMeal);
		form.setDietWednesday(testMeal);
		form.setDietThursday(testMeal);
		form.setDietFriday(testMeal);

		form.setSpecialMonday(testMeal);
		form.setSpecialTuesday(testMeal);
		form.setSpecialWednesday(testMeal);
		form.setSpecialThursday(testMeal);
		form.setSpecialFriday(testMeal);

		service.createNewMenu(form);
		Assert.isTrue(service.getMenuByDate(201951) != null, "Menu was not created");

	}

	@Test
	void testGetAllNormals() {
		List<Meal> mealList = service.getAllNormalMeals();
		Meal dietMeal = service.getMealByName("Mozerellasticks mit leichtem Joghurt-Dip und griechischem Salat").get().findFirst().get();
		Assert.isFalse(mealList.isEmpty(), "No meals were found!");
		Assert.isFalse(mealList.contains(dietMeal), "Normal Meals includes diet meal!");

	}

	@Test
	void testGetAllDiets() {
		List<Meal> mealList = service.getAllDietMeals();
		Meal normalMeal = service.getMealByName("Mostbraten vom Rind mit Rosenkohl und Kartoffelklößen").get().findFirst().get();
		Assert.isFalse(mealList.isEmpty(), "No meals were found!");
		Assert.isFalse(mealList.contains(normalMeal), "Diet Meals includes normal meal!");

	}

	@Test
	void testGetAllSpecials() {
		List<Meal> mealList = service.getAllSpecialMeals();
		Meal dietMeal = service.getMealByName("Gebratene Zucchini mit Sojacreme-Avocado und Tomatenpesto-Kartoffeln").get().findFirst().get();
		Assert.isFalse(mealList.isEmpty(), "No meals were found!");
		Assert.isFalse(mealList.contains(dietMeal), "Special Meals includes diet meal!");

	}

	@Test
	void testGetCurrentDate() {
		Assert.isTrue(service.getCurrentDate().length == 3, "Invalid current date!");
	}

	@Test
	void testGetNextWeeksDate() {
		Assert.isTrue(service.getDateOfNextWeek().length == 3, "Invalid date!");
		Assert.isFalse(Arrays.equals(service.getDateOfNextWeek(), service.getCurrentDate()),
				"This weeks date is equal to next weeks date!!");
	}

	@Test
	void testGetCurrentMenu() {
		Assert.isFalse(service.getMenuForCurrentWeek() == null, "Current menu is null!");
	}

	@Test
	@Transactional
	void testGetNextMenu() {
		Menu menu = service.getMenuForNextWeek();
		Assert.notNull(menu, "Next week's menu is null!");
		Assert.isTrue(menu.getSortedNormalMeals().size() == 5, "Size has to be 5");
		Assert.isTrue(menu.getSortedDietMeals().size() == 5, "Size has to be 5");
		Assert.isTrue(menu.getSortedSpecialMeals().size() == 5, "Size has to be 5");
	}

	@Test
	@Transactional
	void testDeleteMeal() {
		Meal exampleMeal = service.getAllMeals().get(0);
		service.deleteMeal(exampleMeal);
		Assert.isTrue(service.getMealByName(exampleMeal.getName()).isEmpty(), "Meal was not deleted!");
	}

	@Test
	void testGetOOSReportForCurrentWeek() {
		Assert.isTrue(service.getOOSReportForCurrentWeek() == null,
				"Found report that should'nt exist!");
	}
}