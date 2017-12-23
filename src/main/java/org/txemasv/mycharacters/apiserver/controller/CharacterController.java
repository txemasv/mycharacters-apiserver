package org.txemasv.mycharacters.apiserver.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.txemasv.mycharacters.apiserver.model.Character;
import org.txemasv.mycharacters.apiserver.model.CharacterRepository;
import org.txemasv.mycharacters.apiserver.model.MyResource;

@RestController
public class CharacterController {
	
	@Autowired
	private CharacterRepository repository;
	
	@GetMapping(value = "/")
	public List<MyResource> index() {
		List<MyResource> resources = new ArrayList<MyResource>();
		resources.add(new MyResource("characters", "/characters"));
		resources.add(new MyResource("characters", "/api/characters"));
		return resources;
	}
	
	@GetMapping(value = "/characters")
	public List<Character> findAll() {
		return repository.findAll();
	}
	
	@GetMapping(value = "/characters/{id}")
	public Character findOne(@PathVariable String id) {
		return repository.findOne(id);
	}
	
	@PostMapping(value = "/characters")
	public ResponseEntity<Character> update(@RequestBody Character character) {

	    if (character != null) {
	    	repository.save(character);
	    }
	    
	    return new ResponseEntity<Character>(character, HttpStatus.OK);
	}
}
