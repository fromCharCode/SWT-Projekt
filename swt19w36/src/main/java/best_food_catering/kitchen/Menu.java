package best_food_catering.kitchen;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.*;

/**
 * @author Jonas Bohmann
 */

@Entity
public class Menu {

	@NotNull
	@Id
	private int date;

	@NotNull
	private int calenderWeek;

	@NotNull
	private int year;

	@ManyToMany
	private Map<DayOfWeek, Meal> normalMeals;

	@ManyToMany
	private Map<DayOfWeek, Meal> specialMeals;

	@ManyToMany
	private Map<DayOfWeek, Meal> dietMeals;

	/**
	 * Represents a weekly Menu.
	 *
	 * @param calenderWeek calender week for when the menu is active
	 * @param year         year for when the menu is active
	 */
	public Menu(int calenderWeek, int year) {
		this.normalMeals = new HashMap<>();
		this.dietMeals = new HashMap<>();
		this.specialMeals = new HashMap<>();
		this.calenderWeek = calenderWeek;
		this.year = year;
		this.setDate();
	}

	public Menu() {
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
		/*
		 * This will set the date by which the Menu will be identified.
		 *
		 * Concatenates the year and calenderWeek and parses that as int.
		 *
		 * Example: Year: 2019, Calenderweek: 3
		 * Result:  201903
		 *
		 * */
		this.date = Integer.parseInt(this.year + String.format("%02d", this.calenderWeek));

	}

	public int getCalenderWeek() {
		return calenderWeek;
	}

	public int getDate() {
		return this.date;
	}

	public int getYear() {
		return this.year;
	}

	public void setCalenderWeek(int calenderWeek) {
		this.calenderWeek = calenderWeek;
	}

	/**
	 * Adds a Meal and its specified weekday to this Menu.
	 *
	 * @param meal the meal
	 * @param day  the weekday for when the meal can be bought
	 */
	public void addMeal(Meal meal, DayOfWeek day) {
		if (meal.getFoodType() == Meal.FoodType.NORMAL) {
			normalMeals.put(day, meal);
		} else if (meal.getFoodType() == Meal.FoodType.DIET) {
			dietMeals.put(day, meal);
		} else if (meal.getFoodType() == Meal.FoodType.SPECIAL) {
			specialMeals.put(day, meal);
		}
	}

	/**
	 * @param day the weekday
	 * @return all meals that can be bought on the specified weekday
	 */
	public List<Meal> getMealsPerWeekday(DayOfWeek day) {

		List<Meal> toReturn = new ArrayList<>();

		for (Map.Entry<DayOfWeek, Meal> entry : normalMeals.entrySet()) {
			if (entry.getKey() == day) {
				toReturn.add(entry.getValue());
			}
		}

		for (Map.Entry<DayOfWeek, Meal> entry : dietMeals.entrySet()) {
			if (entry.getKey() == day) {
				toReturn.add(entry.getValue());
			}
		}

		for (Map.Entry<DayOfWeek, Meal> entry : specialMeals.entrySet()) {
			if (entry.getKey() == day) {
				toReturn.add(entry.getValue());
			}
		}

		return toReturn;

	}

	/**
	 * @param type the type of meal
	 * @return all meals in this menu with the specified type
	 */
	public Map<DayOfWeek, Meal> getMealsPerType(Meal.FoodType type) {

		switch (type) {
			case DIET:
				return dietMeals;
			case SPECIAL:
				return specialMeals;
			case NORMAL:
			default:
				return normalMeals;
		}

	}

	/**
	 * @param meal the meal to get the weekday from
	 * @return the prettified week day as String translated into German
	 */
	public String getWeekdayFromMeal(Meal meal) {

		// Only have 1 return statement for SonarQube
		String weekdayToReturn = "";

		switch (meal.getFoodType()) {
			case NORMAL:
				for (Map.Entry<DayOfWeek, Meal> entry : normalMeals.entrySet()) {
					if (entry.getValue().equals(meal)) {
						weekdayToReturn = entry.getKey().getDisplayName(TextStyle.FULL, Locale.GERMAN);
					}
				}

			case DIET:
				for (Map.Entry<DayOfWeek, Meal> entry : dietMeals.entrySet()) {
					if (entry.getValue().equals(meal)) {
						weekdayToReturn = entry.getKey().getDisplayName(TextStyle.FULL, Locale.GERMAN);

					}
				}
			case SPECIAL:
				for (Map.Entry<DayOfWeek, Meal> entry : specialMeals.entrySet()) {
					if (entry.getValue().equals(meal)) {
						weekdayToReturn = entry.getKey().getDisplayName(TextStyle.FULL, Locale.GERMAN);

					}
				}
		}

		return weekdayToReturn;
	}

	public Map<DayOfWeek, Meal> getNormalMeals() {
		return normalMeals;
	}

	public void setNormalMeals(Map<DayOfWeek, Meal> normalMeals) {
		this.normalMeals = normalMeals;
	}

	public Map<DayOfWeek, Meal> getSpecialMeals() {
		return specialMeals;
	}

	public void setSpecialMeals(Map<DayOfWeek, Meal> specialMeals) {
		this.specialMeals = specialMeals;
	}

	public Map<DayOfWeek, Meal> getDietMeals() {
		return dietMeals;
	}

	public void setDietMeals(Map<DayOfWeek, Meal> dietMeals) {
		this.dietMeals = dietMeals;
	}

	/**
	 * @return the Menu's Meals with {@link best_food_catering.kitchen.Meal.FoodType} Normal sorted by week day
	 */
	public List<Meal> getSortedNormalMeals() {
		List<Meal> sorted = new ArrayList<>();
		this.normalMeals.entrySet().stream().sorted(Map.Entry.comparingByKey())
				.forEachOrdered(mealIntegerEntry -> sorted.add(mealIntegerEntry.getValue()));
		return sorted;
	}

	/**
	 * @return the Menu's Meals with {@link best_food_catering.kitchen.Meal.FoodType} Diet sorted by week day
	 */
	public List<Meal> getSortedDietMeals() {
		List<best_food_catering.kitchen.Meal> sorted = new ArrayList<>();
		this.dietMeals.entrySet().stream().sorted(Map.Entry.comparingByKey())
				.forEachOrdered(mealIntegerEntry -> sorted.add(mealIntegerEntry.getValue()));
		return sorted;
	}

	/**
	 * @return the Menu's Meals with {@link best_food_catering.kitchen.Meal.FoodType} Diet sorted by week day
	 */
	public List<Meal> getSortedSpecialMeals() {
		List<best_food_catering.kitchen.Meal> sorted = new ArrayList<>();
		this.specialMeals.entrySet().stream().sorted(Map.Entry.comparingByKey())
				.forEachOrdered(mealIntegerEntry -> sorted.add(mealIntegerEntry.getValue()));
		return sorted;
	}

	/**
	 * @return all Meals in this Menu
	 */
	public List<Map<DayOfWeek, Meal>> getMeals() {
		// Backwards Compatibility for OOS templates
		List<Map<DayOfWeek, Meal>> allMeals = new ArrayList<>();
		allMeals.add(this.normalMeals);
		allMeals.add(this.specialMeals);
		allMeals.add(this.dietMeals);
		return allMeals;
	}

}



