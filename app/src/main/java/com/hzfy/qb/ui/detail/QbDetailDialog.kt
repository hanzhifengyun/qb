package com.hzfy.qb.ui.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hzfy.qb.R
import com.hzfy.qb.model.detail.TorrentModel
import com.hzfy.ui.compose.CheckedConfirmAlertDialog
import com.hzfy.ui.compose.DialogMenuModel
import com.hzfy.ui.compose.InputConfirmAlertDialog
import com.hzfy.ui.compose.MenuListDialog


@Composable
fun QbDetailDialogs(viewModel: QbDetailViewModel) {
    val dialogUiState by viewModel.torrentMenuDialogUiState.collectAsStateWithLifecycle()

    val torrent = dialogUiState.torrent
    torrent?.let {
        if (dialogUiState.showTorrentMenuDialog) {
            TorrentMenuDialog(
                item = torrent,
                onDismissRequest = { viewModel.onTorrentMenuDialogDismiss() },
                onMenuPauseClick = {
                    viewModel.onTorrentMenuDialogPauseClick(it)
                },
                onMenuResumeClick = {
                    viewModel.onTorrentMenuDialogResumeClick(it)
                },
                onMenuDeleteClick = {
                    viewModel.onTorrentMenuDialogDeleteClick(it)
                },
                onMenuChangeSavePathClick = {
                    viewModel.onTorrentMenuDialogChangeSavePathClick(it)
                },
            )
        }

        if (dialogUiState.showDeleteTorrentDialog) {
            DeleteTorrentDialog(
                item = torrent,
                onDismissRequest = { viewModel.onDeleteTorrentDialogDismiss() },
                onButtonCancelClick = { viewModel.onDeleteTorrentDialogDismiss() },
                onButtonConfirmClick = { item, deleteFiles ->
                    viewModel.onDeleteTorrentConfirmClick(item, deleteFiles)
                },
            )
        }

        if (dialogUiState.showChangeTorrentSavePathDialog) {
            ChangeTorrentSavePathDialog(
                item = torrent,
                onDismissRequest = { viewModel.onChangeTorrentSavePathDialogDismiss() },
                onButtonCancelClick = { viewModel.onChangeTorrentSavePathDialogDismiss() },
                onButtonConfirmClick = { item, path ->
                    viewModel.onChangeTorrentSavePathConfirmClick(item, path)
                },
            )
        }
    }

}

@Composable
fun ChangeTorrentSavePathDialog(
    item: TorrentModel,
    onDismissRequest: () -> Unit,
    onButtonCancelClick: () -> Unit,
    onButtonConfirmClick: (TorrentModel, String) -> Unit,
) {
    val savePath = if (item.savePath.isNullOrEmpty()) {
        stringResource(id = R.string.qb_torrent_change_save_path_dialog_text_hint)
    } else {
        item.savePath!!
    }
    InputConfirmAlertDialog(
        textHint = stringResource(id = R.string.qb_torrent_change_save_path_dialog_text_hint),
        initInputText = savePath,
        onDismissRequest = onDismissRequest,
        onButtonCancelClick = onButtonCancelClick,
        onButtonConfirmClick = { path ->
            onButtonConfirmClick(item, path)
        },
    )
}

@Composable
fun DeleteTorrentDialog(
    item: TorrentModel,
    onDismissRequest: () -> Unit,
    onButtonCancelClick: () -> Unit,
    onButtonConfirmClick: (TorrentModel, Boolean) -> Unit,
) {
    CheckedConfirmAlertDialog(
        text = stringResource(id = R.string.qb_torrent_delete_dialog_text),
        checkedText = stringResource(id = R.string.qb_torrent_delete_dialog_checked_text),
        onDismissRequest = onDismissRequest,
        onButtonCancelClick = onButtonCancelClick,
        onButtonConfirmClick = { isChecked -> onButtonConfirmClick(item, isChecked) }
    )
}


@Composable
fun TorrentMenuDialog(
    item: TorrentModel,
    onDismissRequest: () -> Unit,
    onMenuPauseClick: (TorrentModel) -> Unit,
    onMenuResumeClick: (TorrentModel) -> Unit,
    onMenuDeleteClick: (TorrentModel) -> Unit,
    onMenuChangeSavePathClick: (TorrentModel) -> Unit,
) {
    val torrentMenuList = getTorrentMenuList(item)
    MenuListDialog(
        onDismissRequest = onDismissRequest,
        itemList = torrentMenuList,
        onItemClick = { _, menu ->
            when (menu) {
                PauseTorrentMenuModel -> {
                    onDismissRequest()
                    onMenuPauseClick(item)
                }

                ResumeTorrentMenuModel -> {
                    onDismissRequest()
                    onMenuResumeClick(item)
                }

                DeleteTorrentMenuModel -> {
                    onDismissRequest()
                    onMenuDeleteClick(item)
                }

                ChangeSavePathTorrentMenuModel -> {
                    onDismissRequest()
                    onMenuChangeSavePathClick(item)
                }
            }
        })
}

fun getTorrentMenuList(item: TorrentModel): List<DialogMenuModel> {
    val dialogMenuList: MutableList<DialogMenuModel> = mutableListOf()
    val stateMenu = when (item.state) {
        TorrentModel.STATE_PAUSED_DL, TorrentModel.STATE_PAUSED_UP -> ResumeTorrentMenuModel
        else -> PauseTorrentMenuModel
    }
    dialogMenuList.add(stateMenu)
    dialogMenuList.add(DeleteTorrentMenuModel)
    dialogMenuList.add(ChangeSavePathTorrentMenuModel)
    return dialogMenuList
}

internal object PauseTorrentMenuModel : DialogMenuModel(R.string.qb_torrent_pause)
internal object ResumeTorrentMenuModel : DialogMenuModel(R.string.qb_torrent_resume)
internal object DeleteTorrentMenuModel : DialogMenuModel(R.string.qb_torrent_delete)
internal object ChangeSavePathTorrentMenuModel :
    DialogMenuModel(R.string.qb_torrent_change_save_path)

