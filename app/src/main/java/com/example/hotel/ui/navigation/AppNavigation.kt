package com.example.hotel.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hotel.ui.screens.HomeScreen
import com.example.hotel.ui.screens.PlaceDetailScreen
import com.example.hotel.ui.screens.SignInScreen
import com.example.hotel.ui.screens.SignUpScreen
import com.example.hotel.ui.screens.SplashScreen
import com.example.hotel.ui.screens.WelcomeScreen

/**
 * Navigation routes for the app
 */
object AppDestinations {
    const val SPLASH_ROUTE = "splash"
    const val WELCOME_ROUTE = "welcome"
    const val SIGN_UP_ROUTE = "sign_up"
    const val SIGN_IN_ROUTE = "sign_in"
    const val HOME_ROUTE = "home"
    const val PLACE_DETAIL_ROUTE = "place_detail/{placeId}"

    // Helper function to create place detail route with place ID
    fun placeDetailRoute(placeId: String): String {
        return "place_detail/$placeId"
    }
}

/**
 * Main navigation component for the app
 */
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = AppDestinations.SPLASH_ROUTE
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Splash screen
        composable(AppDestinations.SPLASH_ROUTE) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(AppDestinations.WELCOME_ROUTE) {
                        popUpTo(AppDestinations.SPLASH_ROUTE) { inclusive = true }
                    }
                }
            )
        }

        // Welcome screen
        composable(AppDestinations.WELCOME_ROUTE) {
            WelcomeScreen(
                onGetStartedClick = {
                    navController.navigate(AppDestinations.SIGN_UP_ROUTE)
                },
                onSignInClick = {
                    navController.navigate(AppDestinations.SIGN_IN_ROUTE)
                }
            )
        }

        // Sign up screen
        composable(AppDestinations.SIGN_UP_ROUTE) {
            SignUpScreen(
                onSignInClick = {
                    navController.navigate(AppDestinations.SIGN_IN_ROUTE) {
                        popUpTo(AppDestinations.SIGN_UP_ROUTE) { inclusive = true }
                    }
                },
                onSignUpSuccess = {
                    navController.navigate(AppDestinations.HOME_ROUTE) {
                        popUpTo(AppDestinations.WELCOME_ROUTE) { inclusive = true }
                    }
                }
            )
        }

        // Sign in screen
        composable(AppDestinations.SIGN_IN_ROUTE) {
            SignInScreen(
                onSignUpClick = {
                    navController.navigate(AppDestinations.SIGN_UP_ROUTE) {
                        popUpTo(AppDestinations.SIGN_IN_ROUTE) { inclusive = true }
                    }
                },
                onSignInSuccess = {
                    navController.navigate(AppDestinations.HOME_ROUTE) {
                        popUpTo(AppDestinations.WELCOME_ROUTE) { inclusive = true }
                    }
                }
            )
        }

        // Home screen
        composable(AppDestinations.HOME_ROUTE) {
            HomeScreen(
                onSignOut = {
                    navController.navigate(AppDestinations.WELCOME_ROUTE) {
                        popUpTo(AppDestinations.HOME_ROUTE) { inclusive = true }
                    }
                },
                onPlaceClick = { placeId ->
                    navController.navigate(AppDestinations.placeDetailRoute(placeId))
                }
            )
        }

        // Place detail screen
        composable(
            route = AppDestinations.PLACE_DETAIL_ROUTE
        ) { backStackEntry ->
            val placeId = backStackEntry.arguments?.getString("placeId") ?: ""
            PlaceDetailScreen(
                placeId = placeId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
