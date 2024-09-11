package com.hzfy.ui.compose

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun VerticalSpacer(height: Dp) {
    Spacer(modifier = Modifier.height(height))
}

@Composable
fun MediumVerticalSpacer() {
    VerticalSpacer(height = 10.dp)
}

@Composable
fun SmailVerticalSpacer() {
    VerticalSpacer(height = 6.dp)
}

@Composable
fun HorizontalSpacer(width: Dp) {
    Spacer(modifier = Modifier.width(width))
}

@Composable
fun MediumHorizontalSpacer() {
    HorizontalSpacer(width = 10.dp)
}

@Composable
fun SmailHorizontalSpacer() {
    HorizontalSpacer(width = 6.dp)
}


