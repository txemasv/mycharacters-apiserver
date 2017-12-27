package org.txemasv.mycharacters.apiserver.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Character {

	@Id
	private String id;
	
	@NotNull
	private String firstName;
	
	private String lastName;
	private String description;
	
	private List<Movie> movies;
	
	public Character() {}

	public Character(String firstName, String lastName, String description) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.description = description;
	}
	
	public Character(String firstName, String lastName, String description, List<Movie> movies) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.description = description;
		this.movies = movies;
	}
	
	public void setFields(Character c) {
		this.firstName = c.getFirstName();
		this.lastName = c.getLastName();
		this.description = c.getDescription();
		this.movies = c.getMovies();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Movie> getMovies() {
		return movies;
	}

	public void setMovies(List<Movie> movies) {
		this.movies = movies;
	}
}