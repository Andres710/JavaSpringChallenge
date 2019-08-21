package com.challenge.animereport;

import com.challenge.animereport.model.Animes;
import com.challenge.animereport.restjob.IdToAnimeProcessor;
import com.challenge.animereport.restjob.LoggingAnimeWriter;
import com.challenge.animereport.restjob.RESTAnimeIdsReader;
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

    private Resource outputResource = new FileSystemResource("output/outputData.csv");

    @Bean
    @StepScope
    ItemReader<Integer> restAnimeIdsReader(RestTemplate restTemplate) {
        return new RESTAnimeIdsReader("urlPruebaaaa", restTemplate);
    }

    @Bean
    FlatFileItemWriter<Animes> writer() {
        FlatFileItemWriter<Animes> writer = new FlatFileItemWriter<>();

        writer.setResource(outputResource);

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
    public Step step1(ItemReader<Integer> restAnimeIdsReader) {

        return stepBuilderFactory.get("step1")
                .<Integer, Animes>chunk(1)
                .reader(restAnimeIdsReader)
                .processor(new IdToAnimeProcessor())
                .writer(writer())
                .build();

    }

    @Bean
    public Job job(@Qualifier("step1") Step step1) {
        return jobBuilderFactory.get("animeJob")
                .incrementer(new RunIdIncrementer())
                .flow(step1)
                .end()
                .build();
    }
}
