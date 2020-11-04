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
package best_food_catering.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class WelcomeControllerIntegrationTests {
	@Autowired
	MockMvc mvc;

	@Test
	void showsIndex() throws Exception {
		mvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(view().name("index"));
		mvc.perform((get("/index")))
				.andExpect(status().isOk()).andExpect(view().name("index"));
	}

	@Test
	void showsAGB() throws Exception {
		mvc.perform(get("/conditions")) //
				.andExpect(status().isOk())
				.andExpect(view().name("conditions"));
	}

	@Test
	void showLogin() throws Exception {
		mvc.perform(get("/login"))
				.andExpect(status().isOk())
				.andExpect(view().name("index"));
	}


	@Test
	void showsRegister() throws Exception {
		mvc.perform(get("/register"))
				.andExpect(status().isOk())
				.andExpect(view().name("register"));
	}

	@Test
	void showsMyOrder() throws Exception {
		mvc.perform(get("/myorders")) //
				.andExpect(status().is4xxClientError());
	}

	@Test
	void showsMenu() throws Exception {
		mvc.perform(get("/menu")) //
				.andExpect(status().is3xxRedirection());
	}

	@Test
	void showsEmployees() throws Exception {
		mvc.perform(get("/employees"))
				.andExpect(status().is3xxRedirection());
	}

	@Test
	void showError() throws Exception {
		mvc.perform(get("/error"))
				.andExpect(status().isOk());
	}
}
