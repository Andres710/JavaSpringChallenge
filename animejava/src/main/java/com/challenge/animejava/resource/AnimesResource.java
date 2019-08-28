package com.challenge.animejava.resource;

import com.challenge.animejava.document.Animes;
import com.challenge.animejava.repository.AnimesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/anime")
public class AnimesResource {

    @Autowired
    private AnimesRepository animesRepository;

    @GetMapping
    public List<Integer> getAll(@RequestParam(required = false) Integer limit, @RequestParam(required = false) String genre) {

        System.out.println("Limit: " + limit);
        System.out.println("Genre: " + genre);

        //List<Animes> answer = filterWithGenre(genre);
        List<Animes> filtered = animesRepository.findAll().stream()
                .filter(e -> e.getGenre().contains(Optional.ofNullable(genre).orElse("")))
                .collect(Collectors.toList());

        if(Optional.ofNullable(limit).orElse(-1) > 0) {
            return filtered.stream()
                    .limit(limit)
                    .map(e -> e.getAnime_id())
                    .collect(Collectors.toList());
        } else {
            return filtered.stream()
                    .map(e -> e.getAnime_id())
                    .collect(Collectors.toList());
        }

    }

    @GetMapping("/{id}")
    public Animes getOneAnime(@PathVariable Integer id) {
        return animesRepository.findAll().stream()
                .filter(e -> e.getAnime_id().equals(id))
                .findAny()
                .get();
    }

    @GetMapping("/top")
    public List<Integer> getTopAnime(@RequestParam(required = false) Integer limit,
                                    @RequestParam(required = false) String genre,
                                    @RequestParam(required = false) String type,
                                    @RequestParam(required = false) String studio,
                                    @RequestParam(required = false) String source,
                                    @RequestParam(required = false) String mainCast) {

        List<Animes> filtered = animesRepository.findAll().stream()
                .filter(e -> e.getGenre().contains(Optional.ofNullable(genre).orElse("")))
                .filter(e -> e.getType().contains(Optional.ofNullable(type).orElse("")))
                .filter(e -> e.getStudios().contains(Optional.ofNullable(studio).orElse("")))
                .filter(e -> e.getSource().contains(Optional.ofNullable(source).orElse("")))
                .filter(e -> e.getMain_cast().contains(Optional.ofNullable(mainCast).orElse("")))
                .sorted(Comparator.comparingDouble(Animes::getRating).reversed())
                .collect(Collectors.toList());

        if(Optional.ofNullable(limit).orElse(-1) > 0) {
            return filtered.stream()
                    .limit(limit)
                    .map(e -> e.getAnime_id())
                    .collect(Collectors.toList());
        } else {
            return filtered.stream()
                    .limit(100)
                    .map(e -> e.getAnime_id())
                    .collect(Collectors.toList());
        }


    }



    public List<Animes> filterWithGenre(String askedGenre) {
        return animesRepository.findAll().stream()
                .filter(e -> e.getGenre().contains(Optional.ofNullable(askedGenre).orElse("")))
                .collect(Collectors.toList());
    }
}
