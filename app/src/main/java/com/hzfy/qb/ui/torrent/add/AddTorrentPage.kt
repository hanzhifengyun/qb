package com.hzfy.qb.ui.torrent.add

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hzfy.common.compose.TopBarPage
import com.hzfy.common.model.ParamMenuItem
import com.hzfy.library.util.FileUtils
import com.hzfy.qb.R
import com.hzfy.qb.model.detail.CategoryModel
import com.hzfy.ui.compose.CommonCheckedItem
import com.hzfy.ui.compose.CustomExposedDropdownMenuBox
import com.hzfy.ui.compose.MultipleFilePicker
import com.hzfy.ui.compose.OutlinedInputText
import com.hzfy.ui.compose.PrimaryElevatedButton
import com.hzfy.ui.compose.SmailHorizontalSpacer
import com.hzfy.ui.compose.SmailVerticalSpacer
import com.hzfy.ui.compose.TopBarConfig
import com.hzfy.ui.compose.text.BodyMediumText

@Composable
fun AddTorrentPage(
    qbUid: Int?,
    onNavigationBack: () -> Unit
) {
    val viewModel: AddTorrentModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onStart(qbUid)
    }

    val fileType = listOf("torrent")
    MultipleFilePicker(show = uiState.showFilePicker, fileExtensions = fileType) { files ->
        viewModel.onFilesPick(files)
    }


    TopBarPage<AddTorrentModel>(
        onCloseCurrentPage = onNavigationBack,
        title = stringResource(id = R.string.qb_title_add_torrent),
        topBarConfig = TopBarConfig.DEFAULT.copy(
            showLeftIcon = true,
            onLeftIconClick = onNavigationBack
        ),
        getViewModel = { viewModel }) { _, _ ->


        ContentScreen(
            uiState = uiState,
            onBtnChooseFileClick = { viewModel.onBtnChooseFileClick() },
            onBtnConfirmClick = { viewModel.onBtnConfirmClick() },
            onCategoryItemClick = { category -> viewModel.onCategoryItemClick(category) },
            onCategoryTextChanged = { text -> viewModel.onCategoryTextChanged(text) },
            onTorrentManagerModeManualCheckedChanged = { isChecked ->
                viewModel.onTorrentManagerModeManualCheckedChanged(
                    isChecked
                )
            },
            onTorrentStartCheckedChanged = { isChecked ->
                viewModel.onTorrentStartCheckedChanged(
                    isChecked
                )
            },
            onTorrentSkipHashVerificationCheckedChanged = { isChecked ->
                viewModel.onTorrentSkipHashVerificationCheckedChanged(
                    isChecked
                )
            },
            onTorrentDownloadInOrderCheckedChanged = { isChecked ->
                viewModel.onTorrentDownloadInOrderCheckedChanged(
                    isChecked
                )
            },
            onTorrentDownloadFirstFileBlocksCheckedChanged = { isChecked ->
                viewModel.onTorrentDownloadFirstFileBlocksCheckedChanged(
                    isChecked
                )
            },
            onSavePathValueChange = { text ->
                viewModel.onSavePathValueChange(text)
            },
            onRenameValueChange = { text ->
                viewModel.onRenameValueChange(text)
            },
            onDownloadSpeedLimitValueChange = { text ->
                viewModel.onDownloadSpeedLimitValueChange(text)
            },
            onUploadSpeedLimitValueChange = { text ->
                viewModel.onUploadSpeedLimitValueChange(text)
            },
            onStopConditionItemClick = { item ->
                viewModel.onStopConditionItemClick(item)
            },
            onContentLayoutItemClick = { item ->
                viewModel.onContentLayoutItemClick(item)
            },
        )
    }
}


