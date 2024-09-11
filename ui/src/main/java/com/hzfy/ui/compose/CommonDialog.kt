package com.hzfy.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.hzfy.ui.R
import com.hzfy.ui.compose.text.BodyMediumText
import com.hzfy.ui.compose.text.LabelLargeText
import com.hzfy.ui.compose.text.LabelMediumText
import com.hzfy.ui.compose.text.PrimaryBodyLargeText
import com.hzfy.ui.compose.text.PrimaryTitleLargeText
import com.hzfy.ui.theme.AppTheme
import com.hzfy.ui.theme.shapes

open class DialogMenuModel(val textResId: Int)


@Composable
fun <T : DialogMenuModel> MenuListDialog(
    onDismissRequest: () -> Unit,
    itemList: List<T>,
    onItemClick: (Int, T) -> Unit,
    properties: DialogProperties = DialogProperties(),
) {
    Dialog(onDismissRequest = onDismissRequest, properties = properties) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = shapes.large,
        ) {
            LazyColumn(
                modifier =
                Modifier.background(color = MaterialTheme.colorScheme.background)
            ) {
                itemsIndexed(itemList) { index, item ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { onItemClick(index, item) }
                    ) {
                        PrimaryTitleLargeText(
                            text = stringResource(id = item.textResId),
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                        CommonDivider()
                    }

                }

            }
        }

    }
}


@Composable
private fun CustomAlertDialog(
    title: String? = null,
    text: String? = null,
    onDismissRequest: () -> Unit,
    onLeftButtonClick: () -> Unit,
    onRightButtonClick: () -> Unit,
    leftText: String = stringResource(id = R.string.cancel),
    rightText: String = stringResource(id = R.string.confirm),
    properties: DialogProperties = DialogProperties(),
    content: @Composable (() -> Unit)? = null,
) {
    Dialog(onDismissRequest = { onDismissRequest() }, properties = properties) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = shapes.large,
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 20.dp, start = 26.dp, end = 16.dp, bottom = 10.dp)
            ) {
                if (!title.isNullOrEmpty()) {
                    PrimaryBodyLargeText(
                        text = title,
                        modifier = Modifier
                            .align(alignment = Alignment.CenterHorizontally)
                            .padding(bottom = 10.dp)
                    )
                }
                if (!text.isNullOrEmpty()) {
                    LabelLargeText(
                        text = text,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }

                if (content != null) {
                    SmailVerticalSpacer()
                    content()
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    CommonTextButton(
                        text = leftText,
                        onClick = onLeftButtonClick,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )
                    CommonTextButton(
                        text = rightText,
                        onClick = onRightButtonClick,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )
                }
            }
        }
    }

}

@Composable
fun CustomConfirmAlertDialog(
    onDismissRequest: () -> Unit,
    onButtonCancelClick: () -> Unit,
    onButtonConfirmClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    CustomAlertDialog(

        onDismissRequest = onDismissRequest,
        onLeftButtonClick = onButtonCancelClick,
        onRightButtonClick = onButtonConfirmClick,
        content = content,
    )
}

@Composable
fun ConfirmAlertDialog(
    text: String,
    title: String = "",
    onDismissRequest: () -> Unit,
    onButtonCancelClick: () -> Unit,
    onButtonConfirmClick: () -> Unit,
) {
    CustomAlertDialog(
        title = title,
        text = text,
        onDismissRequest = onDismissRequest,
        onLeftButtonClick = onButtonCancelClick,
        onRightButtonClick = onButtonConfirmClick,
    )
}


@Composable
fun CheckedConfirmAlertDialog(
    text: String,
    checkedText: String,
    title: String? = null,
    onDismissRequest: () -> Unit,
    onButtonCancelClick: () -> Unit,
    onButtonConfirmClick: (Boolean) -> Unit,
) {
    var checked by rememberSaveable {
        mutableStateOf(true)
    }
    CustomAlertDialog(
        title = title,
        text = text,
        onDismissRequest = onDismissRequest,
        onLeftButtonClick = onButtonCancelClick,
        onRightButtonClick = { onButtonConfirmClick(checked) },
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Checkbox(
                checked = checked, onCheckedChange = { checked = it },
                modifier = Modifier
                    .height(16.dp)
                    .width(16.dp)
            )

            LabelLargeText(
                text = checkedText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp)
            )

        }
    }
}

