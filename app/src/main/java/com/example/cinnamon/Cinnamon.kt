package com.example.cinnamon

import android.app.Application
import com.example.cinnamon.database.RecipeItemDatabase
import com.example.cinnamon.database.RecipeItemRepository

class Cinnamon: Application() {
	private val database by lazy { RecipeItemDatabase.getDatabase(this) }
	val repository by lazy { RecipeItemRepository(database.mainViewItemDao()) }
}