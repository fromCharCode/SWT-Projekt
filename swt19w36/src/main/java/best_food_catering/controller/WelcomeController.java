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

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

	@GetMapping("/")
	public String init(Model model) {
		model.addAttribute("pageTitle", "Best-Food-Catering");
		return "index";
	}

	@GetMapping("/index")
	public String index(Model model) {
		return init(model);
	}

	@GetMapping("/login")
	public String login(Model model) {
		return init(model);
	}

	@GetMapping("/conditions")
	public String conditions(Model model) {
		model.addAttribute("pageTitle", "Allgemeine Geschäftsbedingungen");
		return "conditions";
	}

	@GetMapping("/error")
	public String errorHandler(Model model) {
		model.addAttribute("pageTitle", "Error - Irgenetwas ist schiefgegangen");
		return "error";
	}

}