@Composable
fun InputConfirmAlertDialog(
    text: String = "",
    textHint: String,
    initInputText: String = "",
    onDismissRequest: () -> Unit,
    onButtonCancelClick: () -> Unit,
    onButtonConfirmClick: (String) -> Unit,
) {
    var inputText by rememberSaveable {
        mutableStateOf(initInputText)
    }
    CustomAlertDialog(
        text = text,
        onDismissRequest = onDismissRequest,
        onLeftButtonClick = onButtonCancelClick,
        onRightButtonClick = { onButtonConfirmClick(inputText) },
    ) {
        OutlinedInputText(
            label = textHint,
            text = inputText,
            onValueChange = { inputText = it },
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Composable
fun CommonAlertDialog(
    text: String,
    onDismissRequest: () -> Unit,
    onLeftButtonClick: () -> Unit,
    onRightButtonClick: () -> Unit,
    title: String = "",
    leftText: String = stringResource(id = R.string.cancel),
    rightText: String = stringResource(id = R.string.confirm),
    properties: DialogProperties = DialogProperties(),
) {

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            if (title.isNotEmpty()) {
                BodyMediumText(text = title)
            }
        },
        text = {
            LabelMediumText(text = text)
        },
        dismissButton = {
            CommonTextButton(text = leftText, onClick = onLeftButtonClick)
        },
        confirmButton = {
            CommonTextButton(text = rightText, onClick = onRightButtonClick)
        },
        properties = properties
    )
}

@Preview(showBackground = true, name = "CheckedConfirmAlertDialog")
@Composable
fun CheckedConfirmAlertDialogPreview() {
    AppTheme {
        CheckedConfirmAlertDialog(
//            title = "ConfirmAlertDialog",
            checkedText = "永久性删除这些文件",
            text = "确定要删除选中的 torrent 吗？",
            onDismissRequest = {},
            onButtonCancelClick = {},
            onButtonConfirmClick = {},
        )
    }
}

@Preview(showBackground = true, name = "InputConfirmAlertDialog")
@Composable
fun InputConfirmAlertDialogPreview() {
    AppTheme {
        InputConfirmAlertDialog(
            text = "保存路径:",
            textHint = "保存路径",
            onDismissRequest = {},
            onButtonCancelClick = {},
            onButtonConfirmClick = {},
        )
    }
}

@Preview(showBackground = true, name = "ConfirmAlertDialog")
@Composable
fun ConfirmAlertDialogPreview() {
    AppTheme {
        ConfirmAlertDialog(
            title = "ConfirmAlertDialog",
            text = "是否确认删除种子？",
            onDismissRequest = {},
            onButtonCancelClick = {},
            onButtonConfirmClick = {},
        )
    }
}

@Preview(showBackground = true, name = "CustomConfirmAlertDialog")
@Composable
fun CustomConfirmAlertDialogPreview() {
    AppTheme {
        CustomConfirmAlertDialog(
            onDismissRequest = {},
            onButtonCancelClick = {},
            onButtonConfirmClick = {},
            content = {
                LabelMediumText(text = "text")
            }
        )
    }
}


@Preview(showBackground = true, name = "CommonAlertDialog")
@Composable
fun CommonAlertDialogPreview() {
    AppTheme {
        CommonAlertDialog(
            text = "CommonAlertDialog",
            onDismissRequest = {},
            onLeftButtonClick = {},
            onRightButtonClick = {}
        )
    }
}

@Preview(showBackground = true, name = "MenuListDialog")
@Composable
fun MenuListDialogPreview() {
    AppTheme {
        MenuListDialog<DialogMenuModel>(
            onDismissRequest = {},
            itemList = listOf(DialogMenuModel(R.string.cancel), DialogMenuModel(R.string.confirm)),
            onItemClick = { _, _ -> })
    }
}