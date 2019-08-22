package com.challenge.animestreaming.scheduler;

import com.challenge.animestreaming.model.Vote;
import com.challenge.animestreaming.model.VoteDateString;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class FixedRatePetitionScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(FixedRatePetitionScheduler.class);

    //private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSXXX");

    private final RestTemplate restTemplate;

    public FixedRatePetitionScheduler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Scheduled(fixedRate = 15000)
    public void retrieveVotes(){


        String streamingUrlNoDate = "https://peaceful-tor-90220.herokuapp.com/votes";
        //String streamingUrl = "https://peaceful-tor-90220.herokuapp.com/votes?since=" + dateFormat.format(new Date());
        LOGGER.info("Fetching anime ids from external API by using the url: {}", streamingUrlNoDate);
        ResponseEntity<String>  response = restTemplate.getForEntity(streamingUrlNoDate, String.class);
        String votesStreamed = response.getBody();

        //String Processing
        StringBuilder sb = new StringBuilder(votesStreamed);
        sb.insert(0, '[');
        votesStreamed = sb.toString() + ']';

        String noList = votesStreamed.replace("[{", "");
        String noList2 = noList.replace("}]", "");

        System.out.println(noList2);

        String[] initProcessing = noList2.split("\\}\\{");

        List<VoteDateString> votes = new ArrayList<>();
        Gson g = new GsonBuilder()
                .setLenient()
                .create();

        String stringObject = "";
        for(int i = 0; i < initProcessing.length; i++) {
            sb = new StringBuilder(initProcessing[i]);
            sb.insert(0, '{');
            stringObject = sb.toString() + "}";
            System.out.println(stringObject);
            votes.add(g.fromJson(stringObject, VoteDateString.class));
        }

        System.out.println(votes);
        processDates(votes);



        //System.out.println(votesFinal);
    }

    public void processDates(List<VoteDateString> votesUnformatted) {

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSSZ");
            String dateString = "2019-08-20 21:25:51.044167+00:00";
            Date finalDate = null;
            Vote formattedVote = null;
            for(int i = 0; i < votesUnformatted.size(); i++) {
                dateString = votesUnformatted.get(i).getWhen();
                dateString = dateString.replaceAll("([0-9\\-T]+:[0-9]{2}:[0-9.+]+):([0-9]{2})", "$1$2");
                finalDate = dateFormat.parse(dateString);
                System.out.println(finalDate);
                formattedVote = new Vote();
                formattedVote.setMovie_id(votesUnformatted.get(i).getMovie_id());
                formattedVote.setRating(votesUnformatted.get(i).getRating());
                formattedVote.setUser(votesUnformatted.get(i).getUser());
                formattedVote.setWhen(finalDate);
                saveVote(formattedVote);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


    public void saveVote(Vote vote) {

        try {
            final String baseUrl = "http://localhost:8082/vote/";
            URI uri = new URI(baseUrl);
            ResponseEntity<Vote> result = restTemplate.postForEntity(uri, vote, Vote.class);

        } catch (Exception e) {

        }


    }
}
