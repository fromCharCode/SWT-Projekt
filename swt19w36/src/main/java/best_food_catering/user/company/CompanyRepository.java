package best_food_catering.user.company;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * @author David Schneider
 */
public interface CompanyRepository extends CrudRepository<Company, Long> {

	@Override
	Optional<Company> findById(Long aLong);
}
