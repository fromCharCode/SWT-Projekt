package best_food_catering.user.employee;

/**
 * @author David Schneider
 */
public enum EmployeeType {
	KITCHEN("Küche"),
	ADMIN("Admin"),
	STORAGE("Lager"),
	ACCOUNTING("Buchhaltung");

	private final String displayValue;

	/**
	 * @param displayValue
	 */
	EmployeeType(String displayValue) {
		this.displayValue = displayValue;
	}

	/**
	 * @return returns the String for each type
	 */
	public String getDisplayValue() {
		return displayValue;
	}
}
