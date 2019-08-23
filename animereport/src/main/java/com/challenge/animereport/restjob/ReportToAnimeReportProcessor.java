package com.challenge.animereport.restjob;

import com.challenge.animereport.model.AnimeReport;
import com.challenge.animereport.model.Animes;
import com.challenge.animereport.model.Report;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ReportToAnimeReportProcessor implements ItemProcessor<Report, AnimeReport> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportToAnimeReportProcessor.class);


    @Override
    public AnimeReport process(Report report) throws Exception {
        LOGGER.info("Processing the report with anime id: {}", report.getMovie_id());

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Animes> response = restTemplate.getForEntity("http://localhost:8080/anime/" + report.getMovie_id(), Animes.class);
        Animes foundAnime = response.getBody();

        LOGGER.info("Processed id to anime: {}", foundAnime);
        AnimeReport animeReport = new AnimeReport();
        animeReport.setAnime_id(foundAnime.getAnime_id());
        animeReport.setName(foundAnime.getName());
        animeReport.setSource(foundAnime.getSource());
        animeReport.setType(foundAnime.getType());
        animeReport.setRating(report.getRating());
        animeReport.setWhen(report.getWhen());

        return animeReport;
    }
}
