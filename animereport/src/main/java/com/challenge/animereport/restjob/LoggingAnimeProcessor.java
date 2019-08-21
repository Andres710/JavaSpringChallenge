package com.challenge.animereport.restjob;

import com.challenge.animereport.model.Animes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class LoggingAnimeProcessor implements ItemProcessor<Animes, Animes> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAnimeProcessor.class);

    @Override
    public Animes process(Animes item) throws Exception {
        LOGGER.info("Processing anime information: {}", item);
        return item;
    }
}
