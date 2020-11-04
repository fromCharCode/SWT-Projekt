package best_food_catering.kitchen;

import org.salespointframework.catalog.ProductIdentifier;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Year;

/**
 * @author Jonas Bohmann
 */

public class AddMenuForm {

	@NotNull
	@NotEmpty
	private final String datePickWeek;

	private final String week;
	private final String year;

	private @NotNull ProductIdentifier normalMonday;
	private @NotNull ProductIdentifier normalTuesday;
	private @NotNull ProductIdentifier normalWednesday;
	private @NotNull ProductIdentifier normalThursday;
	private @NotNull ProductIdentifier normalFriday;

	private @NotNull ProductIdentifier specialMonday;
	private @NotNull ProductIdentifier specialTuesday;
	private @NotNull ProductIdentifier specialWednesday;
	private @NotNull ProductIdentifier specialThursday;
	private @NotNull ProductIdentifier specialFriday;

	private @NotNull ProductIdentifier dietMonday;
	private @NotNull ProductIdentifier dietTuesday;
	private @NotNull ProductIdentifier dietWednesday;
	private @NotNull ProductIdentifier dietThursday;
	private @NotNull ProductIdentifier dietFriday;


	/**
	 * Form for creating new Menu objects.
	 *
	 * @param datePickWeek the menu's date from the HTML input (i.e. 2020-W03)
	 */
	public AddMenuForm(String datePickWeek) {
		this.datePickWeek = datePickWeek;

		if (this.datePickWeek != null) {

			// Chrome, Opera and Edge
			if(datePickWeek.contains(Character.toString('-'))){
				String[] parts = this.datePickWeek.split("-");
				this.year = parts[0];
				this.week = parts[1].substring(1); // Delete leading "W"

			// Firefox
			} else {
				this.year = Year.now().toString();
				this.week = this.datePickWeek;
			}

		} else {
			this.week = null;
			this.year = null;

		}
	}

	/***
	 * @throws IllegalArgumentException when the year is not between 2019 - 3001
	 * @return the year when this menu is active
	 */
	public String getYear() {
		if (this.year != null) {
			int int_year = Integer.parseInt(this.year);
			if (int_year < 2015 || int_year > 3000) {
				throw new IllegalArgumentException("Invalid Year!");
			}
		}
		return year;
	}

	/***
	 * @throws IllegalArgumentException when the calender week is not between 1 - 54
	 * @return the calender week for when this menu is active
	 */
	public String getWeek() {
		if (this.week != null) {
			int int_week = Integer.parseInt(this.week);
			if (int_week < 1 || int_week > 53) {
				throw new IllegalArgumentException("Invalid Kalenderwoche!");
			}
		}
		return week;
	}

	public String getDatePickWeek() {
		return datePickWeek;
	}

	public ProductIdentifier getNormalMonday() {
		return normalMonday;
	}

	public void setNormalMonday(ProductIdentifier normalMonday) {
		this.normalMonday = normalMonday;
	}

	public ProductIdentifier getNormalTuesday() {
		return normalTuesday;
	}

	public void setNormalTuesday(ProductIdentifier normalTuesday) {
		this.normalTuesday = normalTuesday;
	}

	public ProductIdentifier getNormalWednesday() {
		return normalWednesday;
	}

	public void setNormalWednesday(ProductIdentifier normalWednesday) {
		this.normalWednesday = normalWednesday;
	}

	public ProductIdentifier getNormalThursday() {
		return normalThursday;
	}

	public void setNormalThursday(ProductIdentifier normalThursday) {
		this.normalThursday = normalThursday;
	}

	public ProductIdentifier getNormalFriday() {
		return normalFriday;
	}

	public void setNormalFriday(ProductIdentifier normalFriday) {
		this.normalFriday = normalFriday;
	}

	public ProductIdentifier getSpecialMonday() {
		return specialMonday;
	}

	public void setSpecialMonday(ProductIdentifier specialMonday) {
		this.specialMonday = specialMonday;
	}

	public ProductIdentifier getSpecialTuesday() {
		return specialTuesday;
	}

	public void setSpecialTuesday(ProductIdentifier specialTuesday) {
		this.specialTuesday = specialTuesday;
	}

	public ProductIdentifier getSpecialWednesday() {
		return specialWednesday;
	}

	public void setSpecialWednesday(ProductIdentifier specialWednesday) {
		this.specialWednesday = specialWednesday;
	}

	public ProductIdentifier getSpecialThursday() {
		return specialThursday;
	}

	public void setSpecialThursday(ProductIdentifier specialThursday) {
		this.specialThursday = specialThursday;
	}

	public ProductIdentifier getSpecialFriday() {
		return specialFriday;
	}

	public void setSpecialFriday(ProductIdentifier specialFriday) {
		this.specialFriday = specialFriday;
	}

	public ProductIdentifier getDietMonday() {
		return dietMonday;
	}

	public void setDietMonday(ProductIdentifier dietMonday) {
		this.dietMonday = dietMonday;
	}

	public ProductIdentifier getDietTuesday() {
		return dietTuesday;
	}

	public void setDietTuesday(ProductIdentifier dietTuesday) {
		this.dietTuesday = dietTuesday;
	}

	public ProductIdentifier getDietWednesday() {
		return dietWednesday;
	}

	public void setDietWednesday(ProductIdentifier dietWednesday) {
		this.dietWednesday = dietWednesday;
	}

	public ProductIdentifier getDietThursday() {
		return dietThursday;
	}

	public void setDietThursday(ProductIdentifier dietThursday) {
		this.dietThursday = dietThursday;
	}

	public ProductIdentifier getDietFriday() {
		return dietFriday;
	}

	public void setDietFriday(ProductIdentifier dietFriday) {
		this.dietFriday = dietFriday;
	}


}
