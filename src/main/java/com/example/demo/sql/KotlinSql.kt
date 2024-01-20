package com.example.demo.sql

class KotlinSql {
	companion object {
		var create: String = """
			CREATE TABLE IF NOT EXISTS item(
				no INTEGER(10000),
				name VARCHAR(100),
				SNAKE_CASE VARCHAR(100),
				CAMEL_CASE VARCHAR(100)
			);
		"""

		var insert: String = "INSERT INTO item VALUES (:no, :name, :snakeCase, :camelCase)"


		fun select(): String = """
			SELECT 
			    item.no,
				item.name,
				item.SNAKE_CASE,
				item.CAMEL_CASE,
				item_register_status.no_dup,
				item_register_status.name_dup
			FROM item, item_register_status
			WHERE item.no = item_register_status.no_dup
			AND item.no < :no
		"""
	}

}