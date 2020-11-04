package best_food_catering.user.employee;

import javax.validation.constraints.NotEmpty;

/**
 * @author Ignacio
 */
public class EmployeeDeleteForm {

	@NotEmpty(message = "stringEmployeeId is empty")
	private String stringEmployeeId;

	/**
	 * @param stringEmployeeId user input
	 */
	public EmployeeDeleteForm(String stringEmployeeId) {
		this.stringEmployeeId = stringEmployeeId;
	}

	// Getters
	public String getId() {
		return stringEmployeeId;
	}

	// Setters
	public void setId(String stringEmployeeId) {
		this.stringEmployeeId = stringEmployeeId;
	}

}


