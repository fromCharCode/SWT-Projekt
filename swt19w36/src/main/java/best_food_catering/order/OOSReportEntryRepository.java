package best_food_catering.order;

import org.springframework.data.repository.CrudRepository;

public interface OOSReportEntryRepository extends CrudRepository<OOSReportEntry, Long> {
	@Override
	Iterable<OOSReportEntry> findAll();
}
