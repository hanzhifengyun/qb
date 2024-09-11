package com.hzfy.qb.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hzfy.qb.navigation.AppNavHost

@Composable
fun HzfyPage() {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    // Fetch your currentDestination:
    val currentDestination = currentBackStack?.destination

    // Change the variable to this and use Overview as a backup screen if this returns null
//    val currentScreen =
//        rallyTabRowScreens.find { it.route == currentDestination?.route } ?: Overview
    AppNavHost(
        navController = navController,
    )
}