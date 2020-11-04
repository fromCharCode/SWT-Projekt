package best_food_catering.storage;


import best_food_catering.user.UserService;
import best_food_catering.user.UserStatus;
import best_food_catering.user.customer.CustomerRegistrationForm;
import best_food_catering.user.customer.CustomerRepository;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.text.Bidi;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ExtendedModelMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;

import javax.money.MonetaryAmount;
import javax.money.spi.MonetaryAmountsSingletonSpi;
import javax.validation.Valid;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.springframework.format.annotation.DateTimeFormat;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.*;


@SpringBootTest
@AutoConfigureMockMvc
public class CompareIngredientTest {


//	private String name = "julio";
//
//	private MonetaryAmount price =
//		Money.of(100, "USD");
//	};
//	private Metric metric;
//	private Double minAmount;
//	private Quantity quantity;
//
//	@DateTimeFormat(pattern = "yyyy-MM-dd")
//	private LocalDate bestBefore;
	@Test
	void CompareIngredientTested(){
//		Ingredient ingredientTest = new Ingredient(
//			"name",
//			"price",
//			"metric",
//			"minAmount",
//			"bestBefore",
//		"quantity"
//		);
		Ingredient ingredientTest = new Ingredient();
		BigDecimal difference = new BigDecimal(123);
		CompareIngredient tester = new CompareIngredient(ingredientTest, difference);





	}





}
