package com.example.demo.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
@Builder
@Data
@ToString
public class Beer{
	private String name;
	private Integer no;
}
