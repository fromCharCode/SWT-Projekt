package best_food_catering.user;

/**
 * @author David Schneider
 */
public enum UserStatus {
	LOCKED("Gesperrt"),
	ACTIVE("Aktiv"),
	WAITING("Wartet (neuer Angestellter)");

	private final String displayName;

	/**
	 * @param displayName the string to display the current status
	 */
	UserStatus(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return returns the String value of each user status
	 */
	public String getDisplayName() {
		return displayName;
	}
}
