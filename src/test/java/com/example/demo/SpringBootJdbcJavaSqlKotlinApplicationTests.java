package com.example.demo;

import com.clevergang.jdbc.FluentNamedParameterJdbcTemplate;
import com.example.demo.model.Beer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootJdbcJavaSqlKotlinApplicationTests {

	@Autowired
	private FluentNamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	private BeerService service;

	@Test
	public void test_beer_paging() {

		int count = 10;
		int page = 1;
		int size = 3;
		int totalPages = (int) Math.ceil((double) count / size);
		int contentSize = page + 1 < totalPages ? size : count - size * page;


		Pageable pageable = PageRequest.of(page, size);
		Page<Beer> dataPage = service.findBeerByPage(pageable);

		assertThat((int) dataPage.getTotalElements(), equalTo(count));
		assertThat(dataPage.getTotalPages(), equalTo(totalPages));
		assertThat(dataPage.getContent().size(), equalTo(contentSize));

	}
}