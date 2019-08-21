package com.challenge.animereport.controller;

import com.challenge.animereport.model.Animes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.print.attribute.IntegerSyntax;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/anime")
public class AnimeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnimeController.class);

    @RequestMapping(method = RequestMethod.GET)
    public List<Animes>  findAnimes() {
        LOGGER.info("Finding animes");

        List<Animes> animes = createAnimes();

        LOGGER.info("Found {} animes", animes.size());

        return animes;
    }

    @RequestMapping("/ids")
    public List<Integer> findAnimeIds() {
        return Arrays.asList(32281, 5114, 28977, 9253);
    }


    private List<Animes> createAnimes() {
        Animes naruto = new Animes();
        naruto.setAnime_id(1);
        naruto.setName("Naruto");
        naruto.setEpisodes(600);

        Animes bleach = new Animes();
        bleach.setAnime_id(2);
        bleach.setName("Bleach");
        bleach.setEpisodes(500);

        return Arrays.asList(naruto, bleach);

    }
}
