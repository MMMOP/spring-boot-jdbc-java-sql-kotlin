package com.example.demo;

import com.clevergang.jdbc.FluentNamedParameterJdbcTemplate;
import com.example.demo.model.Beer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BeerService {
    @Autowired
    private FluentNamedParameterJdbcTemplate jdbcTemplate;

    public List<Beer> findBeer() {
        String querySql = "SELECT name, no " +
                "FROM beers " +
                "WHERE no < :no";
        return jdbcTemplate.query(querySql)
                .bind("no", 10)
                .fetch(Beer.class);
    }

    public Page<Beer> findBeerByPage(Pageable pageable) {

        String rowCountSql = "SELECT count(1) AS result " +
                "FROM beers " +
                "WHERE no < :no";
        int total = jdbcTemplate.query(rowCountSql)
                        .bind("no", 10)
                        .fetchOne((rs, rowNum) -> rs.getInt(1));

        Sort sort = pageable.getSort();

        String order = StringUtils.collectionToCommaDelimitedString(
                        StreamSupport.stream(sort.spliterator(), false)
                        .map(o -> o.getProperty() + " " + o.getDirection())
                        .collect(Collectors.toList()));

        String querySql = "SELECT name, no " +
                "FROM beers " +
                "WHERE no < :no " +
                "ORDER BY :order " +
                "LIMIT :limit " +
                "OFFSET :offset";
        List<Beer> Beers = jdbcTemplate.query(querySql)
                                .bind("no", 10)
                                .bind("order", order)
                                .bind("limit", pageable.getPageSize())
                                .bind("offset", pageable.getOffset())
                                .fetch(Beer.class);

        return new PageImpl<>(Beers, pageable, total);
    }
}