package best_food_catering.order;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * the repository for the OOSReports
 */
public interface ReportRepository extends CrudRepository<OOSReport, Long> {


	@Override
	Iterable<OOSReport> findAll();

	List<OOSReport> findByDate(int date);
}
