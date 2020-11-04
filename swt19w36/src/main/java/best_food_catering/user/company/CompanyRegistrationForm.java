package best_food_catering.user.company;

import best_food_catering.user.UserForm;

import javax.validation.constraints.NotEmpty;

public class CompanyRegistrationForm implements UserForm {

	@NotEmpty(message = "Company name is empty")
	private final String companyName;

	@NotEmpty(message = "Password is empty")
	private final String password;

	@NotEmpty(message = "Email is empty")
	private final String email;

	@NotEmpty(message = "Empty companyType")
	private final String companyType;

	// == constructors ==

	/**
	 * @param companyName user input
	 * @param password    user input
	 * @param email       user input
	 * @param companyType user input
	 */
	public CompanyRegistrationForm(String companyName, String password, String email, String companyType) {
		this.companyName = companyName;
		this.password = password;
		this.email = email;
		this.companyType = companyType;
	}

	public String getCompanyName() {
		return companyName;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}

	public String getCompanyType() {
		return companyType;
	}

}
