package com.hzfy.qb.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hzfy.common.compose.TopBarPage
import com.hzfy.database.entity.QbEntity
import com.hzfy.qb.R
import com.hzfy.ui.compose.text.BodyMediumText
import com.hzfy.ui.compose.CommonDivider
import com.hzfy.ui.compose.text.LabelMediumText
import com.hzfy.ui.compose.PrimaryAddFloatingActionButton
import com.hzfy.ui.compose.SmailVerticalSpacer
import com.hzfy.ui.theme.AppTheme


typealias onQbItemClicked = (QbEntity) -> Unit

@Composable
fun QbListPage(
    onNavigationBack: () -> Unit,
    openAddQbPage: () -> Unit,
    openDetailPage: (QbEntity) -> Unit
) {

    val currentOnNavigationBack by rememberUpdatedState(onNavigationBack)
    val currentOpenAddQbPage by rememberUpdatedState(openAddQbPage)
    val currentOpenDetailPage by rememberUpdatedState(openDetailPage)
    TopBarPage<QbListViewModel>(
        onCloseCurrentPage = currentOnNavigationBack,
        title = stringResource(id = R.string.qb_title_list),
        getViewModel = { hiltViewModel() }) { viewModel, snackbarHostState ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        LaunchedEffect(uiState) {
            if (uiState.needOpenAddQbPage) {
                currentOpenAddQbPage()
            }
            val qb = uiState.openQbDetailPageEvent.getContentIfNotHandled()
            if (qb != null) {
                currentOpenDetailPage(qb)
            }
        }

        val listState = rememberLazyListState()

        ContentScreen(
            uiState = uiState,
            onItemClicked = { viewModel.onQbItemClick(it) },
            onAddQbClick = { viewModel.onAddQbClick() },
            listState = listState,
        )
    }
}

@Composable
fun ContentScreen(
    uiState: QbListUIState,
    onItemClicked: onQbItemClicked = {},
    onAddQbClick: () -> Unit = {},
    listState: LazyListState = rememberLazyListState()
) {
    Box(modifier = Modifier.fillMaxSize()) {
        QbItemList(qbList = uiState.qbList, onItemClicked = onItemClicked, listState = listState)


        PrimaryAddFloatingActionButton(
            onClick = { onAddQbClick() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp, end = 16.dp),
        )
    }
}

@Composable
private fun QbItemList(
    qbList: List<QbEntity>,
    onItemClicked: onQbItemClicked,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        modifier = modifier,
        state = listState
    ) {
        items(qbList) { item ->
            Column(Modifier.fillParentMaxWidth()) {
                QbItem(
                    modifier = Modifier.fillParentMaxWidth(),
                    item = item,
                    onItemClicked = onItemClicked
                )
                CommonDivider()
            }
        }
    }
}

@Composable
private fun QbItem(
    modifier: Modifier = Modifier,
    item: QbEntity,
    onItemClicked: onQbItemClicked
) {
    Column(modifier = modifier
        .clickable { onItemClicked(item) }
        .padding(12.dp)) {
        BodyMediumText(text = item.url)
        SmailVerticalSpacer()
        LabelMediumText(text = item.username)
    }

}


@Preview(showBackground = true, name = "QbListPage")
@Composable
fun QbListPagePreview() {
    AppTheme {
        ContentScreen(
            uiState = QbListUIState(
                qbList = listOf(
                    QbEntity(
                        url = "hhhh",
                        "hanzhifengyun",
                        password = ""
                    ),
                    QbEntity(
                        url = "aaaa",
                        "hanzhifengyun",
                        password = ""
                    ),
                    QbEntity(
                        url = "bbbb",
                        "hanzhifengyun",
                        password = ""
                    ),
                )
            ),
        )
    }
}