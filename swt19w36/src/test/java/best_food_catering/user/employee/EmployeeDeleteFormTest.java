package best_food_catering.user.employee;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;


import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeDeleteFormTest {
	String id = "123";
	String idReversed = "321";
	@Test
	void testEmployeeId(){
		EmployeeDeleteForm tester = new EmployeeDeleteForm(id);

		assertEquals(id, tester.getId(),"ID must be 123!");
		tester.setId("321");
		assertEquals(idReversed, tester.getId(), "ID must be 321");
	}








}
