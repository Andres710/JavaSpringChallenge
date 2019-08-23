package com.challenge.animereport.restjob;

import com.challenge.animereport.model.Animes;
import com.challenge.animereport.model.Report;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Scope(value = "step")
public class RESTAnimeRatingsReader implements ItemReader<Report> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RESTAnimeRatingsReader.class);

    private String dynamicApiUrl;

    private final String apiUrl;
    private final RestTemplate restTemplate;

    private int nextRatingIndex;
    private List<Report> ratingData;

    public RESTAnimeRatingsReader(String apiUrl, RestTemplate restTemplate) {
        this.apiUrl = apiUrl;
        this.restTemplate = restTemplate;
        nextRatingIndex = 0;
    }

    @Value("#{jobParameters['dynamicApiUrl']}")
    public void setDynamicApiUrl(final String dynamicApiUrl) {
        this.dynamicApiUrl = dynamicApiUrl;
    }

    @Override
    public Report read() throws Exception {
        LOGGER.info("Reading the information of the next anime rating");

        if(animeRatingsIsNotInitialized()) {
            ratingData = fetchAnimeRatingsFromAPI();
        }

        Report nextRating = null;

        if(nextRatingIndex < ratingData.size()) {
            nextRating = ratingData.get(nextRatingIndex);
            nextRatingIndex++;
        }

        LOGGER.info("Found rating: {}", nextRating);

        return nextRating;
    }

    private boolean animeRatingsIsNotInitialized() {
        return this.ratingData == null;
    }

    private List<Report> fetchAnimeRatingsFromAPI() {

        String finalUrl = "http://localhost:8082/" + dynamicApiUrl;

        LOGGER.info("Fetching anime ratings from external API by using the url: {}", finalUrl);
        ResponseEntity<Report[]> response = restTemplate.getForEntity(finalUrl, Report[].class);
        Report[] animeRatings = response.getBody();
        LOGGER.info("Found {} anime ratings", animeRatings.length);

        return Arrays.asList(animeRatings);
    }
}
