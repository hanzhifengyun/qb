package com.hzfy.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hzfy.ui.compose.text.BodyMediumText
import com.hzfy.ui.theme.AppTheme

@Composable
fun CommonCheckedItem(
    label: String,
    isChecked: Boolean,
    onCheckedChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    isWeight: Boolean = false,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    checkBoxColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        BodyMediumText(text = label, color = textColor)
        if (isWeight) {
            Spacer(modifier = Modifier.weight(1.0f))
        } else {
            Spacer(modifier = Modifier.width(10.dp))
        }
        Box(modifier = Modifier.clickable {
            onCheckedChanged(!isChecked)
        }) {
            if (isChecked) {
                CheckBoxIcon(tint = checkBoxColor)
            } else {
                CheckBoxOutlineBlankIcon(tint = checkBoxColor)
            }
        }
    }

}



@Composable
fun PrimaryContainerCheckedItem(
    text: String,
    isSelected: Boolean,
    onItemClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CheckedItem(
        text = text,
        isSelected = isSelected,
        onItemClicked = onItemClicked,
        modifier = modifier,
        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    )
}

@Composable
fun CheckedItem(
    text: String,
    isSelected: Boolean,
    onItemClicked: () -> Unit,
    backgroundColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
) {
    Row(verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = modifier
            .clickable { onItemClicked() }
            .background(color = backgroundColor)
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, top = 6.dp, bottom = 6.dp)
    ) {
        BodyMediumText(
            text = text,
            modifier = Modifier.weight(1.0f),
            color = contentColor
        )
        if (isSelected) {
            CheckIcon(tint = contentColor)
        }
    }
}

@Preview(showBackground = true, name = "CommonCheckedItem")
@Composable
fun CommonCheckedItemPreview() {
    AppTheme {
        CommonCheckedItem(label = "多看看", false, onCheckedChanged = {})
    }
}

