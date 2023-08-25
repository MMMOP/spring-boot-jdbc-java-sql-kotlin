package com.example.demo;

import com.clevergang.jdbc.FluentNamedParameterJdbcTemplate;
import com.example.demo.model.Beer;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;

@SpringBootApplication
public class SpringBootJdbcJavaSqlKotlinApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootJdbcJavaSqlKotlinApplication.class, args);
	}

}

@Configuration
class Config {

	@Bean
	public FluentNamedParameterJdbcTemplate getJdbcTemplate(DataSource dataSource) {
		return new FluentNamedParameterJdbcTemplate(dataSource);
	}
}

@Component
class Init {

	@Autowired
	private FluentNamedParameterJdbcTemplate jdbcTemplate;

	@PostConstruct
	public void process() {
		//Create the database table:
		jdbcTemplate.update("DROP TABLE IF EXISTS beers").execute();
		jdbcTemplate.update(KotlinSql.Companion.getCreate()).execute();


		//Insert many record:
		for (int i = 0; i < 10; i++) {
			jdbcTemplate.update(KotlinSql.Companion.getInsert())
					.bind("name","name " + i)
					.bind("snakeCase","snakeCase " + i)
					.bind("camelCase","camelCase " + i)
					.bind("no", i)
					.execute();
		}


		//Read records:
		List<Beer> beers = jdbcTemplate.query(KotlinSql.Companion.select())
				.bind("no", 10)
				.fetch(Beer.class);


		//Print read records:
		beers.forEach(System.out::println);
	}
}
