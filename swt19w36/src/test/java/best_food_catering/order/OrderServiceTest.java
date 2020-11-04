package best_food_catering.order;

import best_food_catering.kitchen.MenuRepository;
import best_food_catering.user.company.CompanyType;
import org.junit.jupiter.api.Test;
import org.salespointframework.order.OrderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

// assertTrue import

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class OrderServiceTest {

	@Autowired
	private  OrderService orderService;

	@Autowired
	private MealOrderRepository orderRepo;

	@Autowired
	private MenuRepository menuRepository;

	@Autowired
	private OrderManager<MealOrder> orderManager;

	@Autowired
	private ReportRepository reportRepository;

	@Test
	void createOrderTest(){
		MealOrderRepository mealOrderRepository = mock(MealOrderRepository.class);
		when(mealOrderRepository.save(any())).then(i -> i.getArgument(0));

	}

	@Test
	public void testGetAllOrders(){
		List<MealOrder> orders = orderService.getAllOrders();

		assertEquals(orderRepo.count(), (long) orders.size());
		for(MealOrder m: orders){
			assertTrue(orderRepo.existsById(Long.valueOf(m.getId().getIdentifier())));
		}
		assertEquals(orderRepo.count(), (long) orders.size());
		HashSet<MealOrder> allOrders = new HashSet<>();
		for(MealOrder m: orderRepo.findAll()){
			allOrders.add(m);
		}
		for(MealOrder m: orders) {
			assertTrue(allOrders.contains(m));
		}
	}

	@Test
	public void testGetCompanyTypeFromUserName(){
		assertEquals(CompanyType.KINDERGARTEN, orderService.getCompanyTypeFromUserName("Max Mueller"));
		assertNull(orderService.getCompanyTypeFromUserName("boss"));

	}
}