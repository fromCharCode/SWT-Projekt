package best_food_catering.order;
import static org.junit.jupiter.api.Assertions.assertEquals;
import best_food_catering.kitchen.Meal;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.annotation.SecurityTestExecutionListeners;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.Map;

@SpringBootTest
@AutoConfigureMockMvc
public class OOSReportEntryTest {
	OOSReportEntry tester = new OOSReportEntry();
	Meal testerMeal = new Meal();

	@Test
	void testConstructorAndGetMeal(){

		assertEquals(null, tester.getMeal(), "should be null");
	}
	@Test
	void testGetSize(){
		assertEquals(null, tester.getSize(), "should be null");
	}
//


}
