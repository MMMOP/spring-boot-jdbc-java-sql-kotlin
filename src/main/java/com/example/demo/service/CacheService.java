package com.example.demo.service;

import com.clevergang.jdbc.FluentNamedParameterJdbcTemplate;
import com.example.demo.model.Item;
import com.example.demo.sql.KotlinSql;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CacheService {

    @Autowired
    private FluentNamedParameterJdbcTemplate jdbcTemplate;

    @Cacheable(value = "ITEM")
    public List<Item> findByNo(Integer no) {
        log.debug("find no : " + no);

        return jdbcTemplate.query(KotlinSql.Companion.select())
                .bind("no", no)
                .fetch(Item.class);
    }

}