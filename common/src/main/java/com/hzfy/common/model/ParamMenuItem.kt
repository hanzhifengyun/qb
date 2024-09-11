package com.hzfy.common.model

import androidx.compose.runtime.saveable.mapSaver
import com.hzfy.ui.model.MenuItem

class ParamMenuItem(val name: String, val param: String) : MenuItem<String>(name, param) {


    companion object {

        val Saver = mapSaver<List<ParamMenuItem>>(
            save = { list ->
                val map = mutableMapOf<String, String>()
                list.forEach { item ->
                    item.apply {
                        map[name] = param
                    }
                }
                map

            },
            restore = { map ->
                val result = mutableListOf<ParamMenuItem>()
                for ((name, param) in map) {
                    result.add(ParamMenuItem(name = name, param = param as String))
                }
                result
            }
        )
    }
}

