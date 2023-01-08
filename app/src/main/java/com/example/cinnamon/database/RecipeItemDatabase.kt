package com.example.cinnamon.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cinnamon.items.RecipeItem

@Database(entities = [RecipeItem::class], version = 1, exportSchema = false)
abstract class RecipeItemDatabase: RoomDatabase() {
	abstract fun mainViewItemDao(): RecipeItemDao

	companion object {
		@Volatile
		private var INSTANCE: RecipeItemDatabase? = null

		fun getDatabase(context: Context): RecipeItemDatabase {
			return INSTANCE ?: synchronized(this) {
				val instance = Room.databaseBuilder(context.applicationContext, RecipeItemDatabase::class.java, "main_database").build()
				INSTANCE = instance
				instance
			}
		}
	}
}