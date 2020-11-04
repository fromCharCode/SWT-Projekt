package best_food_catering.kitchen;

import best_food_catering.order.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;

/**
 * @author Jonas Bohmann
 */

@Controller
public class KitchenController {

	private final KitchenService kitchenService;


	/**
	 * The Spring MVC Controller for the Kitchen module
	 *
	 * @param kitchenService the KitchenService bean
	 */
	public KitchenController(KitchenService kitchenService) {
		this.kitchenService = kitchenService;
	}


	/**
	 * Handles GET-Mapping
	 *
	 * @param model Spring MVC model
	 * @return the kitchen.html template for the Kitchen dashboard
	 */
	@GetMapping("/kitchen")
	@PreAuthorize("hasAnyRole('KITCHEN', 'BOSS')")
	public String index(Model model) {
		model.addAttribute("pageTitle", "Küche");
		model.addAttribute("menu", kitchenService.getMenuForCurrentWeek());
		return "kitchen";
	}


	/**
	 * Handles GET-Mapping
	 *
	 * @param model Spring MVC model
	 * @return the dailyOrders.html template to see all orders from the current day
	 */
	@GetMapping("/kitchen/daily")
	@PreAuthorize("hasAnyRole('KITCHEN', 'BOSS')")
	public String dailyOrders(Model model) {
		int dayOfWeek = LocalDate.now().getDayOfWeek().getValue();
		model.addAttribute("pageTitle", "Tägliche Bestellungen");
		model.addAttribute("day", dayOfWeek);
		model.addAttribute("report", kitchenService.getOOSReportForCurrentWeek());
		return "dailyOrders";
	}

	// --- MEALS ---

	/**
	 * Handles GET-Mapping
	 *
	 * @param model Spring MVC model
	 * @return the allMeals.html template to see a list of all existing Meals
	 */
	@GetMapping("/kitchen/meals")
	@PreAuthorize("hasAnyRole('KITCHEN', 'BOSS')")
	public String getMeals(Model model) {
		model.addAttribute("pageTitle", "Alle Gerichte");
		model.addAttribute("meals", kitchenService.getAllUniqueMeals());
		model.addAttribute("ingredients", kitchenService.getAllIngredients());
		return "allMeals";
	}

	/**
	 * Handles GET-Mapping
	 *
	 * @param model    Spring MVC model
	 * @param mealForm the form with data to create a new Meal object
	 * @return the newMeal.html template
	 */
	@GetMapping("/kitchen/meals/new")
	@PreAuthorize("hasAnyRole('KITCHEN', 'BOSS')")
	public String newMeal(Model model, AddMealForm mealForm) {
		model.addAttribute("pageTitle", "Neues Gericht");
		model.addAttribute("mealForm", mealForm);
		model.addAttribute("meals", kitchenService.getAllUniqueMeals());
		model.addAttribute("ingredients", kitchenService.getAllIngredients());
		return "newMeal";
	}

	/**
	 * Handles POST-Mapping
	 *
	 * @param addMealForm the filled-out form with data to create a new Meal object
	 * @param result      the errors that occurred
	 * @return redirect to see all existing Meals
	 */
	@PostMapping(value = "/kitchen/meals/new/post", params = "action=submit")
	@PreAuthorize("hasAnyRole('KITCHEN', 'BOSS')")
	public String createNewMeal(@Valid AddMealForm addMealForm, Errors result) {
		kitchenService.createNewMeal(addMealForm);
		return "redirect:/kitchen/meals";
	}

	/**
	 * Handles POST-Mapping
	 * <p>
	 * Adds a row to the Thymeleaf Dynamic Fields to be filled with an {@link best_food_catering.storage.Ingredient}
	 * and its {@link org.salespointframework.quantity.Quantity}
	 *
	 * @param addMealForm the filled-out form with data to create a new Meal object
	 * @param model       the Spring MVC model
	 * @return the newMeal.html template to contiune with the Meal creation
	 */
	@PostMapping(value = "/kitchen/meals/new/post", params = "action=addrow")
	public String addMealFormRow(final @ModelAttribute("mealForm") AddMealForm addMealForm, Model model) {
		model.addAttribute("meals", kitchenService.getAllUniqueMeals());
		model.addAttribute("ingredients", kitchenService.getAllIngredients());
		addMealForm.getRows().add(new AddMealRow());
		return "newMeal";
	}

