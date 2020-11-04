package best_food_catering.order;

import org.springframework.data.repository.CrudRepository;

/**
 * the repository containing all meal orders
 */
public interface MealOrderRepository extends CrudRepository<MealOrder, Long> {

	@Override
	Iterable<MealOrder> findAll();

	Iterable<MealOrder> findByDate(int Date);

}
