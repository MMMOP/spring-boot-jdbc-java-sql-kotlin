package com.example.demo

class KotlinSql {
	companion object {
		var create: String = """
			CREATE TABLE IF NOT EXISTS beers(
				name VARCHAR(100),
				no INTEGER(10000),
				SNAKE_CASE VARCHAR(100),
				CAMEL_CASE VARCHAR(100)
			)
		"""

		var insert: String = "INSERT INTO beers VALUES (:name, :no, :snakeCase, :camelCase)"


		fun select():String = """
			SELECT 
				name, no, SNAKE_CASE, CAMEL_CASE
			FROM beers 
			WHERE no < :no
		"""
	}
}
