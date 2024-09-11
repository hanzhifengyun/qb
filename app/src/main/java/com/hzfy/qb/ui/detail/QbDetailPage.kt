@file:OptIn(ExperimentalFoundationApi::class)

package com.hzfy.qb.ui.detail

import android.graphics.Color
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hzfy.common.compose.EmptyPage
import com.hzfy.common.compose.TopBarPage
import com.hzfy.common.util.UIUtil
import com.hzfy.database.entity.QbEntity
import com.hzfy.library.util.DateUtil
import com.hzfy.qb.R
import com.hzfy.qb.model.detail.CategoryModel
import com.hzfy.qb.model.detail.QbDetailModel
import com.hzfy.qb.model.detail.QbInfoModel
import com.hzfy.qb.model.detail.StateModel
import com.hzfy.qb.model.detail.TagModel
import com.hzfy.qb.model.detail.TorrentModel
import com.hzfy.qb.util.QbUtil
import com.hzfy.ui.compose.CommonDivider
import com.hzfy.ui.compose.OnPrimaryArrowDownwardIcon
import com.hzfy.ui.compose.OnPrimaryArrowForwardIcon
import com.hzfy.ui.compose.OnPrimaryMenuIcon
import com.hzfy.ui.compose.PrimaryAddFloatingActionButton
import com.hzfy.ui.compose.PrimaryContainerCheckedItem
import com.hzfy.ui.compose.PrimaryDownloadIcon
import com.hzfy.ui.compose.PrimaryUploadIcon
import com.hzfy.ui.compose.SmailHorizontalSpacer
import com.hzfy.ui.compose.SmailVerticalSpacer
import com.hzfy.ui.compose.TopBarConfig
import com.hzfy.ui.compose.WhiteDivider
import com.hzfy.ui.compose.text.BodyMediumText
import com.hzfy.ui.compose.text.FilledOnPrimaryLabelSmallText
import com.hzfy.ui.compose.text.LabelMediumText
import com.hzfy.ui.compose.text.OnPrimaryBodyLargeText
import com.hzfy.ui.compose.text.OutlinePrimaryLabelSmallText
import com.hzfy.ui.compose.text.PrimaryBodyMediumText
import com.hzfy.ui.theme.AppTheme
import com.hzfy.ui.theme.shapes
import kotlinx.coroutines.launch


typealias onItemTorrentClick = (TorrentModel) -> Unit
typealias onItemTorrentLongClick = (TorrentModel) -> Unit

