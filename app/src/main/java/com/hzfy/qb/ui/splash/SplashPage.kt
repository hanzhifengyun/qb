package com.hzfy.qb.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hzfy.library.log.HzfyLog
import com.hzfy.qb.R
import com.hzfy.ui.compose.text.OnPrimaryBodyMediumText
import com.hzfy.ui.theme.AppTheme
import com.hzfy.ui.theme.shapes
import dev.shreyaspatil.permissionFlow.utils.launch
import dev.shreyaspatil.permissionflow.compose.rememberMultiplePermissionState
import dev.shreyaspatil.permissionflow.compose.rememberPermissionFlowRequestLauncher

@Composable
fun SplashPage(viewModel: SplashViewModel = hiltViewModel(), onTimeout: () -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentOnTimeout by rememberUpdatedState(onTimeout)

    val permissionLauncher = rememberPermissionFlowRequestLauncher()
    LaunchedEffect(Unit) {
        viewModel.onStart()
        permissionLauncher.launch(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_PHONE_STATE
        )

    }

    LaunchedEffect(uiState) {
        if (uiState.checkPermissions) {
            permissionLauncher.launch(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_PHONE_STATE
            )
        }
        HzfyLog.i("uiState.openHomePage = ${uiState.openHomePage}")
        if (uiState.openHomePage) {
            currentOnTimeout()
        }
    }

    MultiplePermission(onPermissionAllGranted = { viewModel.onPermissionAllGranted() })



    SplashPageContent(uiState = uiState, onJumpClick = { viewModel.onJumpClick() })
}

@Composable
fun MultiplePermission(onPermissionAllGranted: () -> Unit) {
    val state by rememberMultiplePermissionState(
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_PHONE_STATE,
    )

    LaunchedEffect(state.allGranted) {
        if (state.allGranted) {
            // Render something
            onPermissionAllGranted()
        }
    }

//    val grantedPermissions = state.grantedPermissions
    // Do something with `grantedPermissions`

//    val deniedPermissions = state.deniedPermissions
    // Do something with `deniedPermissions`

//    val permissionsRequiringRationale = state.permissionsRequiringRationale
    // Do something with `permissionsRequiringRationale`
}


@Composable
fun SplashPageContent(uiState: SplashUIState, onJumpClick: () -> Unit) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary)
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(16.dp)
    ) {
        val (text, icon) = createRefs()

        Surface(
            shape = shapes.large,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .clickable { onJumpClick() }
                .constrainAs(text) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
        ) {
            OnPrimaryBodyMediumText(
                text = stringResource(
                    id = R.string.splash_countdown_text,
                    uiState.countdownSeconds
                ),
                modifier = Modifier.padding(10.dp)
            )
        }



        Image(
            alignment = Alignment.Center,
            painter = painterResource(id = R.mipmap.ic_qb_logo),
            contentDescription = "",
            modifier = Modifier
                .height(100.dp)
                .width(100.dp)
                .constrainAs(icon) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }

        )
    }
}


@Preview(showBackground = true, name = "SplashPage")
@Composable
fun PagePreview() {
    AppTheme {
        SplashPageContent(SplashUIState(), onJumpClick = {})
    }
}