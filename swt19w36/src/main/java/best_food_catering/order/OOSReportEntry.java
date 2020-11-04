package best_food_catering.order;

import best_food_catering.kitchen.Meal;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author Jonas Bohmann
 */

@Entity
public class OOSReportEntry {

	private @Id
	@GeneratedValue
	long id;

	private @OneToOne(cascade = {CascadeType.ALL})
	Meal meal;

	private @NotNull Meal.Size size;

	/**
	 * Represents an entry for a {@link OOSReport}. Holds the Meal that was ordered and its Meal.Size.
	 *
	 * @param meal the meal this entry is for
	 * @param size the meal size, automatically determined by the CompanyType the user that ordered the meal belongs to
	 */
	public OOSReportEntry(Meal meal, Meal.Size size) {
		this.meal = meal;
		this.size = size;
	}

	public OOSReportEntry() {
	}

	public Meal getMeal() {
		return meal;
	}

	public void setMeal(Meal meal) {
		this.meal = meal;
	}

	public Meal.Size getSize() {
		return size;
	}

	/**
	 * @return the size translated into German
	 */
	public String getPrettySize() {
		return this.size.equals(Meal.Size.SMALL) ? "Klein" : "Groß";
	}

	public void setSize(Meal.Size size) {
		this.size = size;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
