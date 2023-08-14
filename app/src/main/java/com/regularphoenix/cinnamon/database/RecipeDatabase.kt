package com.regularphoenix.cinnamon.database

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.regularphoenix.cinnamon.Recipe
import java.io.ByteArrayOutputStream

class Converters {
	@TypeConverter
	fun bitmapToByteArray(value: Bitmap?): ByteArray? {
		val stream = ByteArrayOutputStream()
		value?.compress(Bitmap.CompressFormat.PNG, 100, stream)
		return stream.toByteArray()
	}

	@TypeConverter
	fun byteArrayToBitmap(value: ByteArray?): Bitmap? {
		return BitmapFactory.decodeByteArray(value, 0, value?.size ?: 0)
	}
}

class RecipeDatabase {
	@Database(entities = [Recipe::class], version = 1)
	@TypeConverters(Converters::class)
	abstract class RecipeDatabase: RoomDatabase() {
		abstract fun recipeDao(): RecipeDao

		companion object { // TODO: Create and keep an instance
			fun getDatabase(context: Context): RecipeDatabase {
				return Room.databaseBuilder(context.applicationContext, RecipeDatabase::class.java, "recipe_database").allowMainThreadQueries().build() // TODO: Remove main thread query?
			}
		}
	}
}