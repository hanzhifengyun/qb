package com.hzfy.ui.compose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.IndeterminateCheckBox
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun CheckBoxOutlineBlankIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(Icons.Filled.CheckBoxOutlineBlank, tint = tint, contentDescription = "CheckBoxOutlineBlank", modifier = modifier)
}

@Composable
fun OnSurfaceCheckBoxOutlineBlankIcon(modifier: Modifier = Modifier) {
    CheckBoxOutlineBlankIcon(tint = MaterialTheme.colorScheme.onSurface, modifier = modifier)
}

@Composable
fun PrimaryCheckBoxOutlineBlankIcon(modifier: Modifier = Modifier) {
    CheckBoxOutlineBlankIcon(tint = MaterialTheme.colorScheme.primary, modifier = modifier)
}

@Composable
fun OnSurfaceCheckBoxIcon(modifier: Modifier = Modifier) {
    CheckBoxIcon(tint = MaterialTheme.colorScheme.onSurface, modifier = modifier)
}

@Composable
fun IndeterminateCheckBoxIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(Icons.Filled.IndeterminateCheckBox, tint = tint, contentDescription = "IndeterminateCheckBox", modifier = modifier)
}

@Composable
fun CheckBoxIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(Icons.Filled.CheckBox, tint = tint, contentDescription = "CheckBox", modifier = modifier)
}

@Composable
fun PrimaryCheckBoxIcon(modifier: Modifier = Modifier) {
    CheckBoxIcon(tint = MaterialTheme.colorScheme.primary, modifier = modifier)
}

@Composable
fun DownloadingIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(Icons.Filled.Downloading, tint = tint, contentDescription = "Downloading", modifier = modifier)
}

@Composable
fun PrimaryDownloadingIcon(modifier: Modifier = Modifier) {
    DownloadingIcon(tint = MaterialTheme.colorScheme.primary, modifier = modifier)
}

@Composable
fun DownloadIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(Icons.Filled.Download, tint = tint, contentDescription = "Download", modifier = modifier)
}


@Composable
fun PrimaryDownloadIcon(modifier: Modifier = Modifier) {
    DownloadIcon(tint = MaterialTheme.colorScheme.primary, modifier = modifier)
}

@Composable
fun UploadIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(Icons.Filled.Upload, tint = tint, contentDescription = "Upload", modifier = modifier)
}

@Composable
fun PrimaryUploadIcon(modifier: Modifier = Modifier) {
    UploadIcon(tint = MaterialTheme.colorScheme.primary, modifier = modifier)
}

@Composable
fun CommentIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(Icons.AutoMirrored.Filled.Comment, tint = tint, contentDescription = "Comment", modifier = modifier)
}

@Composable
fun PrimaryCommentIcon(modifier: Modifier = Modifier) {
    CommentIcon(tint = MaterialTheme.colorScheme.primary, modifier = modifier)
}

@Composable
fun FavoriteIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(Icons.Filled.Favorite, tint = tint, contentDescription = "Favorite", modifier = modifier)
}

@Composable
fun PrimaryFavoriteIcon(modifier: Modifier = Modifier) {
    FavoriteIcon(tint = MaterialTheme.colorScheme.primary, modifier = modifier)
}

@Composable
fun UnFavoriteIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(Icons.Filled.FavoriteBorder, tint = tint, contentDescription = "UnFavoriteIcon", modifier = modifier)
}

@Composable
fun PrimaryUnFavoriteIcon(modifier: Modifier = Modifier) {
    UnFavoriteIcon(tint = MaterialTheme.colorScheme.primary, modifier = modifier)
}

@Composable
fun SearchIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(Icons.Filled.Search, tint = tint, contentDescription = "Search", modifier = modifier)
}

@Composable
fun OnPrimaryContainerSearchIcon(modifier: Modifier = Modifier) {
    SearchIcon(tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = modifier)
}

@Composable
fun ScheduleIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(Icons.Filled.Schedule, tint = tint, contentDescription = "Schedule", modifier = modifier)
}

@Composable
fun PrimaryScheduleIcon(modifier: Modifier = Modifier) {
    ScheduleIcon(tint = MaterialTheme.colorScheme.primary, modifier = modifier)
}

@Composable
fun CheckIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(Icons.Filled.Check, tint = tint, contentDescription = "Check", modifier = modifier)
}

