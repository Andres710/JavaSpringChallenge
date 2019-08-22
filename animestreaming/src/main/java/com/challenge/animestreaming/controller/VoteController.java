package com.challenge.animestreaming.controller;

import com.challenge.animestreaming.model.Vote;
import com.challenge.animestreaming.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/vote")
public class VoteController {

    @Autowired
    private VoteRepository voteRepository;

    @RequestMapping("/getAll")
    public List<Vote> getAll() {

        List<Vote> answer = StreamSupport.stream(voteRepository.findAll().spliterator(), false)
                                .collect(Collectors.toList());

        return answer;
    }

    @PostMapping(path = "/", consumes = "application/json", produces = "application/json")
    public Vote insertVote(@RequestBody Vote vote) {

        System.out.println("Se mandó la peticiooooooon");
        System.out.println(vote);
        return vote;
    }
}
