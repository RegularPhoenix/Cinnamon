package com.example.cinnamon.database

import androidx.annotation.WorkerThread
import com.example.cinnamon.items.RecipeItem
import kotlinx.coroutines.flow.Flow

class RecipeItemRepository(private val recipeItemDao: RecipeItemDao) {
	val allItems: Flow<List<RecipeItem>> = recipeItemDao.allItems()

	@WorkerThread
	suspend fun insertItem(recipeItem: RecipeItem) = recipeItemDao.insertItem(recipeItem)

	@WorkerThread
	suspend fun updateItem(recipeItem: RecipeItem) = recipeItemDao.updateItem(recipeItem)

	@WorkerThread
	suspend fun deleteItem(recipeItem: RecipeItem) = recipeItemDao.deleteItem(recipeItem)
}