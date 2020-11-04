package best_food_catering.kitchen;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MenuTest {

	private Menu menu;

	@Autowired
	private KitchenService kitchenService;

	@BeforeEach
	void setUp() {
		this.menu = new Menu(3, 2018);
	}


	@Test
	void getCalenderWeek() {
		assertEquals(3, this.menu.getCalenderWeek());
	}

	@Test
	void getDate() {
		assertEquals(201803, this.menu.getDate());

	}

	@Test
	void getYear() {
		assertEquals(2018, this.menu.getYear());

	}

	@Test
	void getMealsPerWeekday() {
		Meal meal = kitchenService.getAllMeals().get(0);
		this.menu.addMeal(meal, DayOfWeek.TUESDAY);
		assertTrue(this.menu.getMealsPerWeekday(DayOfWeek.TUESDAY).contains(meal));
	}

	@Test
	void getMealsPerType() {
		Meal meal = kitchenService.getAllMeals().get(0);
		this.menu.addMeal(meal, DayOfWeek.TUESDAY);
		assertTrue(this.menu.getMealsPerType(meal.getFoodType()).containsValue(meal));
	}

	@Test
	void getWeekdayFromMeal() {
		Meal meal = kitchenService.getAllMeals().get(0);
		this.menu.addMeal(meal, DayOfWeek.TUESDAY);
		assertEquals("Dienstag", this.menu.getWeekdayFromMeal(meal));

		Meal meal2 = kitchenService.getAllMeals().get(1);
		this.menu.addMeal(meal2, DayOfWeek.FRIDAY);
		assertEquals("Freitag", this.menu.getWeekdayFromMeal(meal2));
	}

	@Test
	void getNormalMeals() {
		Meal meal = kitchenService.getAllNormalMeals().get(0);
		this.menu.addMeal(meal, DayOfWeek.TUESDAY);
		assertTrue(this.menu.getNormalMeals().containsValue(meal));
	}

	@Test
	void getSpecialMeals() {
		Meal meal = kitchenService.getAllSpecialMeals().get(0);
		this.menu.addMeal(meal, DayOfWeek.TUESDAY);
		assertTrue(this.menu.getSpecialMeals().containsValue(meal));
	}

	@Test
	void getDietMeals() {
		Meal meal = kitchenService.getAllDietMeals().get(0);
		this.menu.addMeal(meal, DayOfWeek.TUESDAY);
		assertTrue(this.menu.getDietMeals().containsValue(meal));
	}

	@Test
	void getSortedNormalMeals() {
		Meal meal = kitchenService.getAllNormalMeals().get(0);
		this.menu.addMeal(meal, DayOfWeek.TUESDAY);
		Meal meal2 = kitchenService.getAllNormalMeals().get(2);
		this.menu.addMeal(meal2, DayOfWeek.MONDAY);
		List<Meal> expected = new ArrayList<>();
		expected.add(meal2);
		expected.add(meal);
		assertEquals(expected, this.menu.getSortedNormalMeals());
	}

	@Test
	void getSortedDietMeals() {
		Meal meal = kitchenService.getAllDietMeals().get(0);
		this.menu.addMeal(meal, DayOfWeek.TUESDAY);
		Meal meal2 = kitchenService.getAllDietMeals().get(2);
		this.menu.addMeal(meal2, DayOfWeek.MONDAY);
		List<Meal> expected = new ArrayList<>();
		expected.add(meal2);
		expected.add(meal);
		assertEquals(expected, this.menu.getSortedDietMeals());
	}

	@Test
	void getSortedSpecialMeals() {
		Meal meal = kitchenService.getAllSpecialMeals().get(0);
		this.menu.addMeal(meal, DayOfWeek.TUESDAY);
		Meal meal2 = kitchenService.getAllSpecialMeals().get(2);
		this.menu.addMeal(meal2, DayOfWeek.MONDAY);
		List<Meal> expected = new ArrayList<>();
		expected.add(meal2);
		expected.add(meal);
		assertEquals(expected, this.menu.getSortedSpecialMeals());
	}
}