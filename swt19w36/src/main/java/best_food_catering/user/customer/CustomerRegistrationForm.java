package best_food_catering.user.customer;

import best_food_catering.user.UserForm;

import javax.validation.constraints.NotEmpty;

/**
 * @author David Schneider
 */
public class CustomerRegistrationForm implements UserForm {

	@NotEmpty(message = "Forename is empty")
	private final String forename;

	// maybe this should be optional?
	@NotEmpty(message = "Surename is empty")
	private final String surname;

	@NotEmpty(message = "Password is empty")
	private final String password;

	@NotEmpty(message = "Email is empty")
	private final String email;

	@NotEmpty(message = "Something unexpected happened. The Company you have chosen seem not to exist")
	private final String associatedCompany;

	/**
	 * @param forename          user input
	 * @param surname           user input
	 * @param password          user input
	 * @param email             user input
	 * @param associatedCompany user input
	 */
	public CustomerRegistrationForm(String forename, String surname, String password,
									String email, String associatedCompany) {
		this.forename = forename;
		this.surname = surname;
		this.password = password;
		this.email = email;
		this.associatedCompany = associatedCompany;
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

	public String getAssociatedCompany() {
		return associatedCompany;
	}

}
