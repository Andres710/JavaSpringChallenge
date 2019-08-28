package com.challenge.animestreaming.model;

import lombok.Data;

import java.util.Date;

@Data
public class Report {

    private Integer movie_id;

    private Date when;

    private double rating;
}
