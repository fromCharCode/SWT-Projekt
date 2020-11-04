package best_food_catering.user.employee;


import best_food_catering.user.UserForm;

import javax.validation.constraints.NotEmpty;

/**
 * @author Ignacio
 */
public class EmployeeSettingsForm implements UserForm {
	@NotEmpty(message = "ID is empty")
	private final String id;

	@NotEmpty(message = "Forename is empty")
	private final String forename;

	// maybe this should be optional?
	@NotEmpty(message = "Surename is empty")
	private final String surname;

	// maybe this should be optional?
	@NotEmpty(message = "Email is empty")
	private final String email;

	/**
	 * @param id       user input (yet)
	 * @param forename user input
	 * @param surname  user input
	 * @param email    user input
	 */
	public EmployeeSettingsForm(String id, String forename, String surname, String email) {
		this.id = id;
		this.forename = forename;
		this.surname = surname;
		this.email = email;
	}

	public String getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getForename() {
		return forename;
	}

	public String getSurname() {
		return surname;
	}

}
