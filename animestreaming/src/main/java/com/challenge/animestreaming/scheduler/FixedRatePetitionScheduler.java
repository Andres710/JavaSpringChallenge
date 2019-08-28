package com.challenge.animestreaming.scheduler;

import com.challenge.animestreaming.model.Vote;
import com.challenge.animestreaming.model.VoteDateString;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class FixedRatePetitionScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(FixedRatePetitionScheduler.class);

    private final RestTemplate restTemplate;

    private int executions;

    public FixedRatePetitionScheduler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.executions =  0;
    }


    @Scheduled(fixedRate = 60000)
    public void retrieveVotes(){


        String streamingUrl = "https://peaceful-tor-90220.herokuapp.com/votes";

        if(executions > 0){

            ResponseEntity<Date> response = restTemplate.getForEntity("http://localhost:8082/ratings/last", Date.class);
            Date lastDate = response.getBody();

            String pattern = "dd/MM/yyyy HH:mm:ss SSSSSS";
            DateFormat df = new SimpleDateFormat(pattern);
            lastDate.setTime(lastDate.getTime()+1000);
            String sinceDate = df.format(lastDate);
            streamingUrl += "?since=" + sinceDate;

        }


        LOGGER.info("Fetching anime ids from external API by using the url: {}", streamingUrl);
        ResponseEntity<String>  response = restTemplate.getForEntity(streamingUrl, String.class);
        String votesStreamed = response.getBody();

        //String Processing
        Optional<String> incomingVotes = Optional.ofNullable(votesStreamed);

        if(incomingVotes.isPresent()) {
            StringBuilder sb = new StringBuilder(votesStreamed);
            sb.insert(0, '[');
            votesStreamed = sb.toString() + ']';

            String noList = votesStreamed.replace("[{", "");
            String noList2 = noList.replace("}]", "");

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
                votes.add(g.fromJson(stringObject, VoteDateString.class));
            }

            processDates(votes);
            executions++;
            LOGGER.info("Updated database for " + executions + " time(s) with " + votes.size() + " new votes");

        } else {
            executions++;
            LOGGER.info("Updated database for " + executions + " time(s) with 0 new votes");
        }






    }

    public void processDates(List<VoteDateString> votesUnformatted) {

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+0"));
            String dateString = "2019-08-20 21:25:51.044167+00:00";
            Date finalDate = null;
            Vote formattedVote = null;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
            for(int i = 0; i < votesUnformatted.size(); i++) {
                dateString = votesUnformatted.get(i).getWhen().split("\\+")[0];
                LocalDateTime formatDateTime = LocalDateTime.parse(dateString, formatter);
                ZonedDateTime zonedDateTime = formatDateTime.atZone(ZoneId.systemDefault());
                finalDate = Date.from(zonedDateTime.toInstant());
                formattedVote = new Vote();
                formattedVote.setMovieId(votesUnformatted.get(i).getMovie_id());
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
            final String baseUrl = "http://localhost:8082/ratings/";
            URI uri = new URI(baseUrl);
            ResponseEntity<Vote> result = restTemplate.postForEntity(uri, vote, Vote.class);

        } catch (Exception e) {

        }


    }
}
