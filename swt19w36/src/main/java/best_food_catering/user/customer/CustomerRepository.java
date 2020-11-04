package best_food_catering.user.customer;

import org.springframework.data.repository.CrudRepository;

/**
 * @author David Schneider
 */
public interface CustomerRepository extends CrudRepository<Customer, Long> {

	@Override
	Iterable<Customer> findAll();
}
