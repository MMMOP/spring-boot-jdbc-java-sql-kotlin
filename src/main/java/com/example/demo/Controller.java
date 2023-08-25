package com.example.demo;

import com.clevergang.jdbc.FluentNamedParameterJdbcTemplate;
import com.example.demo.model.Beer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
class Controller {

    @Autowired
    private FluentNamedParameterJdbcTemplate jdbcTemplate;

    @GetMapping("/")
    List<Beer> all() {
        return jdbcTemplate.query(KotlinSql.Companion.select())
                .bind("no", 10)
                .fetch(Beer.class);
    }

}
