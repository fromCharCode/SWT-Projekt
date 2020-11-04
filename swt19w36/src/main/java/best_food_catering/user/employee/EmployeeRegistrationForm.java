package best_food_catering.user.employee;

import best_food_catering.user.UserForm;

import javax.validation.constraints.NotEmpty;

/**
 * @author David Schneider
 */
public class EmployeeRegistrationForm implements UserForm {

	@NotEmpty(message = "Forename is empty")
	private final String forename;

	// maybe this should be optional?
	@NotEmpty(message = "Surename is empty")
	private final String surname;

	@NotEmpty(message = "Password is empty")
	private final String password;

	@NotEmpty(message = "Email is empty")
	private final String email;

	@NotEmpty(message = "Role is empty")
	private final String role;

	/**
	 * @param forename user input
	 * @param surname  user input
	 * @param password user input
	 * @param email    user input
	 * @param role     user input
	 */
	public EmployeeRegistrationForm(String forename, String surname, String password, String email, String role) {
		this.forename = forename;
		this.surname = surname;
		this.password = password;
		this.email = email;
		this.role = role;
	}

	public String getForename() {
		return forename;
	}

	public String getSurname() {
		return surname;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}

	public String getRole() {
		return role;
	}
}