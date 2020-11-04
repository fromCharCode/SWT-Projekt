package best_food_catering.kitchen;

import org.springframework.data.repository.CrudRepository;

/**
 * @author Jonas Bohmann
 */

public interface MenuRepository extends CrudRepository<Menu, Integer> {

	@Override
	Iterable<Menu> findAll();

	Menu findByCalenderWeek(int week);

	Menu findByDate(int date);
}