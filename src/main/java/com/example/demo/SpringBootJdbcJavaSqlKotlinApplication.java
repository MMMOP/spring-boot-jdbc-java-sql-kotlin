package com.example.demo;

import com.example.demo.model.Beer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class SpringBootJdbcJavaSqlKotlinApplication {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootJdbcJavaSqlKotlinApplication.class, args);
	}

	@Component
	class Init {

		Init() {
			//Create the database table:
			jdbcTemplate.execute("DROP TABLE IF EXISTS beers");
			jdbcTemplate.execute(KotlinSql.Companion.getCreate());


			//Insert many record:
			Map<String, Object> parameters = new HashMap<>();
			for (int i = 0; i < 10; i++) {
				parameters.put("name","name " + i);
				parameters.put("no", i);
				namedParameterJdbcTemplate.update(KotlinSql.Companion.getInsert(), parameters);
			}


			//Read records:
			List<Beer> beers = namedParameterJdbcTemplate.query(KotlinSql.Companion.select(),
				new MapSqlParameterSource()
					.addValue("no", 10),
				(resultSet, rowNum) -> new Beer(
					resultSet.getString("name"),
					resultSet.getInt("no")
				));


			//Print read records:
			beers.forEach(System.out::println);
		}
	}
}
