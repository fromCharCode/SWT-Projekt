package best_food_catering.kitchen;

import org.salespointframework.quantity.Quantity;

/**
 * @author Jonas Bohmann
 */

public class AddMealRow {


	private long ingredientID;
	private long quantityAmount;

	/**
	 * Represents the rows for adding {@link best_food_catering.storage.Ingredient}
	 * Ingredients to new {@link Meal} Meals
	 */
	public AddMealRow() {
		super();
	}

	@Override
	public String toString() {
		return "Row [ingredient=" + this.ingredientID + ", quantity=" + this.quantityAmount + "]";
	}

	public long getIngredientID() {
		return ingredientID;
	}

	public void setIngredientID(long ingredient) {
		this.ingredientID = ingredient;
	}

	/**
	 * @return the quantity as {@link Quantity}
	 * @throws IllegalArgumentException if the amount is negative
	 */
	public long getQuantityAmount() {
		if (quantityAmount < 0) {
			throw new IllegalArgumentException("Negative quantity amount!");
		}

		return quantityAmount;
	}

	public void setQuantityAmount(long quantity) {
		this.quantityAmount = quantity;
	}
}
