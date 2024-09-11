package com.hzfy.common.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hzfy.common.R
import com.hzfy.common.jetpack.BaseViewModel
import com.hzfy.common.jetpack.EmptyViewModel
import com.hzfy.ui.compose.CenterAlignedTopBar
import com.hzfy.ui.compose.TopBarConfig
import com.hzfy.ui.theme.AppTheme

@Composable
private fun <T : BaseViewModel> BasePage(
    onCloseCurrentPage: () -> Unit,
    needTopBar: Boolean = true,
    title: String = "",
    topBarConfig: TopBarConfig = TopBarConfig.DEFAULT,
    getViewModel: @Composable () -> T,
    content: @Composable (T, SnackbarHostState) -> Unit
) {
    val viewModel: T = getViewModel()
    val currentOnCloseCurrentPage by rememberUpdatedState(onCloseCurrentPage)
    val baseUIState by viewModel.baseUiState.collectAsStateWithLifecycle()
    val snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.onStart()
    }

    LaunchedEffect(baseUIState) {
        if (baseUIState.needShowNetworkError) {
            snackbarHostState.showSnackbar(context.getString(R.string.common_network_error))
        }
        if (baseUIState.needShowMessage) {
            snackbarHostState.showSnackbar(baseUIState.message)
        }
        if (baseUIState.needCloseCurrentPage) {
            currentOnCloseCurrentPage()
        }
    }

    Scaffold(
        topBar = {
            if (needTopBar) {
                CenterAlignedTopBar(
                    title = title,
                    topBarConfig = topBarConfig
                )
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },

        ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            content(viewModel, snackbarHostState)
            baseUIState.showLoading?.let { showLoading ->
                if (showLoading) {
                    LoadingPage()
                }
            }
        }

    }
}

@Composable
fun <T : BaseViewModel> NoTopBarPage(
    onCloseCurrentPage: () -> Unit,
    getViewModel: @Composable () -> T,
    content: @Composable (T, SnackbarHostState) -> Unit
) {
    BasePage(
        onCloseCurrentPage = onCloseCurrentPage,
        needTopBar = false,
        getViewModel = getViewModel
    ) { viewModel, snackbarHostState ->
        content(viewModel, snackbarHostState)
    }
}

@Composable
fun <T : BaseViewModel> TopBarPage(
    onCloseCurrentPage: () -> Unit,
    title: String,
    topBarConfig: TopBarConfig = TopBarConfig.DEFAULT,
    getViewModel: @Composable () -> T,
    content: @Composable (T, SnackbarHostState) -> Unit
) {
    BasePage(
        onCloseCurrentPage = onCloseCurrentPage,
        title = title,
        topBarConfig = topBarConfig,
        getViewModel = getViewModel
    ) { viewModel, snackbarHostState ->
        content(viewModel, snackbarHostState)
    }
}


@Preview(showBackground = true, name = "BasePage")
@Composable
fun BasePagePreview() {
    AppTheme {
        BasePage<EmptyViewModel>(
            onCloseCurrentPage = { },
            getViewModel = { EmptyViewModel() },
            title = "Home"
        ) { _, _ ->
        }
    }
}

