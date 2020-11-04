package best_food_catering.order;

import org.salespointframework.order.Order;
import org.salespointframework.payment.PaymentMethod;
import org.salespointframework.useraccount.UserAccount;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * a meal order
 */
@Entity
public class MealOrder extends Order {

	// == variables ==
	@Column(length = 10000)
	private HashMap<String, ArrayList<String>> attachedOrders = new HashMap<>();
	private String attachedCustomerName;
	private LocalDate begin;
	private LocalDate finish;
	private LocalDate closedDate;
	private int date;
	private boolean opened;
	private boolean isGroupOrder;

	// == constructors ==
	public MealOrder() {
	}

	/**
	 * @param userAccount   the user account
	 * @param paymentMethod debits card or cheque
	 * @param begin         the date the meal order is created
	 * @param finish        the date the meal order is finished
	 *                      Meal order with specified paymentMethod
	 */
	public MealOrder(UserAccount userAccount, PaymentMethod paymentMethod, LocalDate begin, LocalDate finish) {
		super(userAccount, paymentMethod);
		this.attachedCustomerName = userAccount.getUsername();
		this.begin = begin;
		this.finish = finish;
		this.opened = true;
		this.isGroupOrder = false;
		this.setDate();
	}

	/**
	 * @param userAccount the user account
	 * @param begin       the date the meal order is created
	 * @param finish      the date the meal order is finished
	 *                    Meal order without specified payment method
	 */
	public MealOrder(UserAccount userAccount, LocalDate begin, LocalDate finish) {
		super(userAccount);
		this.attachedCustomerName = userAccount.getUsername();
		this.begin = begin;
		this.finish = finish;
		this.opened = true;
	}

	/**
	 * sets the date of the meal order in the format YYYYWW
	 * Y = year of the mealOrders creation, W = corresponding calendar week
	 */
	private void setDate() {
		TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
		int calenderWeek = begin.get(woy);
		this.date = Integer.parseInt(this.begin.getYear() + String.format("%02d", calenderWeek));

	}

	/**
	 * @param closedDate the date when the order is closed
	 *                   sets the state of the order to closed
	 */
	public void close(LocalDate closedDate) {
		this.closedDate = closedDate;
		opened = false;
	}

	public void activateGroupOrder(){
		this.isGroupOrder = true;
	}

	public String getAttachedCustomerName() {
		return attachedCustomerName;
	}

	public LocalDate getBegin() {
		return begin;
	}

	public LocalDate getFinish() {
		return finish;
	}

	public LocalDate getClosedDate() {
		return closedDate;
	}

	public HashMap<String, ArrayList<String>> getAttachedOrders() {
		return attachedOrders;
	}

	public boolean getOpened() {
		return this.opened;
	}

	public int getDate() {
		return date;
	}

	public boolean getIsGroupOrder(){
		return  this.isGroupOrder;
	}

	/**
	 * @return the german string of status for the template
	 */
	public String getOrderStatusToGerman() {
		return getOpened() ? "Offen" : "Abgeschlossen";
	}
}
