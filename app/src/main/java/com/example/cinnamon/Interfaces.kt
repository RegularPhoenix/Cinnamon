package com.example.cinnamon

import com.example.cinnamon.items.IngredientItem
import com.example.cinnamon.items.RecipeItem
import com.example.cinnamon.items.StepItem

interface IOnMainViewItemSelect {
	fun showViewItem(recipeItem: RecipeItem)
}

interface IOnIngredientViewItemSelect {
	fun showViewItem(ingredientItem: IngredientItem)
}

interface IOnStepViewItemSelect {
	fun showViewItem(stepItem: StepItem)
}