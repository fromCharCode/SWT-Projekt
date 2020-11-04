package best_food_catering.user.company;

/**
 * @author David Schneider
 */
public enum CompanyType {
	COMPANY("Firma"),
	KINDERGARTEN("Kindergarten"),
	SCHOOL("Schule");

	private final String displayValue;

	CompanyType(String displayValue) {
		this.displayValue = displayValue;
	}

	public String getDisplayValue() {
		return displayValue;
	}


}
