package com.challenge.animereport.restjob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.Optional;

@RestController
public class RESTAnimeJobLauncher {

    private static final Logger LOGGER = LoggerFactory.getLogger(RESTAnimeJobLauncher.class);

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job topAnimeJob;

    @Autowired
    Job ratingAnimeJob;

    @RequestMapping("/report/top")
    public void handleTopAnimeRequest(@RequestParam(required = false) Integer limit,
                                      @RequestParam(required = false) String genre,
                                      @RequestParam(required = false) String type,
                                      @RequestParam(required = false) String studio,
                                      @RequestParam(required = false) String source,
                                      @RequestParam(required = false) String mainCast) throws Exception {

        String urlNeeded = "anime/top";

        Optional<Integer> tryLimit = Optional.ofNullable(limit);
        Optional<String> tryGenre = Optional.ofNullable(genre);
        Optional<String> tryType = Optional.ofNullable(type);
        Optional<String> tryStudio = Optional.ofNullable(studio);
        Optional<String> trySource = Optional.ofNullable(source);
        Optional<String> tryMainCast = Optional.ofNullable(mainCast);

        System.out.println(tryLimit);
        urlNeeded += "?limit=" + tryLimit.orElse(100);

        if(tryGenre.isPresent()) {
            urlNeeded += "&genre=" + tryGenre.get();
        }
        if(tryType.isPresent()) {
            urlNeeded += "&type=" + tryType.get();
        }
        if(tryStudio.isPresent()) {
            urlNeeded += "&studio=" + tryStudio.get();
        }
        if(trySource.isPresent()) {
            urlNeeded += "&source=" + trySource.get();
        }
        if(tryMainCast.isPresent()) {
            urlNeeded += "&mainCast=" + tryMainCast.get();
        }



        try {
            JobParameters jobParameters = new JobParametersBuilder().addString("dynamicApiUrl", urlNeeded).toJobParameters();

            jobLauncher.run(topAnimeJob, jobParameters);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }

    }

    @RequestMapping("report/rating/{id}")
    public void handleRatingAnimeRequest(@PathVariable Integer id){
        String urlNeeded = "ratings/" + id;

        try {
            JobParameters jobParameters = new JobParametersBuilder().addString("dynamicApiUrl", urlNeeded).toJobParameters();

            jobLauncher.run(ratingAnimeJob, jobParameters);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }


    }

    @RequestMapping("report/rating/{id}/until/{until}")
    public void handleRatingAnimeInTimeRequest(@PathVariable Integer id, @PathVariable String until){
        String urlNeeded = "ratings/" + id + "/until/" + until;

        try {
            JobParameters jobParameters = new JobParametersBuilder().addString("dynamicApiUrl", urlNeeded).toJobParameters();

            jobLauncher.run(ratingAnimeJob, jobParameters);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }


    }

    @RequestMapping("report/rating/{id}/timeseries")
    public void handleRatingAnimeTimeseriesRequest(@PathVariable Integer id){
        String urlNeeded = "ratings/" + id + "/timeseries";

        try {
            JobParameters jobParameters = new JobParametersBuilder().addString("dynamicApiUrl", urlNeeded).toJobParameters();

            jobLauncher.run(ratingAnimeJob, jobParameters);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }


    }



}
