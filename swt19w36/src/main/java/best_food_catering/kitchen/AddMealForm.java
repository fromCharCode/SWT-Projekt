package best_food_catering.kitchen;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jonas Bohmann
 */

public class AddMealForm {

	@NotEmpty(message = "Name is empty")
	private final String name;

	@NotEmpty(message = "Description is empty")
	private final String description;

	@NotNull(message = "Price is empty")
	private final Float money;

	@NotEmpty(message = "Recipe is empty")
	private final String recipe;

	@NotEmpty(message = "Type is empty")
	private final String type;

	private List<AddMealRow> rows = new ArrayList<AddMealRow>();

	/**
	 * Form for creating new Meal objects.
	 *
	 * @param name        name of the meal
	 * @param description description of the meal
	 * @param money       the meal's price
	 * @param recipe      recipe instructions for the cook
	 * @param type        the meal's menu-line {@link Meal.FoodType}
	 */
	public AddMealForm(String name, String description, Float money, String recipe, String type) {
		this.name = name;
		this.description = description;
		this.money = money;
		this.recipe = recipe;
		this.type = type;

	}

	public String getName() {
		return name;
	}

	/**
	 * @return the meal's price
	 * @throws IllegalArgumentException if the amount is negative
	 */
	public Float getMoney() {
		if (this.money != null && this.money < 0) {
			throw new IllegalArgumentException("Money has to be positive!");
		}
		return this.money;
	}

	public String getRecipe() {
		return recipe;
	}


	public String getType() {
		return type;
	}

	public String getDescription() {
		return description;
	}

	public List<AddMealRow> getRows() {
		return this.rows;
	}

	/**
	 * @param type the meal's type in german
	 * @return the type as {@link Meal.FoodType}
	 */
	public Meal.FoodType convertMealTypeFromHTML(String type) {
		Meal.FoodType foodType = Meal.FoodType.NORMAL;
		switch (type) {
			case "Diät":
				foodType = Meal.FoodType.DIET;
				break;
			case "Spezial":
				foodType = Meal.FoodType.SPECIAL;
				break;
			case "Normal":
			default:
				foodType = Meal.FoodType.NORMAL;
		}
		return foodType;
	}

}
