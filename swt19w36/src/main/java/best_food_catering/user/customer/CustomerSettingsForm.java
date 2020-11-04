package best_food_catering.user.customer;

import best_food_catering.user.UserForm;

import javax.validation.constraints.Email;

/**
 * @author David Schneider
 */
public class CustomerSettingsForm implements UserForm {

	private String forename;
	private String surname;
	private @Email String email;

	/**
	 * @param forename user input
	 * @param surname  user input
	 * @param email    user input
	 */
	public CustomerSettingsForm(String forename, String surname, String email) {
		this.forename = forename;
		this.surname = surname;
		this.email = email;
	}

	public String getForename() {
		return forename;
	}

	public String getSurname() {
		return surname;
	}

	public String getEmail() {
		return email;
	}
}
