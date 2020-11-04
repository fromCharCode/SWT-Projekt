package best_food_catering.kitchen;

import best_food_catering.storage.Ingredient;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MealTest {

	private Meal meal;

	@Autowired
	private KitchenService kitchenService;

	@BeforeEach
	void setUp() {
		Map<Ingredient, Quantity> ingredients = new HashMap<>();

		for(Ingredient ingredient : kitchenService.getAllIngredients()) {
			ingredients.put(ingredient, Quantity.of(2));
		}
		this.meal = new Meal("Gericht", "Beschreibug", Money.of(5, "EUR"),
				ingredients, "Rezept", Meal.FoodType.NORMAL);
	}

	@Test
	void testGetPrize() {
		assertEquals(Money.of(5, "EUR"), this.meal.getPrice());
	}

	@Test
	void testGetIngredients() {
		assertFalse(this.meal.getIngredients().isEmpty());
	}

	@Test
	void testGetName() {
		assertEquals("Gericht", this.meal.getName());
	}

	@Test
	void testGetRecipe() {
		assertEquals("Rezept", this.meal.getRecipe());
	}

	@Test
	void testGetDescription() {
		assertEquals("Beschreibug", this.meal.getDescription());
	}

	@Test
	void testGetFoodType() {
		assertEquals(Meal.FoodType.NORMAL, this.meal.getFoodType());
	}

	@Test
	void testGetID() {
		assertNotNull(this.meal.getId());
	}

	@Test
	void testGetPrettyType() {
		assertEquals("Normal", this.meal.getPrettyType());
	}



}