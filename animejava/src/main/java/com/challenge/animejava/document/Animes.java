package com.challenge.animejava.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



@Document(value = "CTF")
@Data
public class Animes {

    @Id
    private String _id;

    private Integer anime_id;
    private String name;
    private String genre;
    private String type;
    private Integer episodes;
    private Double rating;
    private String img;
    private String studios;
    private String source;
    private String main_cast;
    private Integer c1;
    private Integer c2;
    private Integer members;


    //Revisar Lombok!
    public String getGenre() {
        return this.genre;
    }

    public Integer getAnime_id() {
        return this.anime_id;
    }
}