@Composable
private fun ContentScreen(
    uiState: AddTorrentUIState,
    onBtnChooseFileClick: () -> Unit = {},
    onBtnConfirmClick: () -> Unit = {},
    onCategoryItemClick: (CategoryModel) -> Unit = {},
    onCategoryTextChanged: (String) -> Unit = {},
    onSavePathValueChange: (String) -> Unit = {},
    onRenameValueChange: (String) -> Unit = {},
    onDownloadSpeedLimitValueChange: (String) -> Unit = {},
    onUploadSpeedLimitValueChange: (String) -> Unit = {},
    onTorrentManagerModeManualCheckedChanged: (Boolean) -> Unit = {},
    onTorrentStartCheckedChanged: (Boolean) -> Unit = {},
    onTorrentSkipHashVerificationCheckedChanged: (Boolean) -> Unit = {},
    onTorrentDownloadInOrderCheckedChanged: (Boolean) -> Unit = {},
    onTorrentDownloadFirstFileBlocksCheckedChanged: (Boolean) -> Unit = {},
    onStopConditionItemClick: (ParamMenuItem) -> Unit = {},
    onContentLayoutItemClick: (ParamMenuItem) -> Unit = {},
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                val context = LocalContext.current
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    PrimaryElevatedButton(
                        text = stringResource(id = R.string.qb_choose_file),
                        onClick = { onBtnChooseFileClick() },
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )
                    SmailHorizontalSpacer()

                    uiState.filePathList.apply {
                        val filename: String = if (isEmpty()) {
                            stringResource(id = R.string.qb_choose_file_empty)
                        } else if (size == 1) {
                            FileUtils.getFileName(get(0)) ?: ""
                        } else {
                            String.format(
                                context.getString(R.string.qb_choose_filename_format),
                                FileUtils.getFileName(get(0)) ?: "",
                                size.toString()
                            )
                        }
                        BodyMediumText(text = filename)
                    }


                }
                SmailVerticalSpacer()
                CommonCheckedItem(
                    label = stringResource(id = R.string.qb_torrent_manager_mode_manual),
                    uiState.isTorrentManagerModeManualChecked,
                    onCheckedChanged = { onTorrentManagerModeManualCheckedChanged(it) }
                )
                SmailVerticalSpacer()
                OutlinedInputText(
                    label = stringResource(id = R.string.qb_torrent_save_path),
                    text = uiState.savePath,
                    onValueChange = { onSavePathValueChange(it) },
                    singleLine = true,
                    keyboardType = KeyboardType.Text,
                    modifier = Modifier.fillMaxWidth()
                )
                SmailVerticalSpacer()
                OutlinedInputText(
                    label = stringResource(id = R.string.qb_torrent_rename),
                    text = uiState.rename,
                    onValueChange = { onRenameValueChange(it) },
                    singleLine = true,
                    keyboardType = KeyboardType.Text,
                    modifier = Modifier.fillMaxWidth()
                )
                SmailVerticalSpacer()
                CustomExposedDropdownMenuBox(
                    hintText = stringResource(id = R.string.qb_category),
                    itemList = uiState.categoryList,
                    onItemClick = { item -> onCategoryItemClick(item) },
                    onValueChange = { text -> onCategoryTextChanged(text) },
                    selectedText = uiState.category,
                    modifier = Modifier.fillMaxWidth()
                )
                SmailVerticalSpacer()
                CommonCheckedItem(
                    label = stringResource(id = R.string.qb_torrent_start),
                    uiState.isTorrentStartChecked,
                    onCheckedChanged = { onTorrentStartCheckedChanged(it) }
                )
                SmailVerticalSpacer()
                val stopConditionList = rememberSaveable(saver = ParamMenuItem.Saver) {
                    val list = getStopConditionList(context)
                    onStopConditionItemClick(list[0])
                    list
                }

                val stopConditionText = if (uiState.stopCondition != null) {
                    uiState.stopCondition.name
                } else {
                    ""
                }
                CustomExposedDropdownMenuBox(
                    hintText = stringResource(id = R.string.qb_torrent_stop_condition),
                    itemList = stopConditionList,
                    readOnly = true,
                    onItemClick = { item -> onStopConditionItemClick(item) },
                    selectedText = stopConditionText,
                    modifier = Modifier.fillMaxWidth()
                )
                SmailVerticalSpacer()

                CommonCheckedItem(
                    label = stringResource(id = R.string.qb_torrent_skip_hash_verification),
                    uiState.isTorrentSkipHashVerificationChecked,
                    onCheckedChanged = { onTorrentSkipHashVerificationCheckedChanged(it) }
                )

                val contentLayoutList = rememberSaveable(saver = ParamMenuItem.Saver) {
                    val list = getContentLayoutList(context)
                    onContentLayoutItemClick(list[0])
                    list
                }
                SmailVerticalSpacer()

                val contentLayoutText = if (uiState.contentLayout != null) {
                    uiState.contentLayout.name
                } else {
                    ""
                }
                CustomExposedDropdownMenuBox(
                    hintText = stringResource(id = R.string.qb_torrent_content_layout),
                    itemList = contentLayoutList,
                    readOnly = true,
                    onItemClick = { item -> onContentLayoutItemClick(item) },
                    selectedText = contentLayoutText,
                    modifier = Modifier.fillMaxWidth()
                )

                SmailVerticalSpacer()
                CommonCheckedItem(
                    label = stringResource(id = R.string.qb_torrent_download_in_order),
                    uiState.isTorrentDownloadInOrderChecked,
                    onCheckedChanged = { onTorrentDownloadInOrderCheckedChanged(it) }
                )
                SmailVerticalSpacer()
                CommonCheckedItem(
                    label = stringResource(id = R.string.qb_torrent_download_first_file_blocks),
                    uiState.isTorrentDownloadFirstFileBlocksChecked,
                    onCheckedChanged = { onTorrentDownloadFirstFileBlocksCheckedChanged(it) }
                )
                SmailVerticalSpacer()
                OutlinedInputText(
                    label = stringResource(id = R.string.qb_torrent_limit_download_speed),
                    text = uiState.downloadSpeedLimit,
                    onValueChange = { onDownloadSpeedLimitValueChange(it) },
                    singleLine = true,
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.fillMaxWidth()
                )
                SmailVerticalSpacer()
                OutlinedInputText(
                    label = stringResource(id = R.string.qb_torrent_limit_upload_speed),
                    text = uiState.uploadSpeedLimit,
                    onValueChange = { onUploadSpeedLimitValueChange(it) },
                    singleLine = true,
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.fillMaxWidth()
                )

                SmailVerticalSpacer()
                PrimaryElevatedButton(
                    text = stringResource(id = R.string.qb_confirm),
                    onClick = { onBtnConfirmClick() },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }


}


private fun getStopConditionList(context: Context): List<ParamMenuItem> {
    val list: MutableList<ParamMenuItem> = mutableListOf()
    list.apply {
        add(ParamMenuItem(context.getString(R.string.qb_torrent_stop_condition_none), "none"))
        add(
            ParamMenuItem(
                context.getString(R.string.qb_torrent_stop_condition_metadata_received),
                "MetadataReceived"
            )
        )
        add(
            ParamMenuItem(
                context.getString(R.string.qb_torrent_stop_condition_files_checked),
                "FilesChecked"
            )
        )
    }
    return list
}

private fun getContentLayoutList(context: Context): List<ParamMenuItem> {
    val list: MutableList<ParamMenuItem> = mutableListOf()
    list.apply {
        add(
            ParamMenuItem(
                context.getString(R.string.qb_torrent_content_layout_original),
                "Original"
            )
        )
        add(
            ParamMenuItem(
                context.getString(R.string.qb_torrent_content_layout_subfolder),
                "Subfolder"
            )
        )
        add(
            ParamMenuItem(
                context.getString(R.string.qb_torrent_content_layout_no_subfolder),
                "NoSubfolder"
            )
        )
    }
    return list
}

