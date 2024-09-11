package com.hzfy.common.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hzfy.common.R
import com.hzfy.ui.compose.MediumVerticalSpacer
import com.hzfy.ui.compose.text.PrimaryBodyLargeText
import com.hzfy.ui.theme.AppTheme

@Composable
fun EmptyPage() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {

        Image(
            alignment = Alignment.Center,
            painter = painterResource(id = R.mipmap.ic_logo),
            contentDescription = "",
            modifier = Modifier
                .height(100.dp)
                .width(100.dp)

        )
        MediumVerticalSpacer()
        PrimaryBodyLargeText(text = stringResource(id = R.string.common_empty_dec))
    }
}


@Preview(showBackground = true, name = "EmptyPage")
@Composable
fun EmptyPagePreview() {
    AppTheme {
        EmptyPage()
    }
}

