package com.example.demo;

import com.clevergang.jdbc.FluentNamedParameterJdbcTemplate;
import com.example.demo.model.Beer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@SpringBootApplication
public class SpringBootJdbcJavaSqlKotlinApplication {

	@Autowired
	private FluentNamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	private BeerService service;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootJdbcJavaSqlKotlinApplication.class, args);
	}

	@Component
	class Init {

		Init() {
			//Create the database table:
			jdbcTemplate.update("DROP TABLE IF EXISTS beers").execute();
			jdbcTemplate.update(KotlinSql.Companion.getCreate()).execute();


			//Insert many record:
			for (int i = 0; i < 10; i++) {
				jdbcTemplate.update(KotlinSql.Companion.getInsert())
						.bind("name","name " + i)
						.bind("no", i)
						.execute();
			}


			//Read records:
			List<Beer> beers = jdbcTemplate.query(KotlinSql.Companion.select())
					.bind("no", 10)
					.fetch(Beer.class);


			//Print read records:
			beers.forEach(System.out::println);


			//paging data
			System.out.println("\n #### paging #### \n");

			Pageable pageable = PageRequest.of(1, 5,
													Sort.by("name").ascending().and(
													Sort.by("no").descending())
												);
			Page<Beer> beerByPage = service.findBeerByPage(pageable);

			beerByPage.forEach(System.out::println);
		}
	}

	@Bean
	@Primary
	public NamedParameterJdbcTemplate getJdbcTemplate(DataSource dataSource) {
		return new NamedParameterJdbcTemplate(dataSource);
	}

	@Bean
	public FluentNamedParameterJdbcTemplate getFluentJdbcTemplate(NamedParameterJdbcTemplate existingJdbcTemplate) {
		return new FluentNamedParameterJdbcTemplate(existingJdbcTemplate.getJdbcOperations());
	}
}
