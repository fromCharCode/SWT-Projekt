package best_food_catering.ingredient;

import best_food_catering.kitchen.KitchenService;
import best_food_catering.storage.Ingredient;
import best_food_catering.storage.IngredientForm;
import best_food_catering.storage.IngredientRepository;
import best_food_catering.storage.IngredientService;
import org.junit.jupiter.api.Test;
import org.salespointframework.quantity.Metric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IngredientServiceTest {

	@Autowired
	private IngredientRepository ingredientRepository;
	@Autowired
	private KitchenService kitchenService;

	@Autowired
	private IngredientService ingredientService;


	@Test
	void testDeleteItem(){

		IngredientForm ingredientForm = new IngredientForm(
			"Rice",
			3.50d,
			Metric.KILOGRAM.toString(),
			2d,
			LocalDate.now(),
			50f
		);

		Ingredient ingredient = ingredientService.createIngredient(ingredientForm);
		assertNotNull(ingredient);

		ingredientService.deleteIngredient(ingredient);
		for (Ingredient i : ingredientService.findAll()){
			assertNotEquals(ingredient, i);
		}
	}

	@Test
	void testDeleteItemPerID(){

		IngredientForm ingredientForm = new IngredientForm(
			"Senf",
			3.50d,
			Metric.KILOGRAM.toString(),
			2d,
			LocalDate.now(),
			50f
		);

		Ingredient ingredient = ingredientService.createIngredient(ingredientForm);
		assertNotNull(ingredient);

		ingredientService.deleteIngredientById(ingredient.getId());
		for (Ingredient i : ingredientService.findAll()){
			assertNotEquals(ingredient, i);
		}
	}

	@Test
	void testFindByBestBeforeLessThan(){
		IngredientForm ingredientForm = new IngredientForm(
			"Senf",
			3.50d,
			Metric.KILOGRAM.toString(),
			2d,
			LocalDate.EPOCH,
			50f
		);

		Ingredient ingredient = ingredientService.createIngredient(ingredientForm);
		assertNotNull(ingredient);

		Iterable<Ingredient> rotten = ingredientService.findByBestBeforeLessThan();

		boolean isIn = false;

		for(Ingredient i : rotten){
			if(i.getId() == ingredient.getId()){
				isIn = true;
			}
		}
		assertTrue(isIn);
	}



}