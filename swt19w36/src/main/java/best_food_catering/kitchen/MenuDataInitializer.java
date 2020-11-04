package best_food_catering.kitchen;

import best_food_catering.storage.Ingredient;
import best_food_catering.storage.IngredientForm;
import best_food_catering.storage.IngredientService;
import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.salespointframework.core.Currencies.EURO;

/**
 * @author Jonas Bohmann
 */

@Component
public class MenuDataInitializer implements DataInitializer {

	private Menu menu;
	private Menu menuNextWeek;
	private final KitchenService kitchenService;
	private final IngredientService ingredientService;
	private static final Logger LOG = LoggerFactory.getLogger(MenuDataInitializer.class);

	/**
	 * @param kitchenService    the KitchenService bean
	 * @param ingredientService the IngredientService bean
	 */
	public MenuDataInitializer(KitchenService kitchenService, IngredientService ingredientService) {
		this.menu = new Menu(kitchenService.getCurrentDate()[0], kitchenService.getCurrentDate()[1]);
		this.menuNextWeek = new Menu(kitchenService.getDateOfNextWeek()[0], kitchenService.getDateOfNextWeek()[1]);
		this.kitchenService = kitchenService;
		this.ingredientService = ingredientService;
	}

	/**
	 * The {@link DataInitializer} for all objects related to the Kitchen
	 */
	public void initialize() {

		LOG.info("KITCHEN - Creating example menus with example meals and example ingredients.");

		LocalDate bestbeforeTomato = LocalDate.of(2020, 2, 1);
		LocalDate bestbeforeSalt = LocalDate.of(2021, 2, 1);
		LocalDate bestbeforeCream = LocalDate.of(2010, 2, 1);
		List.of(
				new IngredientForm("Salz",
						3d,
						Metric.KILOGRAM.toString(),
						3.0,
						bestbeforeSalt,
						30f),
				new IngredientForm("Tomaten",
						3d,
						Metric.KILOGRAM.toString(), 2.0,
						bestbeforeTomato,
						5f),
				new IngredientForm("Sahne",
						3d,
						Metric.KILOGRAM.toString(), 1.0
						,
						bestbeforeCream,
						20f),

				new IngredientForm("Zucker",
						3d,
						Metric.KILOGRAM.toString(), 4.0
						,
						bestbeforeCream,
						3f)
		).forEach(ingredientService::createIngredient);

		Map<Ingredient, Quantity> ingredients = new HashMap<>();

		for (Ingredient ingredient : kitchenService.getAllIngredients()) {
			ingredients.put(ingredient, Quantity.of(2));
		}

		// Monday
		menu.addMeal(new Meal(
				"Pommes Frites mit hauseigener Pommes-Soße",
				"Leckere Pommes!",
				Money.of(3.50, EURO),
				ingredients,
				"Kartoffeln in Pommes Form schneiden. " +
						"Bei 250 Grad in Sonnenblumenöl frittieren. Dannach mit Salz bestreuen und mit BFC© Soße heiß " +
						"servieren.",
				Meal.FoodType.NORMAL), DayOfWeek.MONDAY);
		menu.addMeal(new Meal(
				"Mozerellasticks mit leichtem Joghurt-Dip und griechischem Salat",
				"Garantiert schmackhaft für Klein und Groß!",
				Money.of(3.79, EURO),
				ingredients,
				"Mozerellasticks für 10 Minuten braten. Dip aus Joghurt (5% Fett), Salz, Pfeffer und Petersilie " +
						"mischen. Mit Salat servieren.", Meal.FoodType.DIET), DayOfWeek.MONDAY);
		menu.addMeal(new Meal(
				"Vegane Pommes Frites aus Süßkartoffeln",
				"Frische Süßkartoffeln aus dem Oldenburger Münsterland.",
				Money.of(3.79, EURO),
				ingredients,
				"Kartoffeln in Pommes Form schneiden. Bei 250 Grad in Sonnenblumenöl frittieren.",
				Meal.FoodType.SPECIAL), DayOfWeek.MONDAY);

		// Tuesday
		menu.addMeal(new Meal(
				"Mostbraten vom Rind mit Rosenkohl und Kartoffelklößen",
				"Zarter Braten mit deftiger Soße",
				Money.of(5.25, EURO),
				ingredients,
				"Braten braten, Rosenkohl schneiden und Klöße kochen. ", Meal.FoodType.NORMAL), DayOfWeek.TUESDAY);
		menu.addMeal(new Meal(
				"Gebratene Zucchini mit Sojacreme-Avocado und Tomatenpesto-Kartoffeln",
				"Die würzigste Zucchini in ganz Dresden",
				Money.of(3.99, EURO),
				ingredients,
				"Rezept bei der Alte Mensa in Dresden klauen", Meal.FoodType.DIET), DayOfWeek.TUESDAY);
		menu.addMeal(new Meal(
				"Rotes Linsencurry mit Couscous und Salat",
				"Hauseigenes Linsencurry von Chef Hubert",
				Money.of(2.99, EURO),
				ingredients,
				"Linsen waschen und Curry kochen. Salat und Couscous vorbereiten und servieren.",
				Meal.FoodType.SPECIAL), DayOfWeek.TUESDAY);

		// Wednesday
		menu.addMeal(new Meal(
				"Schaschlikpfanne mit Kartoffelpüree und Rotkohlsalat",
				"Der Favorit unter den Schaschlikpfannen.",
				Money.of(4.99, EURO),
				ingredients,
				"Rezept bei der Alten Mensa klauen", Meal.FoodType.NORMAL), DayOfWeek.WEDNESDAY);
		menu.addMeal(new Meal(
				"Marokkanischer Hummus auf Reis-Quinoa Mix",
				"Würzig und leicht!",
				Money.of(3.99, EURO),
				ingredients,
				"Reis und Quinoa für 25 Minuten kochen. Hummus aus dem Kühlschrank nehmen.",
				Meal.FoodType.DIET), DayOfWeek.WEDNESDAY);
		menu.addMeal(new Meal(
				"Avocado-Kale Salat mit Tofu",
				"Der Trend aus 2015 jetzt auch bei uns!",
				Money.of(2.79, EURO),
				ingredients,
				"Avocado aufscheinden und in Schüssel legen. Kale vorbereiten und mit Avocado mischen. " +
						"Mit Salz, Pfeffer, Essig & Knoblauchgewürz verfeinern. Tofu anbraten.",
				Meal.FoodType.SPECIAL), DayOfWeek.WEDNESDAY);

		// Thursday
		menu.addMeal(new Meal(
				"Gegrilltes Hähnchenbrustfilet auf Milaner Risotto mit Paprika-Tomaten-Soße",
				"Zartes Fleisch von sächsischen Hühnern.",
				Money.of(4.89, EURO),
				ingredients,
				"Reis für 25 Minuten kochen. Huhn gut würzen und anbraten. Paprika und Tomaten schneiden," +
						" anbraten und in passierten Tomaten kochen. " +
						"Mit Wein aufgießen und für 50 Minuten zu dünner Soße reduzieren lassen.",
				Meal.FoodType.NORMAL), DayOfWeek.THURSDAY);
		menu.addMeal(new Meal(
				"Heringshappen in Apfel-Dillsahne mit Speckbohnen",
				"Frischer Fisch aus der Nordsee!",
				Money.of(3.29, EURO),
				ingredients,
				"Hier auch das Rezept bei der Alten Mensa klauen", Meal.FoodType.DIET), DayOfWeek.THURSDAY);
		menu.addMeal(new Meal(
						"Pizza Quattro Formaggi",
						"Mit unglaublichen 4 Käsesorten!",
						Money.of(3.99, EURO),
						ingredients,
						"Pizza aus dem Tiefkühlfach nehmen und für 11 Minuten in den Ofen.", Meal.FoodType.SPECIAL),
				DayOfWeek.THURSDAY);

		// Friday
		menu.addMeal(new Meal(
				"Echte italienische Lasagne mit Büffelmozzarella",
				"Ein echter Klassiker mit dem würzigsten Käse auf der Welt.",
				Money.of(4.99, EURO),
				ingredients,
				"Lasagne nach geheimen BFC© Rezept vorbereiten. Büffelmozzarella aus Italien importieren.",
				Meal.FoodType.NORMAL), DayOfWeek.FRIDAY);
		menu.addMeal(new Meal(
				"Gemüse-Lasagne",
				"Frisches Gemüse aus der Region.",
				Money.of(3.99, EURO),
				ingredients,
				"Gemüse-Lasagne aus dem Gefrierfach nehmen und für 10 Minuten in die Mikrowelle.",
				Meal.FoodType.DIET), DayOfWeek.FRIDAY);
		menu.addMeal(new Meal(
				"Überbackenes Kartoffelgratin mit Blumenkohl",
				"Gesund und lecker!",
				Money.of(3.45, EURO),
				ingredients,
				"Kartoffeln schneiden, Béchamelsauce vorbereiteun und Blumenkohl anbraten. Alles zusammen für " +
						"35 Minuten in den Ofen bei 250 Grad.", Meal.FoodType.SPECIAL), DayOfWeek.FRIDAY);

		for (Map.Entry<DayOfWeek, Meal> entry : menu.getNormalMeals().entrySet()) {
			kitchenService.saveMeal(entry.getValue());
			menuNextWeek.addMeal(entry.getValue(), entry.getKey());
		}

		for (Map.Entry<DayOfWeek, Meal> entry : menu.getDietMeals().entrySet()) {
			kitchenService.saveMeal(entry.getValue());
			menuNextWeek.addMeal(entry.getValue(), entry.getKey());
		}

		for (Map.Entry<DayOfWeek, Meal> entry : menu.getSpecialMeals().entrySet()) {
			kitchenService.saveMeal(entry.getValue());
			menuNextWeek.addMeal(entry.getValue(), entry.getKey());
		}

		for (Meal meal : kitchenService.getAllMeals()) {
			kitchenService.putMealInventory(meal, 500);
		}

		kitchenService.saveMenu(menu);
		kitchenService.saveMenu(menuNextWeek);


	}
}
