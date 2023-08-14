package com.regularphoenix.cinnamon

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe_database")
data class Recipe(
	@PrimaryKey(autoGenerate = true) var uid: Int,
	// General info or card info
	@ColumnInfo(name = "image") var image: Bitmap?,
	@ColumnInfo(name = "name") var name: String,
	@ColumnInfo(name = "time") var time: String, // TODO: Change to datetime?
	// Ingredients info
	@ColumnInfo(name = "ingredients_names") var ingredientsNames: String,
	@ColumnInfo(name = "ingredients_amounts") var ingredientsAmounts: String,
	// Steps info
	@ColumnInfo(name = "steps_descriptions") var stepsDescriptions: String,
	@ColumnInfo(name = "steps_times") var stepsTimes: String
)