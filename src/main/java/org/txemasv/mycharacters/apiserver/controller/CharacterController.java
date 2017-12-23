package org.txemasv.mycharacters.apiserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CharacterController {
	@RequestMapping(value = "/")

	public String index() {
		return "index";
	}
}
