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
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class RESTAnimeJobLauncher {

    private static final Logger LOGGER = LoggerFactory.getLogger(RESTAnimeJobLauncher.class);

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job job;

    @RequestMapping("/report/top")
    public void handleTopAnimeRequest() throws Exception {

        try {
            JobParameters jobParameters = new JobParametersBuilder().addString("dynamicApiUrl", "anime/top").toJobParameters();

            jobLauncher.run(job, jobParameters);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }

    }



}
