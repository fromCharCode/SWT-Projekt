package best_food_catering.user.employee;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeTest {
	String role =null;
	@Test
	void setRole(){
		Employee tester = new Employee();
		assertEquals(role, tester.getRole(), "role should be 1");
	}
}
