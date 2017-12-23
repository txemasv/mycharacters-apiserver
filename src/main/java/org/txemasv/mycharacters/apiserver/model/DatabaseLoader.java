package org.txemasv.mycharacters.apiserver.model;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.CommandLineRunner;

import org.springframework.stereotype.Component;


@Component
public class DatabaseLoader implements CommandLineRunner {

	private final CharacterRepository repository;

	@Autowired
	public DatabaseLoader(CharacterRepository repository) {
		this.repository = repository;
	}

	@Override
	public void run(String... strings) throws Exception {
		this.repository.save(new Character("Frodo", "Baggins", "ring bearer"));
	}

}
