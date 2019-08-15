package com.challenge.animejava.repository;

import com.challenge.animejava.document.Animes;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AnimesRepository extends MongoRepository<Animes, String> {
}
