package com.hzfy.ui.compose.text

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hzfy.ui.theme.AppTheme
import com.hzfy.ui.theme.shapes

@Composable
fun LabelMediumText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    BaseText(
        text = text,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        style = MaterialTheme.typography.labelMedium
    )
}

@Composable
fun ErrorLabelMediumText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null,
) {
    BaseText(
        text = text,
        modifier = modifier,
        color = MaterialTheme.colorScheme.error,
        textAlign = textAlign,
        style = MaterialTheme.typography.labelMedium
    )
}


@Composable
fun LabelLargeText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    BaseText(
        text = text,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        style = MaterialTheme.typography.labelLarge
    )
}

@Composable
fun LabelSmallText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    BaseText(
        text = text,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        style = MaterialTheme.typography.labelSmall
    )
}

@Composable
fun OutlinePrimaryLabelMediumText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null,
) {
    LabelMediumText(
        text = text,
        modifier = modifier
            .border(
                1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = shapes.medium
            )
            .padding(4.dp),
        color = MaterialTheme.colorScheme.primary,
        textAlign = textAlign,
    )
}

@Composable
fun OutlinePrimaryLabelSmallText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null,
) {
    LabelSmallText(
        text = text,
        modifier = modifier
            .border(
                1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = shapes.medium
            )
            .padding(4.dp),
        color = MaterialTheme.colorScheme.primary,
        textAlign = textAlign,
    )
}

@Composable
fun FilledOnPrimaryLabelSmallText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null,
) {
    LabelSmallText(
        text = text,
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = shapes.medium
            )
            .padding(4.dp),
        color = MaterialTheme.colorScheme.onPrimary,
        textAlign = textAlign,
    )
}



@Preview(showBackground = true, name = "LabelText")
@Composable
fun LabelTextPreview() {
    AppTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            OutlinePrimaryLabelMediumText(text = "OutlinePrimaryLabelMediumText")
            OutlinePrimaryLabelSmallText(text = "OutlinePrimaryLabelSmallText")
            FilledOnPrimaryLabelSmallText(text = "FilledOnPrimaryLabelSmallText")
            LabelMediumText(text = "LabelMediumText")
            PrimaryBodyMediumText(text = "PrimaryBodyMediumText")
            ErrorLabelMediumText(text = "ErrorLabelMediumText")
        }

    }
}


