package best_food_catering.user.customer;

import best_food_catering.user.User;
import best_food_catering.user.UserStatus;
import org.salespointframework.useraccount.UserAccount;

import javax.persistence.Entity;
import java.time.LocalDate;

/**
 * @author David Schneider
 */
@Entity
public class Customer extends User {

	// Employee stands also for the parents ordering food for their children

	private String forename;
	private String surname;
	private String associatedCompanyName;
	private LocalDate expireDate;

	// == constructors ==

	/**
	 * @param userAccount           customers login data, etc
	 * @param email                 customers email
	 * @param forename              customers first name
	 * @param surname               customers last name
	 * @param associatedCompanyName customers associated company
	 * @param expireDate            settles the date on which the user will be locked (per contract)
	 */
	public Customer(UserAccount userAccount, String email, String forename, String surname,
					String associatedCompanyName, LocalDate expireDate) {
		super(userAccount, email, UserStatus.WAITING);
		this.forename = forename;
		this.surname = surname;
		this.associatedCompanyName = associatedCompanyName;
		this.expireDate = expireDate;
		super.setWaiting();
	}

	public Customer() {
	}


	// == public methods ==

	// getters
	public String getForename() {
		return forename;
	}

	public String getSurname() {
		return surname;
	}

	public String getAssociatedCompanyName() {
		return associatedCompanyName;
	}

	public LocalDate getExpireDate() {
		return expireDate;
	}

	/**
	 * @return true if he has to be expired and now is locked
	 * false if the customer still has time, won't be locked
	 */
	public boolean expire() {
		if (getExpireDate().isBefore(LocalDate.now())) {
			setLocked();
			return true;
		}
		return false;
	}

	// setters

	/**
	 * @param forename updates the customers first name
	 */
	public void setForename(String forename) {
		getUserAccount().setFirstname(forename);
		this.forename = forename;
	}

	/**
	 * @param surname updates the customers second name
	 */
	public void setSurname(String surname) {
		this.getUserAccount().setLastname(surname);
		this.surname = surname;
	}

	/**
	 * @param email updates the customers email
	 */
	@Override
	public void setEmail(String email) {
		super.setEmail(email);
	}

	/**
	 * @param expireDate the date that will be used
	 *                   for checking if a user still has a contract
	 */
	public void setExpireDate(LocalDate expireDate) {
		this.expireDate = expireDate;
	}

	/**
	 * @return returns customers name and its associated company name
	 */
	@Override
	public String toString() {
		return "[Customer " + this.forename + " " + this.surname + " company=" + this.associatedCompanyName;
	}
}
