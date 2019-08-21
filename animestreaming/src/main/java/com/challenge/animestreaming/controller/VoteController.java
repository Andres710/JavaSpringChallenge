package com.challenge.animestreaming.controller;

import com.challenge.animestreaming.model.Vote;
import com.challenge.animestreaming.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping("/addVote")
    public List<Vote> insertVote(Vote vote) {
        voteRepository.save(vote);
        return StreamSupport.stream(voteRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }
}
