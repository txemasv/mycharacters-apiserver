package org.txemasv.mycharacters.apiserver.model;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CharacterRepository extends MongoRepository<Character, String> {

}