package com.hzfy.ui.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hzfy.ui.compose.text.OnPrimaryBodyMediumText
import com.hzfy.ui.compose.text.PrimaryBodyMediumText
import com.hzfy.ui.theme.AppTheme
import com.hzfy.ui.theme.shapes


@Composable
fun CommonTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = { onClick() }, 
        modifier = modifier
    ) {
        PrimaryBodyMediumText(text)
    }
}


@Composable
fun PrimaryElevatedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedButton(
        onClick = { onClick() },
        shape = shapes.large,
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = modifier
    ) {
        OnPrimaryBodyMediumText(text = text)
    }
}

@Composable
fun PrimaryOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = { onClick() },
        shape = shapes.large,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
            contentColor = MaterialTheme.colorScheme.primary
        ),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary),
        modifier = modifier
    ) {
        PrimaryBodyMediumText(text = text)
    }
}

@Composable
private fun CommonFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = FloatingActionButtonDefaults.containerColor,
    content: @Composable () -> Unit
) {
    FloatingActionButton(
        onClick = { onClick() },
        containerColor = containerColor,
        modifier = modifier,
    ) {
        content()
    }
}

@Composable
fun PrimaryAddFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CommonFloatingActionButton(
        onClick = { onClick() },
        containerColor = MaterialTheme.colorScheme.primary,
        modifier = modifier,
    ) {
        OnPrimaryAddIcon()
    }
}

@Composable
fun PrimaryArrowLeftFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CommonFloatingActionButton(
        onClick = { onClick() },
        containerColor = MaterialTheme.colorScheme.primary,
        modifier = modifier,
    ) {
        OnPrimaryArrowLeftIcon()
    }
}

@Composable
fun PrimaryArrowRightFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CommonFloatingActionButton(
        onClick = { onClick() },
        containerColor = MaterialTheme.colorScheme.primary,
        modifier = modifier,
    ) {
        OnPrimaryArrowRightIcon()
    }
}


@Preview(showBackground = true, name = "CommonButtonPreview")
@Composable
fun CommonButtonPreview() {
    AppTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            Row {
                PrimaryOutlinedButton(text = "SubButton", onClick = {})
                PrimaryElevatedButton(text = "MainButton", onClick = {})
            }

            PrimaryAddFloatingActionButton(onClick = {})

            CommonTextButton(text = "MainButton", onClick = {})
        }

    }
}