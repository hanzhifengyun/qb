package com.hzfy.ui.compose

import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun CommonDivider(color: Color = DividerDefaults.color) {
    Divider(color = color)
}

@Composable
fun WhiteDivider() {
    Divider(color = Color.White)
}