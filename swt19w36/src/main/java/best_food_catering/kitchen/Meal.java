package best_food_catering.kitchen;

import best_food_catering.storage.Ingredient;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import org.salespointframework.quantity.Quantity;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @author Jonas Bohmann
 */

@Entity
public class Meal extends Product {

	public enum FoodType {
		NORMAL, SPECIAL, DIET
	}

	public enum Size {
		BIG, SMALL
	}

	private @NotEmpty @NotNull String description;
	private @NotNull FoodType foodType;

	// This attribute is referenced as "recipe" in the developer documentation
	private @ElementCollection
	@NotNull Map<Ingredient, Quantity> ingredients;

	// This is not the "recipe" from the developer documentation, instead this a string with instructions for the cook,
	// not a list of ingredients
	private @NotEmpty @NotNull String recipe;

	/**
	 * Our Salespoint {@link Product}, the Meal.
	 *
	 * @param name        name of the meal
	 * @param description description of the meal
	 * @param price       the meal's price
	 * @param ingredients the meal's ingredients
	 * @param recipe      recipe instructions for the cook
	 * @param type        the meal's menu-line {@link Meal.FoodType}
	 */
	public Meal(String name, String description, Money price,
				Map<Ingredient, Quantity> ingredients, String recipe, FoodType type) {
		super(name, price);
		this.description = description;
		this.recipe = recipe;
		this.ingredients = ingredients;
		this.foodType = type;
		this.addCategory(foodType.toString());
	}

	public Meal() {
	}

	public Map<Ingredient, Quantity> getIngredients() {
		return ingredients;
	}

	public void setIngredients(Map<Ingredient, Quantity> ingredients) {
		this.ingredients = ingredients;
	}

	public FoodType getFoodType() {
		return this.foodType;
	}

	public String getRecipe() {
		return recipe;
	}

	public void setRecipe(String recipe) {
		this.recipe = recipe;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param o Object to compare the Meal instance to
	 * @return whether the Meals have the same name as boolean
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Meal) {
			Meal m = (Meal) o;
			return super.getName().equals(m.getName());
		}
		return false;
	}

	/**
	 * @return converts {@link FoodType} to prettified String translated into German
	 */
	public String getPrettyType() {
		switch (this.foodType) {
			case DIET:
				return "Diät";
			case SPECIAL:
				return "Spezial";
			default:
				return "Normal";
		}
	}

}
