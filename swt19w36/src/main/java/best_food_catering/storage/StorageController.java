package best_food_catering.storage;

import best_food_catering.kitchen.KitchenReport;
import best_food_catering.kitchen.KitchenService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * This is a controller for the ingredients. Its name is due to the early development stage.
 */
@Controller
public class StorageController {

	private final IngredientService service;
	private final KitchenService kitchenService;

	/**
	 * @param service        the ingredient service
	 * @param kitchenService the kitchen service
	 */
	StorageController(IngredientService service, KitchenService kitchenService) {
		this.service = service;
		this.kitchenService = kitchenService;
	}

	/**
	 * @return returns the index of the inventory.
	 */
	@GetMapping("/inventory")
	@PreAuthorize("hasAnyRole('BOSS', 'STORAGE', 'KITCHEN')")
	public String inventory(Model model, IngredientForm form) {

		KitchenReport kitchenReport = kitchenService.getKitchenReportByDate(kitchenService.getCurrentDate()[2]);
		if (kitchenReport != null) {
			model.addAttribute("report", kitchenReport);
		}

		model.addAttribute("pageTitle", "Lager");
		model.addAttribute("ingredientForm", form);
		model.addAttribute("stock", service.findAll());
		model.addAttribute("storageDefault", service.findByBestBeforeLessThan());

		return "inventory";
	}

	/**
	 * @param form   IngredientForm
	 * @param result result of the input
	 * @param model  model
	 * @return "inventory" if result has errors
	 * "inventory/amountError" if minimal amounts of the new ingredient and an already stored ingredient
	 * with the same name differ
	 * "inventory/minAmount" if the ordered quantity is smaller than the minimal amount
	 * "inventory/negativeValue" if the value is price or the minimal value or the quantity is negative
	 * "inventory/stock" if the input was correct
	 */
	@PostMapping("/inventory/stock")
	@PreAuthorize("hasAnyRole('BOSS', 'STORAGE', 'KITCHEN')")
	public String newIngredient(@ModelAttribute("ingredientForm") @Valid IngredientForm form, Errors result, Model model) {
		if (result.hasErrors()) {
			return inventory(model, form);
		}

		if (form.getQuantity() < form.getMinAmount()) {
			model.addAttribute("minAmountError", true);
			return inventory(model, form);
		} else {
			float quantity = form.getQuantity();
			double minAmount = form.getMinAmount();
			for (Ingredient t : service.findAll()) {
				if (t.getName().equals(form.getName())) {
					BigDecimal present = t.getQuantity().getAmount();
					float pre = present.floatValue();
					quantity = quantity + pre;
				}
			}
			service.createIngredient(form);

			return ("redirect:/inventory");
		}

	}

	/**
	 * @param ingredientId id of ingredient
	 * @return "inventory/stock"
	 * deletes on ingredient
	 */
	@GetMapping("/inventory/delete")
	@PreAuthorize("hasAnyRole('BOSS', 'STORAGE')")
	public String deleteIngredient(@RequestParam long ingredientId) {
		service.deleteIngredientById(ingredientId);
		return ("redirect:/inventory");
	}

	/**
	 * @return redirect:/inventory/storageDefault
	 * Deletes all the expired ingredients
	 */
	@PostMapping("/inventory/deleteExpired")
	@PreAuthorize("hasAnyRole('BOSS', 'STORAGE')")
	public String deleteExpiredIngredient() {
		LocalDate Now = LocalDate.now();
		for (Ingredient ingredient : service.findAll()) {
			if (ingredient.getBestBefore().isBefore(Now)) {
				service.deleteIngredient(ingredient);
			}
		}
		return ("redirect:/inventory/");
	}


	/**
	 * @param model model
	 * @return kitchenReport
	 * Redirects to the up-to-date kitchen report with the ingredients needed for this week.
	 */
	@GetMapping("/inventory/kitchenReport")
	@PreAuthorize("hasAnyRole('BOSS', 'STORAGE')")
	public String kitchenReport(Model model) {
		KitchenReport kitchenReport = kitchenService.getKitchenReportByDate(kitchenService.getCurrentDate()[2]);
		if (kitchenReport != null) {
			model.addAttribute("report", kitchenReport);
		}
		return "inventory";
	}


	@GetMapping("/inventory/refill")
	@PreAuthorize("hasAnyRole('BOSS', 'STORAGE')")
	public String refill(@RequestParam long id, @RequestParam double adding) {
		service.refillIngredientByID(id, adding);
		return "redirect:/inventory/";
	}
}
