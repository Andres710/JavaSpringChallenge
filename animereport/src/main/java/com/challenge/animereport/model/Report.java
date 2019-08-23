package com.challenge.animereport.model;

import lombok.Data;

import java.util.Date;

@Data
public class Report {

    private Integer movie_id;

    private Date when;

    private Double rating;
}
