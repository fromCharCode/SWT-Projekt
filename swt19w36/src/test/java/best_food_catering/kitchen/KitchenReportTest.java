package best_food_catering.kitchen;

import best_food_catering.storage.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class KitchenReportTest {

	private KitchenReport report;

	@Autowired
	private KitchenService kitchenService;

	@BeforeEach
	void setUp() {
		this.report = new KitchenReport(5, 2018);
	}

    @Test
    void getId() {
		assertFalse(this.report.getId() != 0);
	}

    @Test
    void getCalenderWeek() {
		assertEquals(5, this.report.getCalenderWeek());
	}

    @Test
    void getYear() {
		assertEquals(2018, this.report.getYear());
	}

    @Test
    void getDate() {
		assertEquals(201805, this.report.getDate());
    }

    @Test
    void getUsedIngredients() {
		Map<Ingredient, Quantity> ingredients = new HashMap<>();

		for(Ingredient ingredient : kitchenService.getAllIngredients()) {
			ingredients.put(ingredient, Quantity.of(2));
		}

		this.report.setUsedIngredients(ingredients);
		assertEquals(ingredients, this.report.getUsedIngredients());

    }

}