package com.example.demo.component;

import com.clevergang.jdbc.FluentNamedParameterJdbcTemplate;
import com.example.demo.model.Item;
import com.example.demo.model.ItemRegisterStatus;
import com.example.demo.model.PmaInfo;
import com.example.demo.repository.ItemRepository;
import com.example.demo.service.CacheService;
import com.example.demo.sql.KotlinSql;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import th.co.gosoft.rms.mm.pdm.pi.utils.BeanPropertyRowMapperWithDefaultValue;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        jdbcTemplate.update("DROP TABLE IF EXISTS pma_info").execute();
        jdbcTemplate.update("DROP TRIGGER IF EXISTS item_register_status_after_update;").execute();

        jdbcTemplate.update(KotlinSql.Companion.getCreate()).execute();
        jdbcTemplate.update("""
							CREATE TABLE IF NOT EXISTS item_register_status(
									NO_DUP INTEGER(10000),
									NAME_DUP VARCHAR(100)
							);
					""").execute();
        jdbcTemplate.update("""
                             CREATE TABLE IF NOT EXISTS pma_info (
                                    PMA_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                                    PMA_CODE CHAR(10)
                             );
                    """).execute();
        jdbcTemplate.update("""
							CREATE TRIGGER IF NOT EXISTS item_register_status_after_update AFTER INSERT ON item
								BEGIN
									INSERT INTO item_register_status VALUES (new.item_no, new.name);
								END;
					""").execute();


        long startTime = System.currentTimeMillis();

        //Insert by normal update:
        IntStream.rangeClosed(1, 10)
                .forEachOrdered(i -> jdbcTemplate.update(KotlinSql.Companion.getInsert())
                .bind("itemNo", i)
                .bind("effectiveStartDate", String.valueOf(i))
                .bind("name", i % 2 == 1 ? null : "name " + i)
                .bind("snakeCase", "snakeCase " + i)
                .bind("camelCase", "camelCase " + i)
                .execute());

        log.debug("normal execute take time " + ((System.currentTimeMillis() - startTime) / 1000) + "s");

        startTime = System.currentTimeMillis();

        //Insert by batch update:
        List<Item> items = new ArrayList<>();
        for (int i = 101; i <= 200; i++) {
            items.add(new Item(i, String.valueOf(i),"name" + 1, "snakeCase" + 1, "camelCase" + 1));
        }

        jdbcTemplate.batchUpdate(
                "INSERT INTO item VALUES (:itemNo, :effectiveStartDate, :name, :snakeCase, :camelCase)",
                SqlParameterSourceUtils.createBatch(items));

        log.debug("batch insert execute take time " + ((System.currentTimeMillis() - startTime) / 1000) + "s");


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

        items = jdbcTemplate.query(KotlinSql.Companion.select())
                            .bind("no", 20)
                            .fetch(new BeanPropertyRowMapperWithDefaultValue<>(Item.class) {
                                public Item mapRow(ResultSet rs, int rowNum) throws SQLException{
                                    Item item = super.mapRow(rs, rowNum);
                                    assert item != null;
                                    item.setEffectiveStartDate("longdo");
                                    return item;
                                }
                            });


        items.forEach(s -> log.debug(s.toString()));

        items = itemRepository.findByNameContaining("name1");

        items.forEach(s -> log.debug(s.toString()));

        List<PmaInfo> pmaInfo = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            pmaInfo.add(new PmaInfo(null , "name" + i));
        }

        KeyHolder keyHolder = new GeneratedKeyHolder(new ArrayList<>());
        jdbcTemplate.batchUpdate(
                "INSERT INTO pma_info ( pma_code) VALUES ( :pmaCode);",
                SqlParameterSourceUtils.createBatch(pmaInfo),
                keyHolder, new String[]{"PMA_ID"});

        Objects.requireNonNull(keyHolder.getKeyList())
                .forEach(stringObjectMap -> stringObjectMap.forEach((key, value) -> {
                    System.out.println(key + " = " + value);
                }));

        //Read records:
        jdbcTemplate.query("SELECT * FROM pma_info")
                .fetch(PmaInfo.class)
                .forEach(s -> log.debug(s.toString()));

        itemRepository.save(new Item(99999, "OK","name", "snakeCase", "camelCase"));
    }
}
