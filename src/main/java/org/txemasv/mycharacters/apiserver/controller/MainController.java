package org.txemasv.mycharacters.apiserver.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.txemasv.mycharacters.apiserver.model.Character;
import org.txemasv.mycharacters.apiserver.model.CharacterRepository;
import org.txemasv.mycharacters.apiserver.model.Item;
import org.txemasv.mycharacters.apiserver.model.Movie;

@RestController
public class MainController {

	private final CharacterRepository repositoryCharacters;

	@Autowired
	MainController(CharacterRepository repositoryCharacters) {
		this.repositoryCharacters = repositoryCharacters;
	}

	@GetMapping(value = "/")
	public List<Item> index() {
		List<Item> items = new ArrayList<Item>();

		// GET
		items.add(new Item("GET", "/characters", "list all characters"));
		items.add(new Item("GET", "/characters/:id", "get one character"));
		items.add(new Item("GET", "/characters/:id/movies", "list all movies from one character"));
		items.add(new Item("GET", "/characters/:id/movies/:code", "get one movie from one character"));

		// POST
		items.add(new Item("POST", "/characters", "create one character"));

		// PUT
		items.add(new Item("PUT", "/characters/:id", "update one character (createIfNotExists not allowed)"));

		// DELETE
		items.add(new Item("DELETE", "/characters/:id", "delete one character"));

		return items;
	}

	@GetMapping("/characters")
	public List<Character> getCharacters() {
		return repositoryCharacters.findAll();
	}

	@GetMapping("/characters/{id}")
	public Character getCharacter(@PathVariable String id) {
		return repositoryCharacters.findOne(id);
	}

	@GetMapping("/characters/{id}/movies")
	public List<Movie> getMoviesByCharacter(@PathVariable String id) {
		return repositoryCharacters.findOne(id).getMovies();
	}

	@GetMapping("/characters/{id}/movies/{code}")
	public List<Movie> getMovieByCharacter(@PathVariable String id, @PathVariable String code) {
		return repositoryCharacters.findOne(id).getMovies().stream()
				.filter(a -> Objects.equals(((Movie) a).getCode(), code)).collect(Collectors.toList());
	}

	@PostMapping("/characters")
	public ResponseEntity<?> createCharacter(@RequestBody Character character) {

		repositoryCharacters.save(new Character(character.getFirstName(), character.getLastName(),
				character.getDescription(), character.getMovies()));

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().build().toUri());

		return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/characters/{id}")
	public ResponseEntity<?> deleteCharacter(@PathVariable String id) {
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().build().toUri());
		
		try {
			Character character = repositoryCharacters.findOne(id);
			
			if(character != null) {
				repositoryCharacters.delete(character);
				
			} else {
				return new ResponseEntity<>(null, httpHeaders, HttpStatus.NOT_FOUND);
			}
			
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(null, httpHeaders, HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(null, httpHeaders, HttpStatus.OK);
	}
	
	@PutMapping("/characters")
	public ResponseEntity<?> updateCharacter(@RequestBody Character character) {
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().build().toUri());
		
		try {
			Character characterPersistent = repositoryCharacters.findOne(character.getId());
			if(characterPersistent != null) {
				characterPersistent.setFields(character);
				repositoryCharacters.save(characterPersistent);
				
			} else {
				return new ResponseEntity<>(null, httpHeaders, HttpStatus.NOT_FOUND);
			}			
			
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(null, httpHeaders, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(null, httpHeaders, HttpStatus.OK);
	}

}
