package com.challenge.animestreaming.scheduler;

import com.challenge.animestreaming.model.Vote;
import com.challenge.animestreaming.model.VoteDateString;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@Component
public class FixedRatePetitionScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(FixedRatePetitionScheduler.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss SSSSSS");

    private final RestTemplate restTemplate;

    public FixedRatePetitionScheduler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Scheduled(fixedRate = 15000)
    public void reportTime(){

        String streamingUrlNoDate = "https://peaceful-tor-90220.herokuapp.com/votes";
        String streamingUrl = "https://peaceful-tor-90220.herokuapp.com/votes?since=" + dateFormat.format(new Date());
        LOGGER.info("Fetching anime ids from external API by using the url: {}", streamingUrlNoDate);
        ResponseEntity<String>  response = restTemplate.getForEntity(streamingUrl, String.class);
        String votesStreamed = response.getBody();

        //String Processing
        StringBuilder sb = new StringBuilder(votesStreamed);
        sb.insert(0, '[');
        votesStreamed = sb.toString() + ']';

        System.out.println(votesStreamed);
        String noList = votesStreamed.replace("[{", "");
        String noList2 = noList.replace("]}", "");

        System.out.println(noList2);


        String[] initProcessing = votesStreamed.split("\\}\\{");
        //Arrays.stream(initProcessing).forEach(System.out::println);
        //Gson g = new Gson();
        //JsonObject[] votesFinal = g.fromJson(votesStreamed, JsonObject[].class);


        //System.out.println(votesFinal);


    }
}
