package best_food_catering.kitchen;

import best_food_catering.storage.Ingredient;
import org.salespointframework.quantity.Quantity;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jonas Bohmann
 */

@Entity
public class KitchenReport {

	private @Id
	@GeneratedValue
	long id;

	@ElementCollection
	private Map<Ingredient, Quantity> usedIngredients;

	@NotNull
	private int calenderWeek;
	@NotNull
	private int year;

	private int date;

	/***
	 * @param week the calender week for the report
	 * @param year the report's year
	 */
	public KitchenReport(int week, int year) {
		this.calenderWeek = week;
		this.year = year;
		this.usedIngredients = new HashMap<>();
		this.setDate();
	}

	public KitchenReport() {
	}

	public long getId() {
		return id;
	}

	public int getCalenderWeek() {
		return calenderWeek;
	}

	public int getYear() {
		return year;
	}

	/***
	 * Converts week and year input from the HTML to the internal date format by which Menu's and Report's
	 * 		are identified by.
	 *
	 * Example:
	 * 	In: week = 19, year = 2019
	 * 	Out: 201919
	 *
	 * 	In: week = 3, year = 2020
	 * 	Out: 202003
	 *
	 */
	private void setDate() {
		this.date = Integer.parseInt(this.year + String.format("%02d", this.calenderWeek));

	}

	public int getDate() {
		return this.date;
	}

	public Map<Ingredient, Quantity> getUsedIngredients() {
		return usedIngredients;
	}

	public void setUsedIngredients(Map<Ingredient, Quantity> usedIngredients) {
		this.usedIngredients = usedIngredients;
	}
}
