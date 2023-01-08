package com.example.cinnamon.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.cinnamon.items.RecipeItem
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeItemDao {
	@Query("SELECT * FROM main_table ORDER BY id ASC")
	fun allItems(): Flow<List<RecipeItem>>

	@Query("SELECT * FROM main_table WHERE id = :id")
	fun getItemById(id: Int): Flow<RecipeItem>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertItem(mainItem: RecipeItem)

	@Update
	suspend fun updateItem(mainItem: RecipeItem)

	@Delete
	suspend fun deleteItem(mainItem: RecipeItem)
}