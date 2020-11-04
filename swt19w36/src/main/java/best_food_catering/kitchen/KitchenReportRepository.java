package best_food_catering.kitchen;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Jonas Bohmann
 */

public interface KitchenReportRepository extends CrudRepository<KitchenReport, Integer> {

	@Override
	Iterable<KitchenReport> findAll();

	List<KitchenReport> findByDate(int date);
}
