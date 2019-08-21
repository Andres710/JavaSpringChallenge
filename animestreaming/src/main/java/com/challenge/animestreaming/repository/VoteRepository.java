package com.challenge.animestreaming.repository;

import com.challenge.animestreaming.model.Vote;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface VoteRepository extends CrudRepository<Vote, Long> {
}
