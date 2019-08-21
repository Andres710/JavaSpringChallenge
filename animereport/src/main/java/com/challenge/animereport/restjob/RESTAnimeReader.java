package com.challenge.animereport.restjob;

import com.challenge.animereport.model.Animes;
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
public class RESTAnimeReader implements ItemReader<Animes> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RESTAnimeReader.class);



    private final String apiUrl;
    private final RestTemplate restTemplate;

    private int nextAnimeIndex;
    private List<Animes> animeData;

    public RESTAnimeReader(String apiUrl, RestTemplate restTemplate) {
        this.apiUrl = apiUrl;
        this.restTemplate = restTemplate;
        nextAnimeIndex = 0;
    }

    @Override
    public Animes read() throws Exception {
        LOGGER.info("Reading the information of the next anime");

        if(animeDataIsNotInitialized()) {
            animeData = fetchAnimeDataFromAPI();
        }

        Animes nextAnime = null;

        if(nextAnimeIndex < animeData.size()) {
            nextAnime = animeData.get(nextAnimeIndex);
            nextAnimeIndex++;
        }

        LOGGER.info("Found anime: {}", nextAnime);

        return nextAnime;
    }

    private boolean animeDataIsNotInitialized() {
        return this.animeData == null;
    }

    private List<Animes> fetchAnimeDataFromAPI() {
        LOGGER.info("Fetching anime data from external API by using the url: {}", apiUrl);

        ResponseEntity<Animes[]> response = restTemplate.getForEntity(apiUrl, Animes[].class);
        Animes[] animeData = response.getBody();
        LOGGER.info("Found {} animes", animeData.length);

        return Arrays.asList(animeData);
    }
}
