package com.regularphoenix.cinnamon

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun CinnamonApp(
	recipes: List<Recipe>,
	windowSizeClass: WindowSizeClass
) {
	val navigationType=
		if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Medium
			|| windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded)
			NavigationType.NavigationRail
		else
			NavigationType.BottomNavigationBar

	val navController = rememberNavController()
	val navigationActions = remember(navController) {
		NavigationActions(navController)
	}
	val navBackStackEntry by navController.currentBackStackEntryAsState()
	val selected = navBackStackEntry?.destination?.route ?: Route.HOME

	Row(modifier = Modifier.fillMaxSize()) {
		AnimatedVisibility(visible = navigationType == NavigationType.NavigationRail) {
			CinnamonNavigationRail(selected, navigationActions)
		}

		Column(
			modifier = Modifier
				.fillMaxSize()
				.background(MaterialTheme.colorScheme.inverseOnSurface)
		) {
			CinnamonNavigationHost(recipes, navController, navigationActions, navigationType, Modifier.weight(1f))

			AnimatedVisibility(visible = navigationType == NavigationType.BottomNavigationBar) {
				CinnamonBottomNavigationBar(selected, navigationActions)
			}
		}
	}
}