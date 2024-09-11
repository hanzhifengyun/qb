package com.hzfy.ui.compose.text

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hzfy.ui.theme.AppTheme
import com.hzfy.ui.theme.shapes

@Composable
internal fun BaseText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    color: Color = MaterialTheme.colorScheme.onSurface,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null,
) {
    Text(
        text = text,
        modifier = modifier,
        style = style,
        color = color,
        maxLines = maxLines,
        textAlign = textAlign
    )
}


@Composable
fun BodyMediumText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    maxLines: Int = Int.MAX_VALUE,
) {
    BaseText(
        text = text,
        modifier = modifier,
        color = color,
        maxLines = maxLines,
        style = MaterialTheme.typography.bodyMedium
    )
}




@Composable
private fun TitleLargeText(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    color: Color = MaterialTheme.colorScheme.onPrimary,
) {
    BaseText(
        text = text,
        modifier = modifier,
        color = color,
        maxLines = maxLines,
        style = MaterialTheme.typography.titleLarge
    )
}

@Composable
fun OnPrimaryTitleLargeText(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
) {
    TitleLargeText(
        text = text,
        maxLines = maxLines,
        modifier = modifier,
        color = MaterialTheme.colorScheme.onPrimary
    )
}

@Composable
fun OnPrimaryContainerTitleLargeText(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
) {
    TitleLargeText(
        text = text,
        maxLines = maxLines,
        modifier = modifier,
        color = MaterialTheme.colorScheme.onPrimaryContainer
    )
}

@Composable
fun PrimaryBodyMediumText(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
) {
    BodyMediumText(
        text = text,
        modifier = modifier,
        maxLines = maxLines,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun PrimaryTitleLargeText(
    text: String,
    modifier: Modifier = Modifier
) {
    TitleLargeText(
        text = text,
        modifier = modifier,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun PrimaryBodyLargeText(
    text: String,
    modifier: Modifier = Modifier
) {
    BodyLargeText(
        text = text,
        modifier = modifier,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun OnPrimaryBodyMediumText(
    text: String,
    modifier: Modifier = Modifier
) {
    BodyMediumText(
        text = text,
        modifier = modifier,
        color = MaterialTheme.colorScheme.onPrimary
    )
}

@Composable
fun OnPrimaryContainerBodyMediumText(
    text: String,
    modifier: Modifier = Modifier
) {
    BodyMediumText(
        text = text,
        modifier = modifier,
        color = MaterialTheme.colorScheme.onPrimaryContainer
    )
}

@Composable
fun OnPrimaryBodyLargeText(
    text: String,
    modifier: Modifier = Modifier
) {
    BodyLargeText(
        text = text,
        modifier = modifier,
        color = MaterialTheme.colorScheme.onPrimary
    )
}


@Composable
fun BodyLargeText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
) {
    BaseText(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.bodyLarge,
        color = color,
    )
}


@Preview(showBackground = true, name = "CommonTextPreview")
@Composable
fun CommonTextPreview() {
    AppTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            BodyMediumText(text = "BodyMediumText")
            BodyMediumText(
                text = "BodyMediumText",
                modifier = Modifier
                    .border(
                        1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = shapes.medium
                    )
                    .padding(6.dp)
            )

            BodyLargeText(text = "BodyLargeText")
            PrimaryBodyMediumText(text = "PrimaryBodyMediumText")
            Surface(color = MaterialTheme.colorScheme.primary) {
                OnPrimaryBodyMediumText(text = "OnPrimaryBodyMediumText")
            }
            Surface(color = MaterialTheme.colorScheme.primaryContainer) {
                OnPrimaryContainerTitleLargeText(text = "OnPrimaryContainerTitleLargeText")
            }

        }

    }
}