package com.hzfy.common.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hzfy.common.R
import com.hzfy.ui.compose.CenterAlignedTopBar
import com.hzfy.ui.compose.MediumVerticalSpacer
import com.hzfy.ui.compose.text.PrimaryBodyLargeText
import com.hzfy.ui.compose.PrimaryElevatedButton
import com.hzfy.ui.theme.AppTheme

@Composable
fun BaseErrorPage(title: String, onRetryClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        CenterAlignedTopBar(title = title)

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            PrimaryBodyLargeText(text = stringResource(id = R.string.common_error_dec))
            MediumVerticalSpacer()
            PrimaryElevatedButton(
                text = stringResource(id = R.string.retry),
                onClick = onRetryClick,
                modifier = Modifier.width(160.dp)
            )
        }

    }
}


@Preview(showBackground = true, name = "BaseErrorPage")
@Composable
fun BaseErrorPagePreview() {
    AppTheme {
        BaseErrorPage(
            title = "错误页面",
            onRetryClick = {}
        )
    }
}

