package best_food_catering.order;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.salespointframework.payment.DebitCard;
import org.salespointframework.payment.PaymentMethod;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class MealOrderTest {

	@Autowired
	private OrderService o;

	@Autowired
	private MealOrderRepository mealOrderRepository;

	@Autowired
	private UserAccountManager userAccountManager;

	private UserAccount userAccount;
	private MealOrder m;
	private LocalDate begin;
	private LocalDate finish;
	private PaymentMethod debitCard;
	private LocalDate closedDate;

	@BeforeEach
	public void init(){
		userAccount = userAccountManager.findByUsername("boss").orElse(null);
		debitCard = new DebitCard("Debitcard", "MaestroCard", "123637", "Max Mustermann",
			"Musterstraße 3", LocalDateTime.now(), LocalDateTime.now().plusDays(14), "3356", Money.of(10, "EUR"));
		begin = LocalDate.now();
		finish = LocalDate.now().plusDays(21);
		closedDate = LocalDate.now().plusDays(10);

		m = new MealOrder(userAccount, debitCard, begin, finish);
	}

	@Test
	public void closeTest(){
		assertTrue(m.getOpened(), "order closed");
		m.close(LocalDate.now().plusDays(5));
		Assertions.assertFalse(m.getOpened(), "order not closed");
		assertTrue(m.getClosedDate().equals(LocalDate.now().plusDays(5)), "closed dates not matching");
	}

	@Test
	public void MealOrderGetterTest(){

		// test getAttachedCustomerName
		assertTrue(m.getAttachedCustomerName().equals(userAccount.getUsername()), "Different name");

		//test getPaymentMethod
		assertTrue(m.getPaymentMethod().equals(debitCard), "Different payment method");

		//test getBegin, getFinish
		assertTrue(m.getBegin().equals(begin), "begin date not matching");
		assertTrue(m.getFinish().equals(finish), "finish date not matching");

		//test getOpened
		assertTrue(m.getOpened(), "Order not open");

		//test getClosedDate
		m.close(closedDate);
		assertTrue(m.getClosedDate().equals(closedDate), "closed date not matching");
	}

	@Test
	public void testCreateOrder(){
		assertTrue(mealOrderRepository.count() == 0, "false");
		o.createOrder(m);
		assertTrue(mealOrderRepository.count() == 1, "false");
	}

}
