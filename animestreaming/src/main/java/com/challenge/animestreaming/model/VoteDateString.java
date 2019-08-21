package com.challenge.animestreaming.model;

import lombok.Data;

import java.util.Date;

@Data
public class VoteDateString {

    private Long id;

    private String when;

    private String user;

    private Integer anime_id;

    private Double rating;
}
