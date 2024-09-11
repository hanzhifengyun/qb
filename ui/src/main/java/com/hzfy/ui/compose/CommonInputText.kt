package com.hzfy.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.hzfy.ui.compose.text.ErrorLabelMediumText
import com.hzfy.ui.compose.text.LabelMediumText
import com.hzfy.ui.theme.AppTheme
import com.hzfy.ui.theme.shapes

@Composable
fun OutlinedInputText(
    label: String,
    text: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    supportingText: String = "",
    isError: Boolean = false,
    singleLine: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    trailingIcon: @Composable (() -> Unit)? = null,
    color: Color = MaterialTheme.colorScheme.outline
) {
    OutlinedTextField(
        value = text,
        colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = color,
            focusedTextColor = color, focusedBorderColor = color, cursorColor = color),
        singleLine = singleLine,
        shape = shapes.small,
        isError = isError,
        textStyle = MaterialTheme.typography.labelMedium,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        onValueChange = onValueChange,
        label = { LabelMediumText(text = label, color = color) },
        supportingText = {
            if (isError) {
                ErrorLabelMediumText(text = supportingText)
            }
        },
        modifier = modifier,
        trailingIcon = trailingIcon
    )
}

@Composable
fun OutlinedInputFieldValueText(
    label: String,
    text: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    supportingText: String = "",
    isError: Boolean = false,
    singleLine: Boolean = false,
    readOnly: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    OutlinedTextField(
        value = text,
        colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = MaterialTheme.colorScheme.outline),
        singleLine = singleLine,
        shape = shapes.small,
        isError = isError,
        textStyle = MaterialTheme.typography.labelMedium,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        onValueChange = onValueChange,
        label = { LabelMediumText(text = label) },
        supportingText = {
            if (isError) {
                ErrorLabelMediumText(text = supportingText)
            }
        },
        modifier = modifier,
        readOnly = readOnly,
        trailingIcon = trailingIcon
    )
}

@Composable
fun SearchItem(
    label: String,
    onKeywordSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.outline
) {
    var keyword by rememberSaveable { mutableStateOf("") }
    OutlinedInputText(
        label = label,
        text = keyword,
        singleLine = true,
        onValueChange = { keyword = it },
        modifier = modifier,
        trailingIcon = {
            Box(modifier = Modifier.clickable {
                onKeywordSearch(keyword)
            }) {
                SearchIcon(tint = color)
            }
        },
        color = color
    )
}


@Preview(showBackground = true, name = "CommonInputTextPreview")
@Composable
fun CommonInputTextPreview() {
    AppTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            OutlinedInputText(
                label = "请输入。。。",
                text = "",
                isError = true,
                onValueChange = { },
            )
            OutlinedInputText(
                label = "",
                text = "",
                keyboardType = KeyboardType.Password,
                onValueChange = { },
            )
        }

    }
}