package best_food_catering.storage;

import best_food_catering.kitchen.KitchenService;
import best_food_catering.kitchen.Meal;
import org.javamoney.moneta.Money;
import org.salespointframework.core.Currencies;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.ArrayList;

import static java.lang.Double.compare;

/**
 * @author Justin Bürger
 * This class is the ingredient service.
 */

@Service
@Transactional
public class IngredientService {

	private final IngredientRepository ingredientRepository;
	private final KitchenService kitchenService;

	/**
	 * @param repository     this is the repository of ingredients
	 * @param kitchenService the IngredientService needs some functions of the KitchenService
	 */
	public IngredientService(IngredientRepository repository, KitchenService kitchenService) {
		Assert.notNull(repository, "repository must not be null");
		this.ingredientRepository = repository;
		this.kitchenService = kitchenService;

	}

	/**
	 * @param form ingredientForm
	 * @return Returns a new ingredient.
	 * This function returns null if there is already one ingredient with the same name and
	 * a different minimal amount.
	 */
	public Ingredient createIngredient(IngredientForm form) {

		// if the minimal amount of the item differs from the original minimal amount, it can't be stored
		for (Ingredient i : ingredientRepository.findAll()) {
			if (i.getName().equals(form.getName()) && (compare(i.getMinAmount(), form.getMinAmount()) != 0)) {
				return null;
			}
		}
		return ingredientRepository.save(new Ingredient(
				form.getName(),
				Money.of(form.getPrice(), Currencies.EURO),
				Metric.valueOf(form.getMetric()),
				form.getMinAmount(),
				form.getBestBefore(),
				Quantity.of(form.getQuantity()))
		);
	}

	/**
	 * @param id id of ingredient
	 */
	public void deleteIngredientById(long id) {
		for (Meal meal : kitchenService.getAllMeals()) {
			meal.getIngredients().keySet().removeIf(ingredient -> ingredient.getId() == id);
		}

		ingredientRepository.deleteById(id);
	}

	/**
	 * @param t ingredient which should be deleted
	 */
	public void deleteIngredient(Ingredient t) {
		for (Meal meal : kitchenService.getAllMeals()) {
			meal.getIngredients().keySet().removeIf(ingredient -> ingredient == t);
		}

		ingredientRepository.delete(t);
	}

	/**
	 * @return all ingredients
	 */
	public Iterable<Ingredient> findAll() {
		return ingredientRepository.findAll();
	}

	/**
	 * @return this returns all expired ingredients.
	 */
	public Iterable<Ingredient> findByBestBeforeLessThan() {
		ArrayList<Ingredient> expiredIngredients = new ArrayList<>();
		LocalDate Now = LocalDate.now();
		for (Ingredient ingredient : ingredientRepository.findAll()) {
			if (ingredient.getBestBefore().isBefore(Now)) {
				expiredIngredients.add(ingredient);
			}
		}
		return expiredIngredients;
	}

	public void refillIngredientByID(long id, double refill) {

		for (Ingredient ingredient : ingredientRepository.findAll()) {
			if (ingredient.getId() == id) {
				Quantity adding = Quantity.of(refill);
				ingredient.addQuantity(adding);
			}
		}
	}

}
