package com.challenge.animereport.restjob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class RESTAnimeJobLauncher {

    private static final Logger LOGGER = LoggerFactory.getLogger(RESTAnimeJobLauncher.class);

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job job;

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


        urlNeeded += "?limit" + tryLimit.orElse(100);

        if(tryGenre.isPresent()) {
            urlNeeded += "&genre=" + tryGenre;
        }
        if(tryType.isPresent()) {
            urlNeeded += "&type=" + tryType;
        }
        if(tryStudio.isPresent()) {
            urlNeeded += "&studio=" + tryStudio;
        }
        if(trySource.isPresent()) {
            urlNeeded += "&source=" + trySource;
        }
        if(tryMainCast.isPresent()) {
            urlNeeded += "&mainCast=" + tryMainCast;
        }



        try {
            JobParameters jobParameters = new JobParametersBuilder().addString("dynamicApiUrl", urlNeeded).toJobParameters();

            jobLauncher.run(job, jobParameters);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }

    }



}
