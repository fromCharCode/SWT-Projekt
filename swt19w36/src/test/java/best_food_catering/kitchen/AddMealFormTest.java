package best_food_catering.kitchen;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AddMealFormTest {

	private AddMealForm form;

	@Autowired
	private KitchenService service;

    @BeforeEach
    void setUp() {
    	this.form = new AddMealForm("Gericht", "Beschreibug", 5.0F,"Rezept",
				"Normal");
    }

    @Test
    void getName() {
    	assertEquals("Gericht", this.form.getName());
    }

    @Test
    void getMoney() {
		assertEquals(5.0F, this.form.getMoney());
	}

    @Test
    void getRecipe() {
		assertEquals("Gericht", this.form.getName());
	}

    @Test
    void getType() {
		assertEquals("Normal", this.form.getType());
	}

    @Test
    void getDescription() {
		assertEquals("Beschreibug", this.form.getDescription());
	}

    @Test
    void getRows() {
    	AddMealRow row0 = new AddMealRow();
    	row0.setIngredientID(555555555);
    	row0.setQuantityAmount(5);

		AddMealRow row1 = new AddMealRow();
		row1.setIngredientID(555555555);
		row1.setQuantityAmount(5);

		this.form.getRows().add(row0);
		this.form.getRows().add(row1);

		List<AddMealRow> expected = new ArrayList<>();
		expected.add(row0);
		expected.add(row1);

		assertEquals(expected, this.form.getRows());
	}

    @Test
    void convertMealTypeFromHTML() {
		assertEquals(Meal.FoodType.NORMAL, this.form.convertMealTypeFromHTML(this.form.getType()));

	}
}