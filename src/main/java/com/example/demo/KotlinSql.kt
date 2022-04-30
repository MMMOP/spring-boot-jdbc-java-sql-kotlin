package com.example.demo

class KotlinSql {
	companion object {
		var create: String = """
			CREATE TABLE IF NOT EXISTS beers(
				name VARCHAR(100),
				no INTEGER(10000)
			)
		"""

		var insert: String = "INSERT INTO beers VALUES (:name, :no)"


		fun select():String = """
			SELECT 
				name, no 
			FROM beers 
			WHERE no < :no
		""".trimIndent()
	}
}
