package best_food_catering.storage;

import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.springframework.format.annotation.DateTimeFormat;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

/**
 * @author Justin Bürger
 * This class is the basic ingredient class.
 */
@Entity
public class Ingredient {

	private String name;

	private MonetaryAmount price;
	private Metric metric;
	private Double minAmount;
	private Quantity quantity;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate bestBefore;

	private @Id
	@GeneratedValue
	long id;


	public Ingredient() {
	}

	/**
	 * @param name       name of the ingredient
	 * @param price      price of the ingredient
	 * @param metric     metric of the ingredient
	 * @param minAmount  minimal amount which needs to be stored in the inventory
	 * @param bestBefore the best before date
	 * @param quantity   the amount of the ingredient actually stored (according to the metric)
	 */

	public Ingredient(String name, MonetaryAmount price,
					  Metric metric, Double minAmount,
					  LocalDate bestBefore, Quantity quantity) {
		this.name = name;
		this.price = price;
		this.metric = metric;
		this.minAmount = minAmount;
		this.bestBefore = bestBefore;
		this.quantity = quantity;
	}

	public String getName() {
		return name;
	}

	public MonetaryAmount getPrice() {
		return price;
	}

	public Metric getMetric() {
		return metric;
	}

	public Double getMinAmount() {
		return minAmount;
	}

	public LocalDate getBestBefore() {
		return bestBefore;
	}

	public Quantity getQuantity() {
		return quantity;
	}

	public long getId() {
		return id;
	}

	public void addQuantity(Quantity quan) {
		this.quantity.add(quan);
	}
}
