package best_food_catering.statistics;

import best_food_catering.order.MealOrder;
import best_food_catering.order.MealOrderRepository;
import com.mysema.commons.lang.Pair;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.order.OrderLine;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;

@Transactional
@Service
public class StatisticsService {

	private final MealOrderRepository orderRepository;

	public StatisticsService(MealOrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	public ArrayList<Pair<String, Integer>> getRanking(LocalDate now) {

		HashMap<ProductIdentifier, Integer> ranking = new HashMap<>();
		HashMap<String, Integer> rankingNames;
		ArrayList<Pair<String, Integer>> ordered = new ArrayList<>();

		if (now.equals(LocalDate.now())) {
			for (MealOrder o : orderRepository.findAll()) {
				for (OrderLine ol : o.getOrderLines()) {

					if (ranking.containsKey(ol.getProductIdentifier())) {
						ranking.put(ol.getProductIdentifier(),
								ranking.get(ol.getProductIdentifier()) + ol.getQuantity().getAmount().intValue());
					} else {
						ranking.put(ol.getProductIdentifier(), ol.getQuantity().getAmount().intValue());
					}
				}
			}
		} else {
			for (MealOrder o : orderRepository.findByDate(getDate())) {
				for (OrderLine ol : o.getOrderLines()) {

					if (ranking.containsKey(ol.getProductIdentifier())) {
						ranking.put(ol.getProductIdentifier(),
								ranking.get(ol.getProductIdentifier()) + ol.getQuantity().getAmount().intValue());
					} else {
						ranking.put(ol.getProductIdentifier(), ol.getQuantity().getAmount().intValue());
					}
				}
			}
		}
		rankingNames = transformMap(ranking);

		if (rankingNames.size() != 0) {
			for (int i = 0; i <= rankingNames.size(); i++) {
				String s = Collections.max(rankingNames.entrySet(), Map.Entry.comparingByValue()).getKey();
				ordered.add(new Pair<>(s, rankingNames.get(s)));
				rankingNames.remove(s);
				i = 0;
			}
		}

		return ordered;
	}

	public HashMap<String, Integer> transformMap(HashMap<ProductIdentifier, Integer> map) {
		HashMap<String, Integer> result = new HashMap<>();

		for (MealOrder o : orderRepository.findAll()) {

			for (OrderLine ol : o.getOrderLines()) {

				for (ProductIdentifier p : map.keySet()) {

					if (ol.getProductIdentifier().equals(p)) {
						result.put(ol.getProductName(), map.get(p));
					}
				}
			}
		}
		return result;
	}

	public int getDate() {
		LocalDate date = LocalDate.now();
		TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
		int weekNumber = date.get(woy);
		return Integer.parseInt(date.getYear() + String.format("%02d", weekNumber));
	}


}
