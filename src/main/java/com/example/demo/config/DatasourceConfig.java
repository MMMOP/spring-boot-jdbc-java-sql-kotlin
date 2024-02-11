package com.example.demo.config;

import com.clevergang.jdbc.FluentNamedParameterJdbcTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
@EnableJdbcRepositories(basePackages = "com.example.demo.repository")
//@ProjectColumnCaseFormat(CaseFormat.LOWER_UNDERSCORE)
public class DatasourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSource myDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public NamedParameterJdbcTemplate getJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    //@ConfigurationProperties(prefix = "com.example.demo.repository")
    @Primary
    @Bean
    public FluentNamedParameterJdbcTemplate getFluentJdbcTemplate(NamedParameterJdbcTemplate existingJdbcTemplate) {
        return new FluentNamedParameterJdbcTemplate(existingJdbcTemplate.getJdbcOperations());
    }
}
