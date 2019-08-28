package com.challenge.animestreaming.repository;

import com.challenge.animestreaming.model.Vote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Date;
import java.util.List;

@RepositoryRestResource
public interface VoteRepository extends CrudRepository<Vote, Long> {
    List<Vote> findAllByMovieId(Integer id);

    @Query("SELECT MAX(v.when) From Vote v")
    Date findMaxDate();

}
