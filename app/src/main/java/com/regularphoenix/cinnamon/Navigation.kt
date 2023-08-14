package com.regularphoenix.cinnamon

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.regularphoenix.cinnamon.pages.NewRecipePage
import com.regularphoenix.cinnamon.pages.RecipesPage

enum class NavigationType {
	BottomNavigationBar,
	NavigationRail
}

enum class LayoutType {
	Header,
	Content
}

object Route {
	const val HOME = "Home"
	const val RECIPE_NEW = "Recipe_new"
	const val TODO = "Todo"
}

data class TopLevelDestination(
	val route: String,
	val icon: ImageVector,
	val textId: Int
)

val TopLevelDestinations = listOf(
	TopLevelDestination(
		Route.HOME,
		Icons.Default.Home,
		R.string.home_page
	),

	TopLevelDestination(
		Route.TODO,
		Icons.Default.Done,
		R.string.todo_page
	)
)

class NavigationActions(private val navController: NavHostController) {
	fun navigateToTopLevel(destination: TopLevelDestination) {
		navigateTo(destination.route)
	}

	fun navigateTo(destination: String) {
		navController.popBackStack() // TODO: I guess this is necessary only when recipe editor is opened

		Log.d("NavLog", "Navigating to: ${navController.currentBackStackEntry?.destination?.route ?: "Nowhere?"}")
		navController.navigate(destination) {
			popUpTo(navController.graph.findStartDestination().id) {
				saveState = true
			}
			launchSingleTop = true
			restoreState = true
		}
	}
}

@Composable
fun CinnamonBottomNavigationBar(
	selected: String,
	actions: NavigationActions
) {
	NavigationBar(modifier = Modifier.fillMaxWidth()) {
		TopLevelDestinations.forEach { destination ->
			NavigationBarItem(
				selected = selected == destination.route,
				onClick = { (actions::navigateToTopLevel)(destination) },
				icon = {
					Icon(
						imageVector = destination.icon,
						contentDescription = stringResource(destination.textId)
					)
				}
			)
		}
	}
}

@Composable
fun CinnamonNavigationRail(
	selected: String,
	actions: NavigationActions
) {
	NavigationRail(
		containerColor = MaterialTheme.colorScheme.inverseOnSurface,
		modifier = Modifier.fillMaxHeight()
	) {
		Layout(
			modifier = Modifier.widthIn(max = 80.dp),
			measurePolicy = navigationMeasurePolicy(),
			content = {
				Column(
					horizontalAlignment = Alignment.CenterHorizontally,
					verticalArrangement = Arrangement.spacedBy(4.dp),
					modifier = Modifier
						.layoutId(LayoutType.Header)
						.fillMaxWidth()
				) {
					AnimatedVisibility(visible = selected == Route.HOME) {
						FloatingActionButton(
							onClick = {
								(actions::navigateTo)(
									Route.RECIPE_NEW
								)
							},
							shape = CircleShape,
							modifier = Modifier.padding(top = 8.dp, bottom = 32.dp) // TODO: Colors
						) {
							Icon(
								imageVector = Icons.Filled.Add,
								contentDescription = stringResource(R.string.new_recipe),
								modifier = Modifier.size(18.dp)
							)
						}
					}
				}

				Column(
					horizontalAlignment = Alignment.CenterHorizontally,
					verticalArrangement = Arrangement.spacedBy(4.dp),
					modifier = Modifier.layoutId(LayoutType.Content)
				) {
					TopLevelDestinations.forEach { destination ->
						NavigationRailItem(
							selected = selected == destination.route,
							onClick = { (actions::navigateToTopLevel)(destination) },
							icon = {
								Icon(
									imageVector = destination.icon,
									contentDescription = stringResource(destination.textId)
								)
							}
						)
					}
				}
			}
		)
	}
}

fun navigationMeasurePolicy(): MeasurePolicy {
	return MeasurePolicy {measurables, constraints ->
		lateinit var headerMeasurable: Measurable
		lateinit var contentMeasurable: Measurable

		measurables.forEach {
			when (it.layoutId) {
				LayoutType.Header -> headerMeasurable = it
				LayoutType.Content -> contentMeasurable = it
				else -> error("Unknown layout")
			}
		}

		val headerPlaceable = headerMeasurable.measure(constraints)
		val contentPlaceable = contentMeasurable.measure(
			constraints.offset(vertical = -headerPlaceable.height)
		)

		layout(constraints.maxWidth, constraints.maxHeight) {
			headerPlaceable.placeRelative(0, 0)
			val nonContentVerticalSpace = constraints.maxHeight - contentPlaceable.height
			val contentPlaceableY = (nonContentVerticalSpace / 2).coerceAtLeast(headerPlaceable.height)

			contentPlaceable.placeRelative(0, contentPlaceableY)
		}
	}
}

@Composable
fun CinnamonNavigationHost(
	recipes: List<Recipe>,
	navController: NavHostController,
	actions: NavigationActions,
	navigationType: NavigationType,
	modifier: Modifier
) {
	NavHost(
		navController = navController,
		startDestination = Route.HOME,
		modifier = modifier
	) {
		composable(Route.HOME) {
			RecipesPage(recipes, actions, navigationType)
		}

		composable(
			Route.RECIPE_NEW + "?uid={uid}",
			arguments = listOf(navArgument("uid") { type = NavType.IntType; defaultValue = -1 })
		) {
			NewRecipePage(it.arguments?.getInt("uid"))
		}

		composable(Route.TODO) {
			// TODO
		}
	}
}