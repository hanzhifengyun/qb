package com.hzfy.common.navigation

import androidx.navigation.NavHostController

open class Destination(val route: String)



fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) { launchSingleTop = true }