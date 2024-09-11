package com.hzfy.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import com.hzfy.ui.compose.text.LabelMediumText
import com.hzfy.ui.compose.text.PrimaryBodyMediumText
import com.hzfy.ui.model.INameItem


@Composable
fun <T : INameItem> CommonDropdownMenu(
    itemList: List<T>,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onItemClick: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownMenu(expanded = expanded, onDismissRequest = onDismissRequest, modifier = modifier) {
        itemList.forEachIndexed { index, item ->
            if (index > 0) {
                CommonDivider()
            }
            DropdownMenuItem(text = {
                PrimaryBodyMediumText(text = item.getItemName())
            }, onClick = { onItemClick(item) })

        }
    }
}


@Composable
fun <T : INameItem> CustomExposedDropdownMenuBox(
    hintText: String,
    itemList: List<T>,
    onItemClick: (T) -> Unit,
    selectedText: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {},
    singleLine: Boolean = true,
    readOnly: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {

    var expanded by rememberSaveable { mutableStateOf(false) }
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedInputFieldValueText(
            label = hintText,
            text = TextFieldValue(selectedText, TextRange(selectedText.length)),
            onValueChange = { onValueChange(it.text) },
            singleLine = singleLine,
            keyboardType = keyboardType,
            readOnly = readOnly,
            trailingIcon = {
                Box(modifier = Modifier.clickable {
                    expanded = !expanded
                }) {
                    if (expanded) {
                        ArrowUpwardIcon()
                    } else {
                        ArrowDownwardIcon()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            itemList.forEachIndexed { index, item ->
                if (index > 0) {
                    CommonDivider()
                }
                DropdownMenuItem(text = {
                    PrimaryBodyMediumText(text = item.getItemName())
                }, onClick = {
                    expanded = false
                    onItemClick(item)
                })

            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : INameItem> CommonExposedDropdownMenuBox(
    itemList: List<T>,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onExpandedChange: (Boolean) -> Unit,
    onValueChange: (String) -> Unit,
    onItemClick: (T) -> Unit,
    selectedText: String,
    modifier: Modifier = Modifier,
    hintText: String = "",
    singleLine: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier
    ) {
        TextField(
            value = selectedText,
            label = { LabelMediumText(text = hintText) },
            onValueChange = onValueChange,
            singleLine = singleLine,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            trailingIcon = { TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor()
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { }) {
            itemList.forEachIndexed { index, item ->
                if (index > 0) {
                    CommonDivider()
                }
                DropdownMenuItem(text = {
                    PrimaryBodyMediumText(text = item.getItemName())
                }, onClick = { onItemClick(item) })

            }
        }
    }
}