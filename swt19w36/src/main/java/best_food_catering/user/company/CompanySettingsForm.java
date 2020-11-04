package best_food_catering.user.company;

import best_food_catering.user.UserForm;

import javax.validation.constraints.Email;

/**
 * @author David Schneider
 */
public class CompanySettingsForm implements UserForm {

	private String companyName;
	private @Email String email;

	// == constructors ==

	/**
	 * @param companyName user input
	 * @param email       user input
	 */
	public CompanySettingsForm(String companyName, String email) {
		this.companyName = companyName;
		this.email = email;
	}

	public String getCompanyName() {
		return companyName;
	}

	public String getEmail() {
		return email;
	}

}
