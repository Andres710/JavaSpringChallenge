package com.challenge.animejava.resource;

import com.challenge.animejava.document.Animes;
import com.challenge.animejava.repository.AnimesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/anime")
public class AnimesResource {

    @Autowired
    private AnimesRepository animesRepository;

    @GetMapping
    public List<Animes> getAll(@RequestParam(required = false) Integer limit, @RequestParam(required = false) String genre) {

        System.out.println("Limit: " + limit);
        System.out.println("Genre: " + genre);

        List<Animes> answer = filterWithGenre(genre);

        if(Optional.ofNullable(limit).orElse(-1) > 0) {
            return answer.stream()
                    .limit(limit)
                    .collect(Collectors.toList());
        } else {
            return answer;
        }

    }

    @GetMapping("/{id}")
    public Animes getOneAnime(@PathVariable Integer id) {
        return animesRepository.findAll().stream()
                .filter(e -> e.getAnime_id().equals(id))
                .findAny()
                .get();
    }

    //@GetMapping("/top")
    //public List<Animes> getTopAnime(@RequestParam(required = false) Integer limit,
      //                              @RequestParam(required = false) String genre,
      //                              @RequestParam(required = false) String type,
      //                              @RequestParam(required = false) String studio,
      //                              @RequestParam(required = false) String source,
      //                              @RequestParam(required = false) String mainCast) {



    //}



    public List<Animes> filterWithGenre(String askedGenre) {
        return animesRepository.findAll().stream()
                .filter(e -> e.getGenre().contains(Optional.ofNullable(askedGenre).orElse("")))
                .collect(Collectors.toList());
    }
}
