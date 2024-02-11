package com.example.demo.sql

class KotlinSql {
	companion object {
		var create: String = """
			CREATE TABLE IF NOT EXISTS item(
				ITEM_NO INTEGER(10000) NOT NULL,
				EFFECTIVE_START_DATE CHAR(10),
				NAME VARCHAR(100),
				SNAKE_CASE VARCHAR(100),
				CAMEL_CASE VARCHAR(100),
				PRIMARY KEY ( ITEM_NO)
			);
		"""

		var insert: String = "INSERT INTO item VALUES (:itemNo, :effectiveStartDate, :name, :snakeCase, :camelCase)"


		fun select(): String = """
			SELECT 
			    item.item_no,
			    item.EFFECTIVE_START_DATE,
				item.name,
				item.SNAKE_CASE,
				item.CAMEL_CASE,
				item_register_status.no_dup,
				item_register_status.name_dup
			FROM item, item_register_status
			WHERE item.item_no = item_register_status.no_dup
			AND item.item_no < :no
		"""
	}

}