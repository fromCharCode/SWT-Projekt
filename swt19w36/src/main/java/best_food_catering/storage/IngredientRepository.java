package best_food_catering.storage;

import org.springframework.data.repository.CrudRepository;

/**
 * @author Justin Bürger
 * This is the Interface for the Ingredientrepository. For more information please see
 * the documentation of Salespoint.
 */
public interface IngredientRepository extends CrudRepository<Ingredient, Long> {

	@Override
	Iterable<Ingredient> findAll();

}
