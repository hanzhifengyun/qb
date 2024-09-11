package com.hzfy.library.ext

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


fun ViewModel.launchIO(
    exceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    },
    block: suspend CoroutineScope.() -> Unit
): Job = viewModelScope.launch(Dispatchers.IO + exceptionHandler, block = block)


fun ViewModel.launchDefault(
    exceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    },
    block: suspend CoroutineScope.() -> Unit
): Job = viewModelScope.launch(Dispatchers.Default + exceptionHandler, block = block)

fun ViewModel.launchMain(
    exceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    },
    block: suspend CoroutineScope.() -> Unit
): Job = viewModelScope.launch(Dispatchers.Main + exceptionHandler, block = block)