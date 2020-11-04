package best_food_catering.statistics;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

@Controller
public class StatisticsController {

	private StatisticsService statisticsService;

	public StatisticsController(StatisticsService statisticsService){
		this.statisticsService = statisticsService;
	}

	// == public methods ==

	@GetMapping("/statistics")
	@PreAuthorize("hasAnyRole('ACCOUNTING', 'BOSS', 'KITCHEN')")
	public String showStatistics(Model model) {

		// add complete list to the model
		model.addAttribute("pageTitle", "Essens-Ranking");
		model.addAttribute("rankingComplete", statisticsService.getRanking(LocalDate.now().plusDays(100)));
		model.addAttribute("weekRanking", statisticsService.getRanking(LocalDate.now()));
		return "statistics";
	}

}
