package com.hzfy.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.hzfy.ui.compose.text.OnPrimaryTitleLargeText
import com.hzfy.ui.theme.AppTheme

data class TopBarConfig(
    val showLeftIcon: Boolean = false,
    val leftIconContent: @Composable () -> Unit = { },
    val onLeftIconClick: () -> Unit = {},
    val showRightIcon: Boolean = false,
    val rightIconContent: @Composable () -> Unit = { },
    val onRightIconClick: () -> Unit = {}
) {
    companion object {
        val DEFAULT = TopBarConfig(
            leftIconContent = { OnPrimaryNavigationBackIcon() },
            rightIconContent = { OnPrimaryMenuIcon() }
        )
    }
}

@Composable
private fun CustomTopBar(
    title: String,
    titleCenterAligned: Boolean = true,
    topBarConfig: TopBarConfig = TopBarConfig.DEFAULT
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary)
            .windowInsetsPadding(WindowInsets.statusBars)
            .height(56.dp)
            .padding(horizontal = 6.dp)
    ) {

        val (titleText, leftIcon, rightIcon) = createRefs()

        if (topBarConfig.showLeftIcon) {
            IconButton(onClick = { topBarConfig.onLeftIconClick() },
                modifier = Modifier.constrainAs(leftIcon) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }) {
                topBarConfig.leftIconContent()
            }
        }

        OnPrimaryTitleLargeText(text = title,
            maxLines = 1,
            modifier = Modifier
                .constrainAs(titleText) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    if (titleCenterAligned) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    } else if (topBarConfig.showLeftIcon) {
                        start.linkTo(leftIcon.end)
                    } else {
                        start.linkTo(parent.start, margin = 10.dp)
                    }
                }
        )


        if (topBarConfig.showRightIcon) {
            IconButton(onClick = { topBarConfig.onRightIconClick() },
                modifier = Modifier.constrainAs(rightIcon) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }) {
                topBarConfig.rightIconContent()
            }
        }
    }
}


@Composable
fun CenterAlignedTopBar(
    title: String,
    topBarConfig: TopBarConfig = TopBarConfig.DEFAULT
) {
    CustomTopBar(
        title = title,
        titleCenterAligned = true,
        topBarConfig = topBarConfig,
    )
}

@Composable
fun LeftAlignedTopBar(
    title: String,
    topBarConfig: TopBarConfig = TopBarConfig.DEFAULT
) {
    CustomTopBar(
        title = title,
        titleCenterAligned = false,
        topBarConfig = topBarConfig,
    )
}


@Preview
@Composable
fun CommonTopBarPreview() {
    AppTheme {
        Column {
            CenterAlignedTopBar(
                title = "标题",
                topBarConfig = TopBarConfig.DEFAULT
            )
            CenterAlignedTopBar(
                title = "标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题题标题标题",
                topBarConfig = TopBarConfig.DEFAULT
                    .copy(
                        showLeftIcon = true,
                        showRightIcon = true
                    )
            )
            CenterAlignedTopBar(
                title = "标题",
                topBarConfig = TopBarConfig.DEFAULT
                    .copy(
                        showLeftIcon = true,
                        showRightIcon = true
                    )
            )
            LeftAlignedTopBar(
                title = "标题",
                topBarConfig = TopBarConfig.DEFAULT
            )
            LeftAlignedTopBar(
                title = "标题",
                topBarConfig = TopBarConfig.DEFAULT
                    .copy(
                        showLeftIcon = true,
                        showRightIcon = true
                    )
            )
        }
    }
}