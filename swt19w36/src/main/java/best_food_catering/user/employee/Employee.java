package best_food_catering.user.employee;

import best_food_catering.user.User;
import best_food_catering.user.UserStatus;
import org.salespointframework.useraccount.UserAccount;

import javax.persistence.Entity;

/**
 * @author David Schneider
 */
@Entity
public class Employee extends User {

	// Employee stands also for the parents ordering food for their children

	private String forename;
	private String surname;
	private String role;

	// == constructors ==

	/**
	 * @param userAccount employees UserAccount containing login data
	 * @param email       employees email address
	 * @param forename    employees first name
	 * @param surname     employees last name
	 * @param role        KITCHEN/STORAGE/...
	 */
	public Employee(UserAccount userAccount, String email, String forename, String surname, String role) {
		super(userAccount, email, UserStatus.ACTIVE);
		this.forename = forename;
		this.surname = surname;
		this.role = role;
	}

	public Employee() {

	}


	// == public methods ==

	// getters
	public String getForename() {
		return forename;
	}

	public String getSurname() {
		return surname;
	}

	public String getRole() {
		return role;
	}

	// setters

	/**
	 * @param forename updates employees forename
	 */
	public void setForename(String forename) {
		this.forename = forename;
	}

	/**
	 * @param surname updates employees second name
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}

	/**
	 * @param role gives employee its role
	 */
	public void setRole(String role) {
		this.surname = role;
	}

	@Override
	public String getEmail() {
		return super.getEmail();
	}
}