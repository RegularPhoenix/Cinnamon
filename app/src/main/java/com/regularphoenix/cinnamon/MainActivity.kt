package com.regularphoenix.cinnamon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.regularphoenix.cinnamon.ui.theme.CinnamonTheme

val recipes: List<Recipe> = listOf(
    Recipe(1, null,"Super Cake", "12 min","","","",""),
    Recipe(2, null,"Some very cool soup with long name", "21 min","","","",""),
    Recipe(3, null,"Fluffy pancakes", "Less than 30 min","","","",""),
    Recipe(4, null,"Lemonade", "1 hour","","","",""),
    Recipe(5, null,"Sandwich", "7 years","","","",""),
    Recipe(6, null,"Spaghetti Linguini", "19 seconds","","","",""),
    Recipe(7, null,"Chocolate", "86 minutes","","","",""),
)

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CinnamonTheme {
                //MainScreen(recipes = RecipeDatabase.RecipeDatabase.getDatabase(LocalContext.current).recipeDao().getAll())
                CinnamonApp(recipes = recipes, calculateWindowSizeClass(this))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true)
@Composable
fun CinnamonCommonPreview() {
    CinnamonTheme {
        CinnamonApp(
            recipes = recipes,
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(400.dp, 900.dp))
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, widthDp = 700, heightDp = 500)
@Composable
fun CinnamonTabletPreview() {
    CinnamonTheme {
        CinnamonApp(
            recipes = recipes,
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(700.dp, 500.dp))
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, widthDp = 500, heightDp = 700)
@Composable
fun CinnamonTabletPortraitPreview() {
    CinnamonTheme {
        CinnamonApp(
            recipes = recipes,
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(500.dp, 700.dp))
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, widthDp = 1280, heightDp = 720)
@Composable
fun CinnamonHDPreview() {
    CinnamonTheme {
        CinnamonApp(
            recipes = recipes,
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(1280.dp, 720.dp))
        )
    }
}
