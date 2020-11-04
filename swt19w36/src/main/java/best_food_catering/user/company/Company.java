package best_food_catering.user.company;

import best_food_catering.user.User;
import best_food_catering.user.UserStatus;
import org.salespointframework.useraccount.UserAccount;

import javax.persistence.Entity;

/**
 * @author David Schneider
 */
@Entity
public class Company extends User {

	// INFO: Company stands also for kindergartens and schools

	private String companyName;
	private CompanyType type;

	// == constructors ==

	/**
	 * @param userAccount userAccount for login etc
	 * @param email       email
	 * @param companyName company name
	 * @param type        school/kindergarten/company
	 */
	public Company(UserAccount userAccount, String email, String companyName, CompanyType type) {
		super(userAccount, email, UserStatus.ACTIVE);
		this.companyName = companyName;
		this.type = type;
		super.setActive();
	}

	public Company() {
	}

	// == public methods ==

	// getters
	public String getCompanyName() {
		return companyName;
	}

	public CompanyType getType() {
		return type;
	}

	@Override
	public UserAccount getUserAccount() {
		return super.getUserAccount();
	}

	// setters

	/**
	 * @param companyName the name of the company
	 *                    updates the company name
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * @param email the email of the company
	 *              updates the email of this company
	 */
	@Override
	public void setEmail(String email) {
		super.setEmail(email);
	}


	/**
	 * @return the name of the company and its type (school/kindergarten/company)
	 */
	@Override
	public String toString() {
		return "[Company " + this.companyName + " type=" + this.type;
	}
}
