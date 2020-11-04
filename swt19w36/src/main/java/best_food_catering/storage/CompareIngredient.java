package best_food_catering.storage;

import java.math.BigDecimal;

/**
 * @author Justin Bürger
 * This class is created to compare ingredients with each other
 */
public class CompareIngredient {


	private Ingredient ingredient;
	//difference refers to the difference between the quantity and the minimal amount
	private BigDecimal difference;

	/**
	 * @param ingredient the ingredient
	 * @param difference the difference between minimal amounts
	 */

	public CompareIngredient(Ingredient ingredient, BigDecimal difference) {
		this.ingredient = ingredient;
		this.difference = difference;
	}


	public Ingredient getIngredient() {
		return ingredient;
	}

	public BigDecimal getDifference() {
		return difference;
	}
}
