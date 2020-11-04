package best_food_catering.user.employee;

import org.springframework.data.repository.CrudRepository;

/**
 * @author David Schneider
 */
public interface EmployeeRepository extends CrudRepository<Employee, Long> {

	@Override
	Iterable<Employee> findAll();
}