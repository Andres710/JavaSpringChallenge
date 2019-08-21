package com.challenge.animereport.restjob;

import com.challenge.animereport.model.Animes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

@StepScope
public class LoggingAnimeWriter implements ItemWriter<Animes> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAnimeWriter.class);

    @Override
    public void write(List<? extends Animes> items) throws Exception {
        LOGGER.info("Received the information of {} animes", items.size());

        items.forEach(i -> LOGGER.info("Received the information of an anime: {}", i));

    }
}
