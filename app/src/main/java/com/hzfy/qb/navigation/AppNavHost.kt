package com.hzfy.qb.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = QbListDestination.route,
        modifier = modifier
    ) {

        QbNavConfig.navConfig(navController = navController).invoke(this)

    }
}


