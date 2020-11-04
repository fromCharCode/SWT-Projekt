package best_food_catering.storage;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author Justin Bürger
 * This class is the basic form to create new ingredients.
 */
public class IngredientForm {

	@NotEmpty(message = "Forename is empty")
	private final String name;

	@NotNull
	@Min(value = 0, message = "JOA MEI! Sehen moa oas wie a woifoart! Joa KRUZI!")
	@Max(value = 10000, message = "money is finite")
	private final Double price;

	@NotEmpty(message = "metric is empty")
	private final String metric;

	@Min(value = 0, message = "quantity must be bigger than or equal to 0")
	@Max(value = 10000, message = "our storage is limited")
	private final Double minAmount;

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private final LocalDate bestBefore;

	@Min(value = 0, message = "quantity must be bigger than or equal to 0")
	@Max(value = 10000, message = "our storage is limited")
	private final Float quantity;

	/**
	 * @param name       name
	 * @param price      price per metric
	 * @param metric     metric
	 * @param minAmount  minimal Amount
	 * @param bestBefore best before date
	 * @param quantity   quantity of the ingredient measured in the metric
	 */
	public IngredientForm(String name, Double price,
						  String metric, Double minAmount,
						  LocalDate bestBefore, Float quantity) {
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

	public Double getPrice() {
		return price;
	}

	public String getMetric() {
		return metric;
	}

	public Double getMinAmount() {
		return minAmount;
	}

	public LocalDate getBestBefore() {
		return bestBefore;
	}

	public Float getQuantity() {
		return quantity;
	}
}
