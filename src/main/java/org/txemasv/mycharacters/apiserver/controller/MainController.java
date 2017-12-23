package org.txemasv.mycharacters.apiserver.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.txemasv.mycharacters.apiserver.model.Character;
import org.txemasv.mycharacters.apiserver.model.CharacterRepository;
import org.txemasv.mycharacters.apiserver.model.Movie;
import org.txemasv.mycharacters.apiserver.model.MovieRepository;
import org.txemasv.mycharacters.apiserver.model.MyResource;

@RestController
public class MainController {
	
	@Autowired
	private CharacterRepository repositoryCharacters;
	
	@GetMapping(value = "/")
	public List<MyResource> index() {
		List<MyResource> resources = new ArrayList<MyResource>();
		resources.add(new MyResource("characters", "GET", "/characters"));
		resources.add(new MyResource("characters", "GET", "/api/characters"));
		
		resources.add(new MyResource("new character", "POST", "/characters"));
		resources.add(new MyResource("new character", "POST", "/api/characters"));
		
		return resources;
	}
	
	@GetMapping(value = "/characters")
	public List<Character> getCharacters() {
		return repositoryCharacters.findAll();
	}
	
	@GetMapping(value = "/characters/{id}")
	public Character getCharacter(@PathVariable String id) {
		return repositoryCharacters.findOne(id);
	}
	
	@GetMapping(value = "/characters/{id}/movies")
	public List<Movie> getMoviesByCharacter(@PathVariable String id) {
		return repositoryCharacters.findOne(id).getMovies();
	}
	
}
