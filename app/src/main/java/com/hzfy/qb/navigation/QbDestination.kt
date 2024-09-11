package com.hzfy.qb.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hzfy.common.navigation.Destination
import com.hzfy.common.navigation.navigateSingleTopTo
import com.hzfy.database.entity.QbEntity
import com.hzfy.qb.ui.add.AddQbPage
import com.hzfy.qb.ui.detail.QbDetailPage
import com.hzfy.qb.ui.list.QbListPage
import com.hzfy.qb.ui.torrent.add.AddTorrentPage


abstract class QbDestination(route: String) : Destination(route = route) {
    val argUid = "qb_uid"
    val routeWithQbUid = "${route}/{${argUid}}"
    val qbUidArguments = listOf(
        navArgument(argUid) { type = NavType.IntType }
    )
}

object AddQbDestination : QbDestination(route = "AddQb")
object QbListDestination : QbDestination(route = "QbList")
object AddTorrentDestination : QbDestination(route = "AddTorrent")
object QbDetailsDestination : QbDestination(route = "QbDetails")



object QbNavConfig {

    fun navConfig(
        navController: NavHostController,
    ): NavGraphBuilder.() -> Unit = {
        val openDetailPage = { qb: QbEntity ->
            navController.openQbDetailsPage(qb.uid)
        }
        val openAddQbPage = {
            navController.openAddQbPage()
        }
        val openAddTorrentPage = { qb: QbEntity ->
            navController.openAddTorrentPage(qb.uid)
        }
        val commonOnNavigationBack: () -> Unit = {
            navController.popBackStack()
        }


        composable(route = AddQbDestination.route) {
            AddQbPage(onNavigationBack = commonOnNavigationBack)
        }
        composable(route = QbListDestination.route) {
            QbListPage(
                openDetailPage = openDetailPage,
                openAddQbPage = openAddQbPage,
                onNavigationBack = commonOnNavigationBack
            )
        }
        composable(
            route = QbDetailsDestination.routeWithQbUid,
            arguments = QbDetailsDestination.qbUidArguments,
        ) { navBackStackEntry ->
            val qbUid =
                navBackStackEntry.arguments?.getInt(QbDetailsDestination.argUid)
            QbDetailPage(
                qbUid,
                onNavigationBack = commonOnNavigationBack,
                openAddTorrentPage = openAddTorrentPage
            )
        }

        composable(
            route = AddTorrentDestination.routeWithQbUid,
            arguments = AddTorrentDestination.qbUidArguments,
        ) { navBackStackEntry ->
            val qbUid =
                navBackStackEntry.arguments?.getInt(AddTorrentDestination.argUid)
            AddTorrentPage(qbUid, onNavigationBack = commonOnNavigationBack)
        }

    }



}


private fun NavHostController.openQbDetailsPage(uid: Int) {
    this.navigateSingleTopTo("${QbDetailsDestination.route}/$uid")
}

private fun NavHostController.openAddTorrentPage(qbUid: Int) {
    this.navigateSingleTopTo("${AddTorrentDestination.route}/$qbUid")
}

private fun NavHostController.openAddQbPage() {
    this.navigateSingleTopTo(AddQbDestination.route)
}