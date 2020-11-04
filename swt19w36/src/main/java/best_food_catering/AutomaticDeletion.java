package best_food_catering;

import best_food_catering.user.UserService;
import best_food_catering.user.employee.Employee;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Justin Bürger
 * This class is essentially a login handler. You can set the default page for a user according to his role
 * and other useful stuff.
 */
@Component
public class AutomaticDeletion implements AuthenticationSuccessHandler {

	private UserService userService;
	private static final String LOGIN_SUCCESSOR_ROUTE = "/menu";
	private static final String LOGIN_STORAGE_ROUTE = "/inventory";
	private static final String LOGIN_ACCOUNTING_ROUTE = "/accounting";
	private static final String LOGIN_KITCHEN_ROUTE = "/kitchen";
	private static final String LOGIN_ADMIN_ROUTE = "/admin";

	/**
	 * @param userService UserService
	 */
	public AutomaticDeletion(UserService userService) {
		this.userService = userService;
	}

	/**
	 * @param request        server request
	 * @param response       server response
	 * @param authentication authentication
	 * @throws IOException      input/output exception
	 * @throws ServletException servlet exception
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
										Authentication authentication) throws IOException, ServletException {

		Employee e = userService.findEmployeeByName(authentication.getName());
		if (e == null) {
			response.sendRedirect(LOGIN_SUCCESSOR_ROUTE);
		} else if (e.getRole().equals("STORAGE")) {
			response.sendRedirect(LOGIN_STORAGE_ROUTE);
		} else if (e.getRole().equals("ACCOUNTING")) {
			response.sendRedirect(LOGIN_ACCOUNTING_ROUTE);
		} else if (e.getRole().equals("KITCHEN")) {
			response.sendRedirect(LOGIN_KITCHEN_ROUTE);
		} else if (e.getRole().equals("ADMIN")) {
			response.sendRedirect(LOGIN_ADMIN_ROUTE);
		} else {
			response.sendRedirect(LOGIN_SUCCESSOR_ROUTE);
		}
	}
}

