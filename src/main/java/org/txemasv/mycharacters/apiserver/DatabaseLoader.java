package org.txemasv.mycharacters.apiserver;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.CommandLineRunner;

import org.springframework.stereotype.Component;
import org.txemasv.mycharacters.apiserver.model.Character;
import org.txemasv.mycharacters.apiserver.model.CharacterRepository;
import org.txemasv.mycharacters.apiserver.model.Movie;


@Component
public class DatabaseLoader implements CommandLineRunner {

	private final CharacterRepository repository;

	@Autowired
	public DatabaseLoader(CharacterRepository repository) {
		this.repository = repository;
	}

	@Override
	public void run(String... strings) throws Exception {
		
		repository.deleteAll();
		
		Character character = new Character("Frodo", "Baggins", "ring bearer");
		List<Movie> movies = new ArrayList<Movie>();
		movies.add(new Movie("THLOR001-001", "The Lord of Rings", "2001", "fantastic universe"));
		movies.add(new Movie("THHOB013-001", "The Hobbit", "2013", "fantastic universe"));
		character.setMovies(movies);
		repository.save(character);
		
		character = new Character("Regan", "MacNeil", "creepy possessed girl");
		movies = new ArrayList<Movie>();
		movies.add(new Movie("THEXO970-001", "The Exorcist", "1970", "terror"));
		movies.add(new Movie("THEXO977-002", "Exorcist II: The Heretic", "1977", "terror"));
		character.setMovies(movies);
		repository.save(character);
		
		character = new Character("Pennywise", "Clown", "creepy clownn");
		movies = new ArrayList<Movie>();
		movies.add(new Movie("IT000990-001", "It", "1990", "Steephen king horor story"));
		movies.add(new Movie("IT000017-002", "It", "2017", "New version of Steephen king horor story"));
		character.setMovies(movies);
		repository.save(character);
		
	}

}
