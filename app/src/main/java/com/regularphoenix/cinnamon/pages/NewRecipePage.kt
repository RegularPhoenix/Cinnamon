package com.regularphoenix.cinnamon.pages

import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.regularphoenix.cinnamon.R
import com.regularphoenix.cinnamon.Recipe
import com.regularphoenix.cinnamon.database.RecipeDatabase
import com.regularphoenix.cinnamon.ui.theme.CinnamonTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewRecipePage(uid: Int?) {
	Surface(
		color = MaterialTheme.colorScheme.background,
		modifier = Modifier.fillMaxSize()
	) {
		Scaffold(
			topBar = {
				CenterAlignedTopAppBar(title = {
					Text(stringResource(id = R.string.recipe_editor))
				})
			}
		) { contentPadding ->
			NewRecipePageContent(contentPadding, uid)
		}
	}
}

@Composable
fun NewRecipePageContent(contentPadding: PaddingValues, uid: Int?) {
	val recipe: Recipe? =
		if (uid != null)
			RecipeDatabase.RecipeDatabase.getDatabase(LocalContext.current).recipeDao().getById(uid)
		else null

	val state = remember { mutableStateOf<Bitmap?>(null) }
	val context = LocalContext.current
	val errorText = stringResource(id = R.string.image_pick_error)
	val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
		if (bitmap != null) {
			state.value = bitmap
		} else {
			Toast.makeText(context, errorText, Toast.LENGTH_SHORT).show()
		}
	}

	Column(
		modifier = Modifier.padding(contentPadding)
	) {
		val newRecipeText = recipe?.name ?: stringResource(id = R.string.new_recipe)
		var text by remember {
			mutableStateOf(
				TextFieldValue(newRecipeText)
			)
		}

		OutlinedTextField(
			value = text,
			label = { Text(stringResource(id = R.string.recipe_name)) },
			onValueChange = { text = it },
			modifier = Modifier
				.fillMaxWidth()
				.padding(6.dp)
		)

		Surface(
			shape = MaterialTheme.shapes.small,
			shadowElevation = 2.dp,
			onClick = { pickMedia.launch() },
			modifier = Modifier
				.padding(6.dp)
				.aspectRatio(1f)
				.fillMaxWidth()
		) {
			Box {
				Image(
					painter = bitmapToPainter(state.value)
						?: painterResource(id = R.drawable.placeholder),
					contentDescription = null,
					modifier = Modifier.fillMaxSize()
				)
			}
		}

		Text(
			text = "Ingredients",
			fontWeight = FontWeight.Bold,
			fontStyle = FontStyle.Italic,
			fontSize = 18.sp,
			modifier = Modifier.padding(8.dp)
		)

		OutlinedButton(
			onClick = { /*TODO*/ },
			modifier = Modifier
				.padding(8.dp)
				.fillMaxWidth()
		) {
			Text(text = "Add ingredient")
		}

		Text(
			text = "Preparation",
			fontWeight = FontWeight.Bold,
			fontStyle = FontStyle.Italic,
			fontSize = 18.sp,
			modifier = Modifier.padding(8.dp)
		)

		StepCard()

		OutlinedButton(
			onClick = { /*TODO*/ },
			modifier = Modifier
				.padding(8.dp)
				.fillMaxWidth()
		) {
			Text(text = "Add step")
		}
	}
}

@Composable
fun Ingredient() {

}

@Composable
fun StepCard() {
	Card(
		modifier = Modifier
			.padding(8.dp)
			.fillMaxWidth()
	) {
		Row(modifier = Modifier.padding(6.dp)) {
			Text(text = "1")
			Text(text = "Some very long description to test if it stretches correctly",
				modifier = Modifier.padding(horizontal = 6.dp))
		}
	}
}

@Preview(showBackground = true)
@Composable
fun CinnamonCommonPreview() {
	CinnamonTheme {
		NewRecipePage(uid = null)
	}
}