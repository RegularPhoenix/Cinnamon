package com.regularphoenix.cinnamon.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.regularphoenix.cinnamon.Recipe

@Dao
interface RecipeDao {
	@Query("SELECT * FROM recipe_database")
	fun getAll(): List<Recipe>

	@Query("SELECT * FROM recipe_database WHERE uid = :uid")
	fun getById(uid: Int): Recipe

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insert(recipe: Recipe)

	@Update
	fun update(recipe: Recipe)

	@Delete
	fun delete(recipe: Recipe)
}