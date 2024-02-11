package com.example.demo.component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JacksonXmlInit implements CommandLineRunner {

    @Bean
    @Primary
    public ObjectMapper primaryMapper() {
        return new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Bean("camelCaseMapper")
    public ObjectMapper camelCaseMapper() {
        return new ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Bean("snakeCaseMapper")
    public ObjectMapper snakeCaseMapper() {
        return new ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategies.UPPER_SNAKE_CASE)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Autowired
    @Qualifier("camelCaseMapper")
    ObjectMapper camelCaseMapper;

    @Autowired
    @Qualifier("snakeCaseMapper")
    ObjectMapper snakeCaseMapper;

    @Override
    public void run(String... args) throws JsonProcessingException {
        log.info(camelCaseMapper.writeValueAsString(new User()));
        log.info(snakeCaseMapper.writeValueAsString(new User()));
    }
}

@Data
class User {
    private String firstName = "test do";
    private String lastName = "test do";
}
