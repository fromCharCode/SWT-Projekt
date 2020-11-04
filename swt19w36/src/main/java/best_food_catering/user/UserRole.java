package best_food_catering.user;

/**
 * @author David Schenider
 */
public enum UserRole {
	ADMIN("Admin"),
	BOSS("Boss"),
	COMPANY("Company"),
	CUSTOMER("Customer"),
	KITCHEN("Kitchen"),
	STORAGE("Storage"),
	ACCOUNTING("Accounting");

	private final String displayValue;

	/**
	 * @param displayValue the string to display the current role
	 */
	UserRole(String displayValue) {
		this.displayValue = displayValue;
	}

	/**
	 * @return returns the String value of each role
	 */
	public String getDisplayValue() {
		return displayValue;
	}

}
