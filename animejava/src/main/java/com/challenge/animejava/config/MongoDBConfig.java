package com.challenge.animejava.config;

import com.challenge.animejava.repository.AnimesRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackageClasses = AnimesRepository.class)
@Configuration
public class MongoDBConfig {
}