	/**
	 * Handles POST-Mapping
	 * <p>
	 * Deletes a row from the Thymeleaf Dynamic Fields that was filled with an {@link best_food_catering.storage.Ingredient}
	 * and its {@link org.salespointframework.quantity.Quantity}
	 *
	 * @param addMealForm the filled-out form with data to create a new Meal object
	 * @param req         the Spring MVC HttpServletRequest with the row's ID as parameter
	 * @param model       the Spring MVC model
	 * @return the newMeal.html template to contiune with the Meal creation
	 */
	@PostMapping(value = "/kitchen/meals/new/post", params = "removerow")
	public String removeMealFormRow(final @ModelAttribute("mealForm") AddMealForm addMealForm,
									final HttpServletRequest req, Model model) {
		model.addAttribute("meals", kitchenService.getAllUniqueMeals());
		model.addAttribute("ingredients", kitchenService.getAllIngredients());
		final Integer rowId = Integer.valueOf(req.getParameter("removerow"));
		addMealForm.getRows().remove(rowId.intValue());
		return "newMeal";
	}

	/**
	 * Handles POST-Mapping
	 *
	 * @param req the Spring MVC HttpServletRequest with the Meal's ID as parameter
	 * @return redirect to see all existing Meals
	 */
	@PostMapping(value = "/kitchen/meals/delete", params = "deletemeal")
	@PreAuthorize("hasAnyRole('KITCHEN', 'BOSS')")
	public String removeMeal(final HttpServletRequest req) {
		String productIdentifier = req.getParameter("deletemeal");
		kitchenService.deleteMeal(kitchenService.getMealByName(productIdentifier).toList().get(0));
		return "redirect:/kitchen/meals";
	}

	// --- MENUs ---

	/**
	 * Handles GET-Mapping
	 *
	 * @param model Spring MVC model
	 * @return the allMenus.html template to see all existing Menus
	 */
	@GetMapping("/kitchen/menus")
	@PreAuthorize("hasAnyRole('KITCHEN', 'BOSS')")
	public String getMenus(Model model) {
		model.addAttribute("pageTitle", "Alle Speisepläne");
		model.addAttribute("menus", kitchenService.getAllMenus());
		return "allMenus";
	}

	/**
	 * Handles GET-Mapping
	 *
	 * @param model    Spring MVC model
	 * @param menuForm the form with data to create a new Menu object
	 * @return the newMenu.html template
	 */
	@GetMapping("/kitchen/menus/new")
	@PreAuthorize("hasAnyRole('KITCHEN', 'BOSS')")
	public String newMenu(Model model, AddMenuForm menuForm) {
		model.addAttribute("pageTitle", "Neuer Speiseplan");
		model.addAttribute("menuForm", menuForm);
		model.addAttribute("normals", kitchenService.getAllNormalMeals());
		model.addAttribute("specials", kitchenService.getAllSpecialMeals());
		model.addAttribute("diets", kitchenService.getAllDietMeals());
		return "newMenu";
	}

	/**
	 * Handles POST-Mapping
	 *
	 * @param addMenuForm the filled-out form with data to create a new Menu object
	 * @return redirect to see all existing Menus
	 */
	@PostMapping("/kitchen/menus/new/post")
	@PreAuthorize("hasAnyRole('KITCHEN', 'BOSS')")
	public String createNewMenu(@Valid AddMenuForm addMenuForm) {
		kitchenService.createNewMenu(addMenuForm);
		return "redirect:/kitchen/menus";
	}

	/**
	 * Handles POST-Mapping
	 *
	 * @param req the Spring MVC HttpServletRequest with the Menu's ID as parameter
	 * @return redirect to see all existing Menus
	 */
	@PostMapping(value = "/kitchen/menus/delete", params = "deletemenu")
	@PreAuthorize("hasAnyRole('KITCHEN', 'BOSS')")
	public String removeMenu(final HttpServletRequest req) {
		String date = req.getParameter("deletemenu");
		kitchenService.deleteMenu(kitchenService.getMenuByDate(Integer.parseInt(date)));
		return "redirect:/kitchen/menus";
	}


}
