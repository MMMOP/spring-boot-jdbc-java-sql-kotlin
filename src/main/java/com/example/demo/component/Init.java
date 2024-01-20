package com.example.demo.component;

import com.clevergang.jdbc.FluentNamedParameterJdbcTemplate;
import com.example.demo.model.Item;
import com.example.demo.model.ItemRegisterStatus;
import com.example.demo.repository.ItemRepository;
import com.example.demo.service.CacheService;
import com.example.demo.sql.KotlinSql;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Component;
import th.co.gosoft.rms.mm.pdm.pi.utils.BeanPropertyRowMapperWithDefaultValue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Component
@Slf4j
public class Init implements CommandLineRunner {

    @Autowired
    private FluentNamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public void run(String... args) {
        log.debug("INIT .....................");

        //Create the database table:
        jdbcTemplate.update("DROP TABLE IF EXISTS item").execute();
        jdbcTemplate.update("DROP TABLE IF EXISTS item_register_status").execute();
        jdbcTemplate.update("DROP TRIGGER IF EXISTS item_register_status_after_update;").execute();

        jdbcTemplate.update(KotlinSql.Companion.getCreate()).execute();
        jdbcTemplate.update("""
							CREATE TABLE IF NOT EXISTS item_register_status(
									NO_DUP INTEGER(10000),
									NAME_DUP VARCHAR(100)
							);
					""").execute();
        jdbcTemplate.update("""
							CREATE TRIGGER IF NOT EXISTS item_register_status_after_update AFTER INSERT ON item
								BEGIN
									INSERT INTO item_register_status VALUES (new.no, new.name);
								END;
					""").execute();


        long startTime = System.currentTimeMillis();

        //Insert by normal update:
        IntStream.rangeClosed(1, 1000)
                .forEachOrdered(i -> jdbcTemplate.update(KotlinSql.Companion.getInsert())
                .bind("no", i)
                .bind("name", i % 2 == 1 ? null : "name " + i)
                .bind("snakeCase", "snakeCase " + i)
                .bind("camelCase", "camelCase " + i)
                .execute());

        log.debug("normal execute take time " + ((System.currentTimeMillis() - startTime) / 1000) + "s");

        startTime = System.currentTimeMillis();

        //Insert by batch update:
        List<Item> items = new ArrayList<>();
        for (int i = 1001; i <= 2000; i++) {
            items.add(new Item(i, "name" + 1, "snakeCase" + 1, "camelCase" + 1, null));
        }


        jdbcTemplate.batchUpdate(
                KotlinSql.Companion.getInsert(),
                SqlParameterSourceUtils.createBatch(items));

        log.debug("batch execute take time " + ((System.currentTimeMillis() - startTime) / 1000) + "s");


        //Read records in cache
        for (int i = 0; i < 100; i++) {

            //initiate
            cacheService.findByNo(i);
            //cache
            items = cacheService.findByNo(i);

            //Print read records:
            items.forEach(s -> log.debug(s.toString()));
        }


        String sql = """
						SELECT no_dup, name_dup
						FROM item_register_status
						WHERE no_dup < :no
					""";
        //Read records:
        List<ItemRegisterStatus> itemRegisterStatuses = jdbcTemplate.query(sql)
                .bind("no", 20)
                .fetch(ItemRegisterStatus.class);


        //Print read records:
        itemRegisterStatuses.forEach(s -> log.debug(s.toString()));

        items = jdbcTemplate.query(
                KotlinSql.Companion.select().replace(":no", "20"),
                new BeanPropertyRowMapperWithDefaultValue<>(Item.class)
        );

        items.forEach(s -> log.debug(s.toString()));

        items = itemRepository.findByNameContaining("name1");

        items.forEach(s -> log.debug(s.toString()));
    }
}