@Composable
fun QbDetailPage(
    qbUid: Int?, onNavigationBack: () -> Unit,
    openAddTorrentPage: (QbEntity) -> Unit
) {

    val viewModel: QbDetailViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        viewModel.onStart(qbUid)
    }

    BackHandler {
        if (drawerState.isOpen) {
            scope.launch {
                drawerState.close()
            }
        } else {
            onNavigationBack()
        }
    }

    val listState = rememberLazyListState()



    ModalNavigationDrawer(drawerState = drawerState,
        modifier = Modifier.fillMaxSize(),
        drawerContent = {
            DetailDrawerContent(uiState = uiState,
                onStateItemClick = { viewModel.onStateItemClick(it) },
                onCategoryItemClick = { viewModel.onCategoryItemClick(it) },
                onTagItemClick = { viewModel.onTagItemClick(it) }
            )
        }) {

        val stateTitle =
            stringResource(id = QbUtil.getTorrentStateResId(uiState.currentSelectedState))
        val categoryTitle = when (uiState.currentSelectedCategory) {
            CategoryModel.ALL -> ""
            CategoryModel.NONE -> ""
            else -> "(${uiState.currentSelectedCategory})"
        }
        val tagTitle = when (uiState.currentSelectedTag) {
            TagModel.ALL -> ""
            TagModel.NONE -> ""
            else -> "(${uiState.currentSelectedTag})"
        }
        val title = stateTitle + categoryTitle + tagTitle
        TopBarPage(
            onCloseCurrentPage = onNavigationBack,
            title = title,
            topBarConfig = TopBarConfig.DEFAULT.copy(showLeftIcon = true,
                leftIconContent = { OnPrimaryMenuIcon() },
                onLeftIconClick = {
                    if (drawerState.isClosed) {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                }),
            getViewModel = { viewModel }) { _, snackbarHostState ->
            val context = LocalContext.current

            LaunchedEffect(uiState) {
                if (uiState.needScrollToTop) {
                    listState.scrollToItem(0)
                }
                val qbEntity = uiState.openAddTorrentPageEvent.getContentIfNotHandled()
                qbEntity?.let(openAddTorrentPage)

                val operateSuccess = uiState.showOperationSuccessEvent.getContentIfNotHandled()
                operateSuccess?.let { success ->
                    if (success) {
                        snackbarHostState.showSnackbar(context.getString(R.string.qb_operate_success))
                    } else {
                        snackbarHostState.showSnackbar(context.getString(R.string.qb_operate_failure))
                    }
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                ContentScreen(
                    torrentList = uiState.getDisplayTorrentList(),
                    listState = listState,
                    onAddTorrentClick = { viewModel.onAddTorrentClick() },
                    onItemTorrentClick = { viewModel.onItemTorrentClick(it) },
                    onItemTorrentLongClick = { viewModel.onItemTorrentLongClick(it) }
                )


                QbDetailDialogs(viewModel)
            }


        }

    }


}

@Composable
fun DetailDrawerContent(
    uiState: QbDetailUIState,
    onStateItemClick: (StateModel) -> Unit,
    onCategoryItemClick: (CategoryModel) -> Unit,
    onTagItemClick: (TagModel) -> Unit,
    modifier: Modifier = Modifier
) {
    val qbDetailModel = uiState.qbDetailModel
    var isStateOpen by rememberSaveable { mutableStateOf(true) }
    var isCategoryOpen by rememberSaveable { mutableStateOf(true) }
    var isTagsOpen by rememberSaveable { mutableStateOf(true) }

    LazyColumn(
        modifier = Modifier
            .width(250.dp)
            .fillMaxHeight()
            .windowInsetsPadding(WindowInsets.statusBars)
            .background(color = MaterialTheme.colorScheme.primary),
    ) {
        qbDetailModel?.apply {
            item {
                TorrentDetailItem(qbInfo = qbInfo, modifier = modifier)
            }

            item {
                GroupTitleItem(title = stringResource(id = R.string.qb_torrent_state),
                    isOpen = isStateOpen,
                    onGroupItemClick = {
                        isStateOpen = !isStateOpen
                    }
                )
            }
            if (isStateOpen) {
                items(stateList) { item ->
                    if (item.count > 0) {
                        Column(Modifier.fillParentMaxWidth()) {
                            StateItem(
                                currentSelectedState = uiState.currentSelectedState,
                                modifier = Modifier.fillParentMaxWidth(),
                                item = item,
                                onItemClicked = onStateItemClick
                            )
                            CommonDivider()
                        }
                    }
                }
            }

            item {
                WhiteDivider()
                GroupTitleItem(title = stringResource(id = R.string.qb_category),
                    isOpen = isCategoryOpen,
                    onGroupItemClick = {
                        isCategoryOpen = !isCategoryOpen
                    }
                )
            }
            if (isCategoryOpen) {
                items(categoryList) { item ->
                    if (item.count > 0) {
                        Column(Modifier.fillParentMaxWidth()) {
                            CategoryItem(
                                currentSelectedCategory = uiState.currentSelectedCategory,
                                modifier = Modifier.fillParentMaxWidth(),
                                item = item,
                                onItemClicked = onCategoryItemClick
                            )
                            CommonDivider()
                        }
                    }
                }
            }

            item {
                WhiteDivider()
                GroupTitleItem(title = stringResource(id = R.string.qb_tag),
                    isOpen = isTagsOpen,
                    onGroupItemClick = {
                        isTagsOpen = !isTagsOpen
                    }
                )
            }

            if (isTagsOpen) {
                items(tagList) { item ->
                    if (item.count > 0) {
                        Column(Modifier.fillParentMaxWidth()) {
                            TagItem(
                                currentSelectedTag = uiState.currentSelectedTag,
                                modifier = Modifier.fillParentMaxWidth(),
                                item = item,
                                onItemClicked = onTagItemClick
                            )
                            CommonDivider()
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(56.dp))
            }
        }


    }
}

@Composable
fun TorrentDetailItem(
    qbInfo: QbInfoModel,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Image(painter = painterResource(R.mipmap.ic_qb_logo), contentDescription = null)

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            OnPrimaryBodyLargeText(
                text = qbInfo.url,
                modifier = Modifier.padding(start = 16.dp)
            )
            OnPrimaryBodyLargeText(
                text = qbInfo.username,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

    }
}

@Composable
fun CategoryItem(
    currentSelectedCategory: String,
    item: CategoryModel,
    onItemClicked: (CategoryModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val nameText = when (item.name) {
        CategoryModel.ALL -> stringResource(id = R.string.qb_torrent_category_all)
        CategoryModel.NONE -> stringResource(id = R.string.qb_torrent_category_none)
        else -> item.name
    }
    val text = nameText + " (${item.count})"
    val isSelected = item.name == currentSelectedCategory
    PrimaryContainerCheckedItem(
        text = text,
        isSelected = isSelected,
        onItemClicked = { onItemClicked(item) },
        modifier = modifier
    )
}

@Composable
fun TagItem(
    currentSelectedTag: String,
    item: TagModel,
    onItemClicked: (TagModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val nameText = when (item.name) {
        TagModel.ALL -> stringResource(id = R.string.qb_torrent_tag_all)
        TagModel.NONE -> stringResource(id = R.string.qb_torrent_tag_none)
        else -> item.name
    }
    val text = nameText + " (${item.count})"
    val isSelected = item.name == currentSelectedTag
    PrimaryContainerCheckedItem(
        text = text,
        isSelected = isSelected,
        onItemClicked = { onItemClicked(item) },
        modifier = modifier
    )
}

@Composable
fun StateItem(
    currentSelectedState: String,
    item: StateModel,
    onItemClicked: (StateModel) -> Unit,
    modifier: Modifier = Modifier
) {
    val text = stringResource(id = QbUtil.getTorrentStateResId(item.state)) + " (${item.count})"
    val isSelected = item.state == currentSelectedState
    PrimaryContainerCheckedItem(
        text = text,
        isSelected = isSelected,
        onItemClicked = { onItemClicked(item) },
        modifier = modifier
    )
}


@Composable
fun GroupTitleItem(
    title: String,
    isOpen: Boolean,
    onGroupItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = modifier
            .clickable { onGroupItemClick() }
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        OnPrimaryBodyLargeText(text = title, modifier = Modifier.weight(1.0f))
        if (isOpen) {
            OnPrimaryArrowDownwardIcon()
        } else {
            OnPrimaryArrowForwardIcon()
        }
    }
}


@Preview(showBackground = true, backgroundColor = Color.BLUE.toLong(), name = "GroupTitleItem")
@Composable
fun GroupTitleItemPreview() {
    AppTheme {
        Column {
            GroupTitleItem(
                title = "状态",
                isOpen = true,
                onGroupItemClick = {}
            )
            StateItem(
                currentSelectedState = TorrentModel.STATE_ALL,
                item = StateModel(TorrentModel.STATE_ALL),
                onItemClicked = {})
        }

    }
}

@Composable
fun ContentScreen(
    torrentList: List<TorrentModel>? = null,
    onItemTorrentClick: onItemTorrentClick = {},
    onItemTorrentLongClick: onItemTorrentLongClick = {},
    onAddTorrentClick: () -> Unit = {},
    listState: LazyListState = rememberLazyListState()
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (torrentList.isNullOrEmpty()) {
            EmptyPage()
        } else {
            TorrentItemList(
                torrentList = torrentList,
                listState = listState,
                onItemTorrentClick = onItemTorrentClick,
                onItemTorrentLongClick = onItemTorrentLongClick
            )
        }
        PrimaryAddFloatingActionButton(
            onClick = { onAddTorrentClick() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp, end = 16.dp),
        )
    }
}

@Composable
fun TorrentItemList(
    torrentList: List<TorrentModel>,
    listState: LazyListState,
    onItemTorrentClick: onItemTorrentClick,
    modifier: Modifier = Modifier,
    onItemTorrentLongClick: onItemTorrentLongClick = {},
) {
    LazyColumn(
        modifier = modifier, state = listState
    ) {
        items(torrentList) { item ->
            TorrentItem(
                item = item,
                onItemTorrentClick = onItemTorrentClick,
                onItemTorrentLongClick = onItemTorrentLongClick,
                modifier = Modifier.fillParentMaxWidth()
            )
        }
    }
}

@Composable
fun TorrentItem(
    item: TorrentModel,
    onItemTorrentClick: onItemTorrentClick,
    modifier: Modifier = Modifier,
    onItemTorrentLongClick: onItemTorrentLongClick = {},
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = shapes.medium,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp, start = 16.dp, end = 16.dp)
            .combinedClickable(onClick = { onItemTorrentClick(item) },
                onLongClick = { onItemTorrentLongClick(item) }),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            PrimaryBodyMediumText(
                text = item.name!!,
                modifier = Modifier.fillMaxWidth()
            )
            SmailVerticalSpacer()
            Row(verticalAlignment = Alignment.CenterVertically) {
                val sizeText = String.format(
                    stringResource(id = R.string.qb_torrent_size_format),
                    UIUtil.getSizeText(item.selectedSize!!, scale = 1)
                )
                LabelMediumText(
                    text = sizeText
                )
                Spacer(modifier = Modifier.weight(1f))
                val createTimeText = String.format(
                    stringResource(id = R.string.qb_torrent_create_time_format),
                    DateUtil.format(item.createTime!!)
                )
                LabelMediumText(
                    text = createTimeText,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            SmailVerticalSpacer()
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (!item.category.isNullOrEmpty()) {
                    OutlinePrimaryLabelSmallText(
                        text = item.category!!,
                        modifier = Modifier.padding(end = 6.dp)
                    )
                }
                val tagText = item.getTagText()
                if (tagText.isNotEmpty()) {
                    FilledOnPrimaryLabelSmallText(
                        text = tagText,
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                LabelMediumText(
                    text = stringResource(id = QbUtil.getTorrentStateResId(item.state!!))
                )
            }

            SmailVerticalSpacer()
            Row(verticalAlignment = Alignment.CenterVertically) {
                PrimaryDownloadIcon()
                BodyMediumText(text = UIUtil.getSpeedText(item.downloadSpeed!!))
                PrimaryUploadIcon(modifier = Modifier.padding(start = 16.dp))
                BodyMediumText(
                    text = UIUtil.getSpeedText(item.uploadSpeed!!)
                )
                Spacer(modifier = Modifier.weight(1f))
                SmailHorizontalSpacer()
                LabelMediumText(
                    text = UIUtil.getProgressText(item.progress!!),
                )
            }

            SmailVerticalSpacer()

            LinearProgressIndicator(
                progress = { item.progress!! },
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.onPrimary,
            )

        }
    }
}

@Preview(showBackground = true, name = "TorrentItem")
@Composable
fun TorrentItemPreview() {
    AppTheme {
        TorrentItem(
            TorrentModel(
                name = "abcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabc",
                category = "abc",
                downloadSpeed = 100000L,
                uploadSpeed = 200000L,
                progress = 1f,
                ratio = 0.1
            ),
            onItemTorrentClick = {})
    }
}


@Preview(showBackground = true, name = "QbDetail")
@Composable
fun QbDetailPagePreview() {
    AppTheme {
        DetailDrawerContent(QbDetailUIState(
            qbDetailModel = QbDetailModel(
                qbInfo = QbInfoModel(
                    url = "http://192.168.2.110:8080/",
                    username = "hanzhifengyun",
                    password = "111111",
                    appVersion = "4.6.2",
//                    totalDownloadSize = 119591447171703L,
//                    totalUploadSize = 11959144717170L,
//                    totalDownloadSpeed = 799233L,
//                    totalUploadSpeed = 599233L,
                )
            )
        ),
            onStateItemClick = {}, onCategoryItemClick = {}, onTagItemClick = {})
    }
}