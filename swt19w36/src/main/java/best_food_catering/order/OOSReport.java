package best_food_catering.order;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dominik and Jonas Bohmann
 */

@Entity
public class OOSReport {

	private @Id
	@GeneratedValue
	long id;

	@ElementCollection
	@Cascade(CascadeType.ALL)
	private Map<OOSReportEntry, Integer> mealsMonday;

	@ElementCollection
	@Cascade(CascadeType.ALL)
	private Map<OOSReportEntry, Integer> mealsTuesday;

	@ElementCollection
	@Cascade(CascadeType.ALL)
	private Map<OOSReportEntry, Integer> mealsWednesday;

	@ElementCollection
	@Cascade(CascadeType.ALL)
	private Map<OOSReportEntry, Integer> mealsThursday;

	@ElementCollection
	@Cascade(CascadeType.ALL)
	private Map<OOSReportEntry, Integer> mealsFriday;

	@NotNull
	private int calenderWeek;

	@NotNull
	private int year;

	private int date;

	/**
	 * @param week the calendar week the oosReport is made for
	 * @param year the current year
	 */
	public OOSReport(int week, int year) {
		this.mealsMonday = new HashMap<>();
		this.mealsTuesday = new HashMap<>();
		this.mealsWednesday = new HashMap<>();
		this.mealsThursday = new HashMap<>();
		this.mealsFriday = new HashMap<>();

		this.calenderWeek = week;
		this.year = year;
		this.setDate();
	}

	public OOSReport() {
	}

	/**
	 * @param day day of the week
	 * @return gets the meals for the weekday
	 */
	public Map<OOSReportEntry, Integer> getMealsPerDay(DayOfWeek day) {
		Map<OOSReportEntry, Integer> mealsPerDay = new HashMap<>();
		switch (day) {
			case MONDAY:
				mealsPerDay = this.mealsMonday;
				break;
			case TUESDAY:
				mealsPerDay = this.mealsTuesday;
				break;
			case WEDNESDAY:
				mealsPerDay = this.mealsWednesday;
				break;
			case THURSDAY:
				mealsPerDay = this.mealsThursday;
				break;
			default:
				mealsPerDay = this.mealsFriday;
				break;
		}
		return mealsPerDay;
	}

	// getter and setter

	/**
	 * sets the date of the OOSReport in the format YYYYWW
	 */
	private void setDate() {
		this.date = Integer.parseInt(this.year + String.format("%02d", this.calenderWeek));
	}

	public long getId() {
		return id;
	}

	public Map<OOSReportEntry, Integer> getMealsMonday() {
		return mealsMonday;
	}

	public Map<OOSReportEntry, Integer> getMealsTuesday() {
		return mealsTuesday;
	}

	public Map<OOSReportEntry, Integer> getMealsWednesday() {
		return mealsWednesday;
	}

	public Map<OOSReportEntry, Integer> getMealsThursday() {
		return mealsThursday;
	}

	public Map<OOSReportEntry, Integer> getMealsFriday() {
		return mealsFriday;
	}

	public void setMealsMonday(Map<OOSReportEntry, Integer> mealsMonday) {
		this.mealsMonday = mealsMonday;
	}

	public void setMealsTuesday(Map<OOSReportEntry, Integer> mealsTuesday) {
		this.mealsTuesday = mealsTuesday;
	}

	public void setMealsWednesday(Map<OOSReportEntry, Integer> mealsWednesday) {
		this.mealsWednesday = mealsWednesday;
	}

	public void setMealsThursday(Map<OOSReportEntry, Integer> mealsThursday) {
		this.mealsThursday = mealsThursday;
	}

	public void setMealsFriday(Map<OOSReportEntry, Integer> mealsFriday) {
		this.mealsFriday = mealsFriday;
	}

	public int getCalenderWeek() {
		return calenderWeek;
	}

	public int getYear() {
		return year;
	}

	public int getDate() {
		return this.date;
	}
}
