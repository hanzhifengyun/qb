package com.hzfy.common.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.hzfy.ui.theme.shapes

@Composable
fun LoadingPage() {
    Column(
        modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Surface(shape = shapes.medium) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(16.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        alignment = Alignment.Center,
                        painter = painterResource(id = R.mipmap.ic_logo),
                        contentDescription = "logo",
                        modifier = Modifier
                            .height(40.dp)
                            .width(40.dp)

                    )

                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .height(60.dp)
                            .width(60.dp)
                    )

                }

                MediumVerticalSpacer()
                PrimaryBodyLargeText(text = stringResource(id = R.string.loading_hard))
            }
        }
    }

}


@Preview(showBackground = true, name = "LoadingPage")
@Composable
fun LoadingPagePreview() {
    AppTheme {
        LoadingPage()
    }
}
