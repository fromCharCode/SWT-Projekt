package best_food_catering.user.company;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author David Schneider
 */
public class UpdateCustomerDateForm {

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private final LocalDate date;

	/**
	 * @param date user input
	 */
	// == constructors ==
	public UpdateCustomerDateForm(LocalDate date) {
		this.date = date;
	}

	public LocalDate getDate() {
		return date;
	}
}
