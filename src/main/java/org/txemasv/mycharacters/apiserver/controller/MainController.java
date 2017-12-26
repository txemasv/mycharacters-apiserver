package org.txemasv.mycharacters.apiserver.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.txemasv.mycharacters.apiserver.model.Character;
import org.txemasv.mycharacters.apiserver.model.CharacterRepository;
import org.txemasv.mycharacters.apiserver.model.ErrorType;
import org.txemasv.mycharacters.apiserver.model.ItemType;
import org.txemasv.mycharacters.apiserver.model.Movie;
import org.txemasv.mycharacters.apiserver.model.ResponseType;

@RestController
public class MainController {

	private final CharacterRepository repositoryCharacters;

	@Autowired
	MainController(CharacterRepository repositoryCharacters) {
		this.repositoryCharacters = repositoryCharacters;
	}

	@GetMapping(value = "/")
	public ResponseEntity<List<ItemType>> index() {
		List<ItemType> items = new ArrayList<ItemType>();

		// GET
		items.add(new ItemType("GET", "/characters", "list all characters", "page (default 0), limit (default 10)"));
		items.add(new ItemType("GET", "/characters/:id", "get one character", null));
		items.add(new ItemType("GET", "/characters/:id/movies", "list all movies from one character", null));
		items.add(new ItemType("GET", "/characters/:id/movies/:code", "get one movie from one character", null));

		// POST
		items.add(new ItemType("POST", "/characters", "create one character", null));

		// PUT
		items.add(new ItemType("PUT", "/characters/:id", "update one character (createIfNotExists not allowed)", null));

		// DELETE
		items.add(new ItemType("DELETE", "/characters/:id", "delete one character", null));

		return new ResponseEntity<>(items, HttpStatus.OK) ;
	}

	@GetMapping("/characters")
	public ResponseEntity<?> getCharacters(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "limit", defaultValue = "10") int limit) {
		
		Pageable pageable = new PageRequest(page, limit);
		
		Page<Character> charactersPage = repositoryCharacters.findAll(pageable);

        ResponseType response = new ResponseType();
        response.setCharacters(charactersPage.getContent());
        response.setCount(charactersPage.getTotalElements());
        response.setLimit(limit);
        response.setPage(page);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/characters/{id}")
	public ResponseEntity<?> getCharacter(@PathVariable String id) {
		
		Character character = repositoryCharacters.findOne(id);
		
		if(character == null) {
			return new ResponseEntity<>(new ErrorType("Character with id=" + id + " not found."), HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(character, HttpStatus.OK);
	}

	@GetMapping("/characters/{id}/movies")
	public ResponseEntity<?> getMoviesByCharacter(@PathVariable String id) {
		
		Character character = repositoryCharacters.findOne(id);
		
		if(character == null) {
			return new ResponseEntity<>(new ErrorType("Character with id=" + id + " not found."), HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(character.getMovies(), HttpStatus.OK);
	}

	@GetMapping("/characters/{id}/movies/{code}")
	public ResponseEntity<?> getMovieByCharacter(@PathVariable String id, @PathVariable String code) {
		
		Character character = repositoryCharacters.findOne(id);
		
		if(character == null) {
			return new ResponseEntity<>(new ErrorType("Character with id=" + id + " not found."), HttpStatus.NOT_FOUND);
		}
		
		List<Movie> movies = repositoryCharacters.findOne(id).getMovies().stream()
				.filter(a -> Objects.equals(((Movie) a).getCode(), code)).collect(Collectors.toList());
		
		return new ResponseEntity<>(movies, HttpStatus.OK);
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
