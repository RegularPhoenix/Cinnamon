package com.regularphoenix.cinnamon.pages

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.regularphoenix.cinnamon.NavigationActions
import com.regularphoenix.cinnamon.NavigationType
import com.regularphoenix.cinnamon.R
import com.regularphoenix.cinnamon.Recipe
import com.regularphoenix.cinnamon.Route
import com.regularphoenix.cinnamon.ui.theme.Tint

@Composable
fun RecipesPage(
	recipes: List<Recipe>,
	actions: NavigationActions,
	navigationType: NavigationType
) {
	Surface(
		modifier = Modifier.fillMaxSize(),
		color = MaterialTheme.colorScheme.background
	) {
		Scaffold(
			/*topBar = {
				CenterAlignedTopAppBar(title = {
					Text(text = "Your recipes")
				})
			},*/

			floatingActionButton = {
				AnimatedVisibility(visible = navigationType == NavigationType.BottomNavigationBar) {
					FloatingActionButton(
						onClick = {
							(actions::navigateTo)(
								Route.RECIPE_NEW
							)
						},
						modifier = Modifier.padding(bottom = 6.dp) // TODO: Colors
					) {
						Icon(
							imageVector = Icons.Filled.Add,
							contentDescription = stringResource(R.string.new_recipe),
							modifier = Modifier.size(18.dp)
						)
					}
				}
			},
		) { contentPadding ->
			// TODO: Search bar
			RecipeList(
				recipes = recipes,
				paddingValues = contentPadding,
				actions = actions
			)
		}
	}
}


@Composable
fun RecipeList(recipes: List<Recipe>, paddingValues: PaddingValues, actions: NavigationActions) {
	LazyVerticalGrid(
		columns = GridCells.Adaptive(minSize = 180.dp),
		modifier = Modifier.fillMaxSize(),
		contentPadding = paddingValues
	) {
		items(recipes) { recipe ->
			RecipeCard(recipe = recipe, actions = actions)
		}
	}
}

@Composable
fun RecipeCard(recipe: Recipe, actions: NavigationActions) {
	Surface(
		shape = MaterialTheme.shapes.small,
		shadowElevation = 2.dp,
		modifier = Modifier
			.fillMaxWidth()
			.aspectRatio(1f)
			.padding(6.dp)
			.clickable { (actions::navigateTo)(Route.RECIPE_NEW + "?uid=${recipe.uid}") }
	) {
		Box {
			Image(
				painter = bitmapToPainter(recipe.image)
					?: painterResource(R.drawable.placeholder), // TODO: Placeholder, find a better image
				contentDescription = null,
				modifier = Modifier.fillMaxSize()
			)

			Surface(
				color = Tint,
				modifier = Modifier
					.fillMaxWidth()
					.size(50.dp) // TODO: Probably scale, but looks fine for now
					.align(Alignment.BottomCenter)
			) {
				Column(
					verticalArrangement = Arrangement.Center,
					modifier = Modifier
						.fillMaxSize()
						.padding(horizontal = 8.dp, vertical = 0.dp)
				) {
					Text(
						text = recipe.name,
						color = Color.White,
						overflow = TextOverflow.Ellipsis,
						maxLines = 1
					)

					Row {
						Text(
							text = recipe.time,
							color = Color.White,
							overflow = TextOverflow.Ellipsis,
							maxLines = 1
						)

						Spacer(modifier = Modifier.width(4.dp))

						Image(
							painter = painterResource(R.drawable.baseline_timelapse_24),
							contentDescription = null,
							modifier = Modifier
								.size(18.dp)
								.align(Alignment.Bottom)
						)
					}
				}
			}
		}
	}
}

fun bitmapToPainter(bitmap: Bitmap?): BitmapPainter? {
	return if (bitmap != null) BitmapPainter(bitmap.asImageBitmap()) else null
}