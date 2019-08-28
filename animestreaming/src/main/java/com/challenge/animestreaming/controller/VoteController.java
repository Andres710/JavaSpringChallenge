package com.challenge.animestreaming.controller;

import com.challenge.animestreaming.model.Report;
import com.challenge.animestreaming.model.Vote;
import com.challenge.animestreaming.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/ratings")
public class VoteController {

    @Autowired
    private VoteRepository voteRepository;

    @RequestMapping("/getAll")
    public List<Vote> getAll() {

        List<Vote> answer = StreamSupport.stream(voteRepository.findAll().spliterator(), false)
                                .collect(Collectors.toList());

        return answer;
    }

    @GetMapping("/{id}")
    public List<Report> getRatingById(@PathVariable Integer id) {
        System.out.println("Entro en el primeroooooo");
        List<Vote> foundVotes = voteRepository.findAllByMovieId(id);
        Double averageRating = foundVotes.stream()
                .mapToDouble(e -> e.getRating())
                .average()
                .orElse(0.0);

        Report reportRating = new Report();
        reportRating.setMovie_id(id);
        reportRating.setWhen(foundVotes.get(foundVotes.size()-1).getWhen());
        reportRating.setRating(averageRating);

        List<Report> reportList = new ArrayList<>();
        reportList.add(reportRating);
        return reportList;
    }

    @GetMapping("/{id}/until/{until}")
    public List<Report> getRatingByIdUntilTime(@PathVariable Integer id, @PathVariable String until) {

        try {
            List<Vote> foundVotes = voteRepository.findAllByMovieId(id);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Optional<Date> inputDate = Optional.ofNullable(dateFormat.parse((until)));
            Double averageRating = 0.0;

            int counter = 0;
            for(int i = 0; i < foundVotes.size(); i++) {
                if(inputDate.get().compareTo(foundVotes.get(i).getWhen()) >= 0) {
                    averageRating += foundVotes.get(i).getRating();
                    counter++;
                }
            }

            Report reportRating = new Report();
            reportRating.setMovie_id(id);
            reportRating.setWhen(inputDate.get());
            reportRating.setRating(averageRating/counter);

            List<Report> reportList = new ArrayList<>();
            reportList.add(reportRating);
            return reportList;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            Report reportRating = new Report();
            reportRating.setMovie_id(id);
            reportRating.setRating(-1.0);

            List<Report> reportList = new ArrayList<>();
            reportList.add(reportRating);
            return reportList;

        }

    }

    @GetMapping("/{id}/timeseries")
    public List<Report> getRatingByIdTimeseries(@PathVariable Integer id) {
        List<Vote> foundVotes = voteRepository.findAllByMovieId(id);
        return getTimeseriesRating(foundVotes);
    }



    @GetMapping("/{id}/allVotes")
    public List<Vote> getAllVotesgById(@PathVariable Integer id) {
        List<Vote> foundVotes = voteRepository.findAllByMovieId(id);
        return foundVotes;
    }


    @PostMapping(path = "/", consumes = "application/json", produces = "application/json")
    public Vote insertVote(@RequestBody Vote vote) {
        voteRepository.save(vote);
        return vote;
    }

    @DeleteMapping
    public void deleteAll() {
        voteRepository.deleteAll();
    }


    @GetMapping("/last")
    public Date findLastVote() {
        return voteRepository.findMaxDate();

    }


    //Helper
    public List<Report> getTimeseriesRating(List<Vote> votes) {

        double[] ratings = new double[votes.size()];

        for(int i = 0; i < votes.size(); i++) {
            if(i == 0) {
                ratings[i] = votes.get(i).getRating();
            } else {
                ratings[i] = ratings[i-1] + votes.get(i).getRating();
            }
        }

        List<Report> timeseriesReport = new ArrayList<>();
        Report currentReport = null;
        for(int j = 0; j < ratings.length; j++) {
            currentReport = new Report();
            currentReport.setMovie_id(votes.get(j).getMovieId());
            currentReport.setWhen(votes.get(j).getWhen());
            currentReport.setRating(ratings[j]/(j+1));
            timeseriesReport.add(currentReport);
        }

        return timeseriesReport;
    }

}
