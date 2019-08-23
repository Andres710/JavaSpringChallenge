package com.challenge.animereport.restjob;

import com.challenge.animereport.model.Animes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@StepScope
public class IdToAnimeProcessor implements ItemProcessor<Integer, Animes> {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdToAnimeProcessor.class);


    @Override
    public Animes process(Integer id) throws Exception {
        LOGGER.info("Processing the id: {}", id);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Animes> response = restTemplate.getForEntity("http://localhost:8080/anime/" + id, Animes.class);
        Animes foundAnime = response.getBody();

        LOGGER.info("Processed id to anime: {}", foundAnime);
        return foundAnime;
    }
}
