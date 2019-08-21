package com.challenge.animereport.restjob;

import com.challenge.animereport.model.Animes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@StepScope
public class RESTAnimeIdsReader implements ItemReader<Integer> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RESTAnimeReader.class);


    private String dynamicApiUrl;

    private final String apiUrl;
    private final RestTemplate restTemplate;

    private int nextIntIndex;
    private List<Integer> animeIds;

    public RESTAnimeIdsReader(String apiUrl, RestTemplate restTemplate) {
        this.apiUrl = apiUrl;
        this.restTemplate = restTemplate;
        nextIntIndex = 0;
    }

    @Value("#{jobParameters['dynamicApiUrl']}")
    public void setDynamicApiUrl(final String dynamicApiUrl) {
        this.dynamicApiUrl = dynamicApiUrl;
    }

    @Override
    public Integer read() throws Exception {
        LOGGER.info("Reading the information of the next anime ID");

        if(animeIdsIsNotInitialized()) {
            animeIds = fetchAnimeIdsFromAPI();
        }

        Integer nextId = null;

        if(nextIntIndex < animeIds.size()) {
            nextId = animeIds.get(nextIntIndex);
            nextIntIndex++;
        }

        LOGGER.info("Found anime id: {}", nextId);

        return nextId;
    }

    private boolean animeIdsIsNotInitialized() {
        return this.animeIds == null;
    }

    private List<Integer> fetchAnimeIdsFromAPI() {



        String finalUrl = "http://localhost:8080/" + dynamicApiUrl;

        LOGGER.info("Fetching anime ids from external API by using the url: {}", finalUrl);
        ResponseEntity<Integer[]> response = restTemplate.getForEntity(finalUrl, Integer[].class);
        Integer[] animeIds = response.getBody();
        LOGGER.info("Found {} animes", animeIds.length);

        return Arrays.asList(animeIds);
    }
}
