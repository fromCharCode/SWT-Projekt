package best_food_catering.purchase;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;

public class DebitsCardForm {


	@NotEmpty(message = "Card name is empty")
	private final String cardName;

	@NotEmpty(message = "Card association is empty")
	private final String cardAssociationName;

	@NotEmpty(message = "Card number is empty")
	private final String cardNumber;

	@NotEmpty(message = "Name on card is empty")
	private final String nameOnCard;

	@NotEmpty(message = "Adress is empty")
	private final String billingAddress;

	@Past
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private final LocalDate validFrom;

	@Future
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private final LocalDate expiryDate;

	@NotEmpty(message = "Card verification code is empty")
	private final String cardVerificationCode;

	private final Integer dailyWithdrawalLimit;

	/**
	 * @param cardName             the name of the card
	 * @param cardAssociationName  the associated name on the card
	 * @param cardNumber           the number of the card
	 * @param nameOnCard           the name written on the card
	 * @param billingAddress       the billing address of the customer
	 * @param validFrom            the date from which on the card is valid
	 * @param expiryDate           the date at which the card expires
	 * @param cardVerificationCode the verification code on the card
	 * @param dailyWithdrawalLimit the amount of money the customer can withdraw from his card each day
	 */
	public DebitsCardForm(String cardName, String cardAssociationName, String cardNumber, String nameOnCard,
						  String billingAddress, LocalDate validFrom, LocalDate expiryDate,
						  String cardVerificationCode, Integer dailyWithdrawalLimit) {
		this.cardName = cardName;
		this.cardAssociationName = cardAssociationName;
		this.cardNumber = cardNumber;
		this.nameOnCard = nameOnCard;
		this.billingAddress = billingAddress;
		this.validFrom = validFrom;
		this.expiryDate = expiryDate;
		this.cardVerificationCode = cardVerificationCode;
		this.dailyWithdrawalLimit = 100000;
	}

	public String getCardName() {
		return cardName;
	}

	public String getCardAssociationName() {
		return cardAssociationName;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public String getNameOnCard() {
		return nameOnCard;
	}

	public String getBillingAddress() {
		return billingAddress;
	}

	public LocalDate getValidFrom() {
		return validFrom;
	}

	public LocalDate getExpiryDate() {
		return expiryDate;
	}

	public String getCardVerificationCode() {
		return cardVerificationCode;
	}

	public Integer getDailyWithdrawalLimit() {
		return dailyWithdrawalLimit;
	}
}
