package com.challenge.animereport.model;

import lombok.Data;
import org.springframework.scheduling.support.SimpleTriggerContext;

import java.util.Date;

@Data
public class AnimeReport {

    private Integer anime_id;

    private String name;

    private Double rating;

    private String type;

    private String source;

    private Date when;
}
