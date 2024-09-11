package com.hzfy.qb.ui.add

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hzfy.common.compose.TopBarPage
import com.hzfy.qb.R
import com.hzfy.ui.compose.MediumVerticalSpacer
import com.hzfy.ui.compose.OutlinedInputText
import com.hzfy.ui.compose.PrimaryElevatedButton
import com.hzfy.ui.compose.TopBarConfig
import com.hzfy.ui.theme.AppTheme

@Composable
fun AddQbPage(onNavigationBack: () -> Unit) {
    TopBarPage<AddQbViewModel>(
        onCloseCurrentPage = onNavigationBack,
        title = stringResource(id = R.string.qb_title_add),
        topBarConfig = TopBarConfig.DEFAULT.copy(
            showLeftIcon = true,
            onLeftIconClick = onNavigationBack
        ),
        getViewModel = { hiltViewModel() }) { viewModel, snackbarHostState ->

        val context = LocalContext.current
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        LaunchedEffect(uiState) {
            if (uiState.saveQbSuccess) { 
                snackbarHostState.showSnackbar(context.getString(R.string.qb_add_success))
            }

            if (uiState.saveQbError) {
                snackbarHostState.showSnackbar(context.getString(R.string.qb_save_error))
            }
        }
        ContentScreen(
            uiState = uiState,
            onEditUrlTextChanged = { viewModel.onEditUrlTextChanged(it) },
            onEditUsernameTextChanged = { viewModel.onEditUsernameTextChanged(it) },
            onEditPasswordTextChanged = { viewModel.onEditPasswordTextChanged(it) },
            onBtnConfirmClick = { viewModel.onBtnConfirmClick() }
        )
    }
}


@Composable
private fun ContentScreen(
    uiState: AddQbUIState,
    onEditUrlTextChanged: (String) -> Unit = {},
    onEditUsernameTextChanged: (String) -> Unit = {},
    onEditPasswordTextChanged: (String) -> Unit = {},
    onBtnConfirmClick: () -> Unit = {},
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Image(painter = painterResource(R.mipmap.ic_qb_logo), contentDescription = null)
        MediumVerticalSpacer()
        OutlinedInputText(
            label = stringResource(id = R.string.qb_hint_url),
            text = uiState.url,
            singleLine = true,
            isError = uiState.urlEmptyError,
            supportingText = stringResource(id = R.string.qb_hint_url_error),
            onValueChange = { onEditUrlTextChanged(it) },
            modifier = Modifier.fillMaxWidth()
        )
        MediumVerticalSpacer()
        OutlinedInputText(
            label = stringResource(id = R.string.qb_hint_username),
            text = uiState.username,
            singleLine = true,
            isError = uiState.usernameEmptyError,
            supportingText = stringResource(id = R.string.qb_hint_username_error),
            onValueChange = { onEditUsernameTextChanged(it) },
            modifier = Modifier.fillMaxWidth()
        )
        MediumVerticalSpacer()
        OutlinedInputText(
            label = stringResource(id = R.string.qb_hint_password),
            text = uiState.password,
            singleLine = true,
            keyboardType = KeyboardType.Password,
            onValueChange = { onEditPasswordTextChanged(it) },
            modifier = Modifier.fillMaxWidth()
        )
        MediumVerticalSpacer()
        PrimaryElevatedButton(
            text = stringResource(id = R.string.qb_confirm),
            onClick = { onBtnConfirmClick() },
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Preview(showBackground = true, name = "AddQbPage")
@Composable
fun PagePreview() {
    AppTheme {
        AddQbPage({})
    }
}
