/*
 * Copyright 2014-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package best_food_catering;

import org.salespointframework.EnableSalespoint;
import org.salespointframework.SalespointSecurityConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableSalespoint
@EntityScan
@EnableScheduling
public class Application {

	private static final String LOGIN_ROUTE = "/";
	private static final String LOGIN_SUCCESSOR_ROUTE = "/menu";
	private static final String LOGOUT_SUCCESSOR_ROUTE = "/";

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Configuration
	static class BestFoodCateringConfiguration implements WebMvcConfigurer {
		@Override
		public void addViewControllers(ViewControllerRegistry registry) {
			registry.addViewController(LOGIN_ROUTE).setViewName("index");
			registry.addViewController("/").setViewName("menu");
		}
	}

	@Configuration
	@EnableWebSecurity
	static class BestFoodCateringSecurityConfiguration extends SalespointSecurityConfiguration {

		private final AutomaticDeletion deletion;

		BestFoodCateringSecurityConfiguration(AutomaticDeletion deletion) {
			this.deletion = deletion;
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests()
					.antMatchers("/**").permitAll()
					.antMatchers("/register").permitAll().and()
					.formLogin().loginPage(LOGIN_ROUTE).loginProcessingUrl("/login").successHandler(deletion)
					// <- Registrierung erfolgt hier
					.and().logout().logoutUrl("/logout").logoutSuccessUrl(LOGOUT_SUCCESSOR_ROUTE);
		}
	}

}