@Composable
fun OnPrimaryContainerCheckIcon(modifier: Modifier = Modifier) {
    CheckIcon(tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = modifier)
}

@Composable
fun ArrowRightIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        Icons.AutoMirrored.Filled.ArrowRight,
        tint = tint,
        contentDescription = "ArrowRight",
        modifier = modifier
    )
}

@Composable
fun OnPrimaryArrowRightIcon(modifier: Modifier = Modifier) {
    ArrowRightIcon(tint = MaterialTheme.colorScheme.onPrimary, modifier = modifier)
}

@Composable
fun ArrowLeftIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        Icons.AutoMirrored.Filled.ArrowLeft,
        tint = tint,
        contentDescription = "ArrowLeft",
        modifier = modifier
    )
}

@Composable
fun OnPrimaryArrowLeftIcon(modifier: Modifier = Modifier) {
    ArrowLeftIcon(tint = MaterialTheme.colorScheme.onPrimary, modifier = modifier)
}


@Composable
fun ArrowForwardIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        Icons.AutoMirrored.Filled.ArrowForward,
        tint = tint,
        contentDescription = "ArrowForward",
        modifier = modifier
    )
}

@Composable
fun OnPrimaryArrowForwardIcon(modifier: Modifier = Modifier) {
    ArrowForwardIcon(tint = MaterialTheme.colorScheme.onPrimary, modifier = modifier)
}

@Composable
fun ArrowUpwardIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        Icons.Filled.ArrowUpward,
        tint = tint,
        contentDescription = "ArrowUpward",
        modifier = modifier
    )
}

@Composable
fun OnPrimaryArrowUpwardIcon(modifier: Modifier = Modifier) {
    ArrowUpwardIcon(tint = MaterialTheme.colorScheme.onPrimary, modifier = modifier)
}

@Composable
fun ArrowDownwardIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        Icons.Filled.ArrowDownward,
        tint = tint,
        contentDescription = "ArrowDownward",
        modifier = modifier
    )
}

@Composable
fun OnPrimaryArrowDownwardIcon(modifier: Modifier = Modifier) {
    ArrowDownwardIcon(tint = MaterialTheme.colorScheme.onPrimary, modifier = modifier)
}



@Composable
fun OnBackgroundArrowDownwardIcon(modifier: Modifier = Modifier) {
    ArrowDownwardIcon(tint = MaterialTheme.colorScheme.onBackground, modifier = modifier)
}

@Composable
fun AddIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(Icons.Filled.Add, tint = tint, contentDescription = "Add", modifier = modifier)
}

@Composable
fun OnPrimaryAddIcon(modifier: Modifier = Modifier) {
    AddIcon(tint = MaterialTheme.colorScheme.onPrimary, modifier = modifier)
}

@Composable
fun NavigationBackIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
        tint = tint,
        contentDescription = "ArrowBack", modifier = modifier
    )
}

@Composable
fun OnPrimaryNavigationBackIcon(modifier: Modifier = Modifier) {
    NavigationBackIcon(
        tint = MaterialTheme.colorScheme.onPrimary, modifier = modifier
    )
}

@Composable
fun OnPrimaryContainerNavigationBackIcon(modifier: Modifier = Modifier) {
    NavigationBackIcon(
        tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = modifier
    )
}

@Composable
fun RefreshIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        imageVector = Icons.Filled.Refresh,
        tint = tint,
        contentDescription = "Refresh", modifier = modifier
    )
}

@Composable
fun PrimaryRefreshIcon(modifier: Modifier = Modifier) {
    RefreshIcon(
        tint = MaterialTheme.colorScheme.primary, modifier = modifier
    )
}

@Composable
fun OnPrimaryRefreshIcon(modifier: Modifier = Modifier) {
    RefreshIcon(
        tint = MaterialTheme.colorScheme.onPrimary, modifier = modifier
    )
}


@Composable
fun MenuIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        imageVector = Icons.AutoMirrored.Filled.List,
        tint = tint,
        contentDescription = "List", modifier = modifier
    )
}

@Composable
fun OnPrimaryMenuIcon(modifier: Modifier = Modifier) {
    MenuIcon(
        tint = MaterialTheme.colorScheme.onPrimary, modifier = modifier
    )
}

@Composable
fun OnPrimaryContainerMenuIcon(modifier: Modifier = Modifier) {
    MenuIcon(
        tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = modifier
    )
}
