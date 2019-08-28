package com.challenge.animereport;

import com.challenge.animereport.model.AnimeReport;
import com.challenge.animereport.model.Animes;
import com.challenge.animereport.model.Report;
import com.challenge.animereport.restjob.*;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BatchConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    private Resource outputResource1 = new FileSystemResource("output/topAnime.csv");

    private Resource outputResource2 = new FileSystemResource("output/animeRating.csv");

    @Bean
    @StepScope
    ItemReader<Integer> restAnimeIdsReader(RestTemplate restTemplate) {
        return new RESTAnimeIdsReader("urlPruebaaaa", restTemplate);
    }

    @Bean
    @StepScope
    ItemReader<Report> restAnimeRatingsReader(RestTemplate restTemplate) {
        return new RESTAnimeRatingsReader("urlPruebaaaa", restTemplate);
    }

    @Bean
    FlatFileItemWriter<Animes> writer1() {
        FlatFileItemWriter<Animes> writer = new FlatFileItemWriter<>();

        writer.setResource(outputResource1);

        writer.setAppendAllowed(false);

        writer.setLineAggregator(new DelimitedLineAggregator<Animes>(){
            {
                setDelimiter(",");
                setFieldExtractor(new BeanWrapperFieldExtractor<Animes>(){
                    {
                        setNames(new String[] {"anime_id", "name", "rating", "type", "source"});
                    }
                });
            }
        });

        return writer;
    }

    @Bean
    FlatFileItemWriter<AnimeReport> writer2() {
        FlatFileItemWriter<AnimeReport> writer = new FlatFileItemWriter<>();

        writer.setResource(outputResource2);

        writer.setAppendAllowed(false);

        writer.setLineAggregator(new DelimitedLineAggregator<AnimeReport>(){
            {
                setDelimiter(",");
                setFieldExtractor(new BeanWrapperFieldExtractor<AnimeReport>(){
                    {
                        setNames(new String[] {"anime_id", "name", "type", "source", "rating", "when"});
                    }
                });
            }
        });

        return writer;
    }



    @Bean
    public Step step1(ItemReader<Integer> restAnimeIdsReader) {

        return stepBuilderFactory.get("step1")
                .<Integer, Animes>chunk(1)
                .reader(restAnimeIdsReader)
                .processor(new IdToAnimeProcessor())
                .writer(writer1())
                .build();

    }

    @Bean
    public Step step2(ItemReader<Report> restAnimeRatingsReader) {

        return stepBuilderFactory.get("step2")
                .<Report, AnimeReport>chunk(1)
                .reader(restAnimeRatingsReader)
                .processor(new ReportToAnimeReportProcessor())
                .writer(writer2())
                .build();

    }

    @Bean
    public Job topAnimeJob(@Qualifier("step1") Step step1) {
        return jobBuilderFactory.get("topAnimeJob")
                .incrementer(new RunIdIncrementer())
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Job ratingAnimeJob(@Qualifier("step2") Step step2) {
        return jobBuilderFactory.get("ratingAnimeJob")
                .incrementer(new RunIdIncrementer())
                .flow(step2)
                .end()
                .build();
    }
}
