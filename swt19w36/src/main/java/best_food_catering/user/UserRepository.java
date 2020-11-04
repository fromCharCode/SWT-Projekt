package best_food_catering.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

/**
 * @author David Schneider
 */
public interface UserRepository extends CrudRepository<User, Long> {
	/**
	 * Re-declared {@link CrudRepository#findAll()} to return a {@link Streamable} instead of {@link Iterable}.
	 */
	@Override
	Streamable<User> findAll();
}
