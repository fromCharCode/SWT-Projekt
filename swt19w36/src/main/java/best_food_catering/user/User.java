package best_food_catering.user;

import com.mysema.commons.lang.Assert;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * @author David Schneider
 */
@Entity
public class User {

	@OneToOne
	private UserAccount userAccount;

	private UserStatus status;
	private @Id
	@GeneratedValue
	long id;

	/**
	 * @param userAccount stores login data for user
	 * @param email       email address
	 * @param status      waiting/active/locked
	 */
	// == constructors ==
	public User(UserAccount userAccount, String email, UserStatus status) {
		Assert.notNull(userAccount, "UserAccount must not be empty");
		this.userAccount = userAccount;
		this.status = status;
	}

	public User() {

	}

	// == public methods ==

	// getters
	public long getId() {
		return id;
	}

	public String getEmail() {
		return userAccount.getEmail();
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	public UserStatus getStatus() {
		return status;
	}

	public boolean isLocked() {
		return status == UserStatus.LOCKED;
	}

	public boolean isWaiting() {
		return status == UserStatus.WAITING;
	}

	public boolean isOpen(){
		return !isWaiting() && !isLocked();
	}

	public boolean isCustomer() {
		return userAccount.hasRole(Role.of(UserRole.CUSTOMER.toString()));
	}

	public boolean isCompany() {
		return userAccount.hasRole(Role.of(UserRole.COMPANY.toString()));
	}

	public boolean isEmployee() {
		return !isCompany() && !isCustomer();
	}

	// setters

	/**
	 * @param email updates the users email
	 */
	public void setEmail(String email) {
		userAccount.setEmail(email);
	}

	/**
	 * locks the user account
	 */
	public void setLocked() {
		this.status = UserStatus.LOCKED;
		lockAccount();
	}

	/**
	 * activates the user account
	 */
	public void setActive() {
		this.status = UserStatus.ACTIVE;
		enableAccount();
	}

	/**
	 * locks the user account set to waiting (for new customers)
	 */
	public void setWaiting() {
		this.status = UserStatus.WAITING;
		lockAccount();
	}


	// == private methods ==
	private void lockAccount() {
		userAccount.setEnabled(false);
	}

	private void enableAccount() {
		userAccount.setEnabled(true);
	}
}
