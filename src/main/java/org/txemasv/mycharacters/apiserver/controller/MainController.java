package org.txemasv.mycharacters.apiserver.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

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
import org.txemasv.mycharacters.apiserver.model.Movie;
import org.txemasv.mycharacters.apiserver.util.ErrorType;
import org.txemasv.mycharacters.apiserver.util.ItemType;
import org.txemasv.mycharacters.apiserver.util.ResponseType;
import org.txemasv.mycharacters.apiserver.util.SuccessType;

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
		items.add(new ItemType("GET", "/characters", "list all characters", "[page, limit]"));
		items.add(new ItemType("GET", "/characters/:id", "get one character", "[]"));
		items.add(new ItemType("GET", "/characters/:id/movies", "list all movies from one character", "[]"));
		items.add(new ItemType("GET", "/characters/:id/movies/:code", "get one movie from one character", "[]"));

		// POST
		items.add(new ItemType("POST", "/characters", "create one character", "[]"));

		// PUT
		items.add(new ItemType("PUT", "/characters/:id", "update one character (createIfNotExists not allowed)", "[]"));

		// DELETE
		items.add(new ItemType("DELETE", "/characters/:id", "delete one character", "[]"));

		return new ResponseEntity<>(items, HttpStatus.OK);
	}

	@GetMapping("/characters/insert/{number}")
	public void insert(@PathVariable(value = "number") int number) {

		for (int i = 0; i < number; i++) {
			Character character = new Character("Character_" + i, "Surname_" + i, "description_" + i);
			List<Movie> movies = new ArrayList<Movie>();
			movies.add(new Movie("code_" + i, "Title_" + i, "year_" + 1, "description_" + i));
			character.setMovies(movies);
			repositoryCharacters.save(character);
		}

		if (number == -1) {
			repositoryCharacters.deleteAll();
		}
	}

	/* Characters */

	@GetMapping("/characters")
	public ResponseEntity<?> getCharacters(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "10") int limit) {

		Pageable pageable = new PageRequest(page, limit);
		Page<Character> charactersPage = repositoryCharacters.findAll(pageable);

		ResponseType response = new ResponseType();

		if (charactersPage.getNumberOfElements() == 0) {
			response.setInfo("empty collection");
		}

		response.setCount(charactersPage.getTotalElements());
		response.setLimit(limit);
		response.setPage(charactersPage.getNumber());
		response.setCharacters(charactersPage.getContent());

		// TODO:filter (firstName, lastName, description)

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/characters/{id}")
	public ResponseEntity<?> getCharacter(@PathVariable String id) {

		Character character = repositoryCharacters.findOne(id);

		if (character == null) {
			return new ResponseEntity<>(new ErrorType("Character not found"), HttpStatus.NOT_FOUND);
		}

		// TODO:filter (firstName, lastName, description)

		return new ResponseEntity<>(character, HttpStatus.OK);
	}

	@PostMapping("/characters")
	public ResponseEntity<?> createCharacter(@RequestBody Character character) {

		try {
			repositoryCharacters.save(new Character(character.getFirstName(), character.getLastName(),
					character.getDescription(), character.getMovies()));

			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().build().toUri());
			return new ResponseEntity<>(new SuccessType("character created"), httpHeaders, HttpStatus.CREATED);
			
		} catch (ConstraintViolationException e) {
			return new ResponseEntity<>(new ErrorType("id can't be null"), HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/characters")
	public ResponseEntity<?> updateCharacter(@RequestBody Character character) {

		try {
			Character characterPersistent = repositoryCharacters.findOne(character.getId());
			if (characterPersistent != null) {
				characterPersistent.setFields(character);
				repositoryCharacters.save(characterPersistent);

			} else {
				return new ResponseEntity<>(new ErrorType("character not found"), HttpStatus.NOT_FOUND);
			}

		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(new ErrorType("id can't be null"), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(new SuccessType("character updated"), HttpStatus.OK);
	}

	@DeleteMapping("/characters/{id}")
	public ResponseEntity<?> deleteCharacter(@PathVariable String id) {

		try {
			Character character = repositoryCharacters.findOne(id);

			if (character != null) {
				repositoryCharacters.delete(character);

			} else {
				return new ResponseEntity<>(new ErrorType("character not found"), HttpStatus.NOT_FOUND);
			}

		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(new ErrorType("id can't be null"), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(new SuccessType("character deleted"), HttpStatus.OK);
	}

	// TODO: DELETE characters -> delete all characters

	/* Movies */

	@GetMapping("/characters/{id}/movies")
	public ResponseEntity<?> getMoviesByCharacter(@PathVariable String id) {

		Character character = repositoryCharacters.findOne(id);

		if (character == null) {
			return new ResponseEntity<>(new ErrorType("Character not found"), HttpStatus.NOT_FOUND);
		}

		// TODO:filter (firstName, lastName, description)

		return new ResponseEntity<>(character.getMovies(), HttpStatus.OK);
	}

	@GetMapping("/characters/{id}/movies/{code}")
	public ResponseEntity<?> getMovieByCharacter(@PathVariable String id, @PathVariable String code) {

		Character character = repositoryCharacters.findOne(id);

		if (character == null) {
			return new ResponseEntity<>(new ErrorType("Character not found"), HttpStatus.NOT_FOUND);
		}

		List<Movie> movies = repositoryCharacters.findOne(id).getMovies().stream()
				.filter(a -> Objects.equals(((Movie) a).getCode(), code)).collect(Collectors.toList());

		if (movies.size() == 0) {
			return new ResponseEntity<>(new ErrorType("Movie not found for this character"), HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(movies, HttpStatus.OK);
	}

	// TODO: PATCH user -> create one movie = update user adding one movie

	// TODO: PUT movie -> update movie

	// TODO: DELETE movie -> delete one movie

	// TODO: DELETE movies -> delete all movie

}
