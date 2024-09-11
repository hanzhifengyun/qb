package com.hzfy.qb.model.detail

import com.hzfy.database.entity.QbEntity
import com.hzfy.qb.api.result.detail.CategoryResponse
import com.hzfy.qb.api.result.detail.QbDetailResponse
import com.hzfy.qb.api.result.detail.ServerState
import com.hzfy.qb.api.result.detail.Torrent

data class QbDetailModel(
    val qbInfo: QbInfoModel,
    var rid: Long = 0L,
) {
    fun update(qbDetail: QbDetailModel): QbDetailModel {
        //qb信息
        this.qbInfo.update(qbDetail.qbInfo)
        this.rid = qbDetail.rid

        //种子信息
        // 使用Map来存储当前的种子,以便快速查找
        val currentTorrents = this.torrentList.associateBy { it.hash }

        qbDetail.torrentList.forEach { item ->


            var isTorrentExist = false
            this.torrentList.forEach { currentItem ->

                if (item.hash == currentItem.hash) {
                    isTorrentExist = true
                    val currentState = currentItem.state!!
                    val currentCategory = currentItem.getRealCategory()
                    val currentTagList = currentItem.tagList
                    currentItem.update(item)
                    val newState = currentItem.state!!
                    val newCategory = currentItem.getRealCategory()
                    val newTagList = currentItem.tagList
                    if (currentState != newState) {
                        //状态改变
                        updateStateCount(currentState, isAdd = false)
                        updateStateCount(newState)
                    }

                    if (currentCategory != newCategory) {
                        //分类改变
                        updateCategoryCount(currentCategory, isAdd = false)
                        updateCategoryCount(newCategory)
                    }

                    if (currentTagList.toString() != newTagList.toString()) {
                        //tag改变
                        if (currentTagList.isEmpty()) {
                            updateTagCount(TagModel.NONE, isAdd = false)
                        }
                        if (newTagList.isEmpty()) {
                            updateTagCount(TagModel.NONE)
                        }

                        val removeTagMap = mutableMapOf<String, Boolean>()
                        val savedTagList = mutableSetOf<String>()
                        newTagList.forEach { newTag ->
                            var isTagExists = false
                            currentTagList.forEach { tag ->
                                if (tag == newTag) {
                                    isTagExists = true
                                    savedTagList.add(tag)
                                    removeTagMap[newTag] = false
                                } else {
                                    val needRemove1 = removeTagMap[tag]
                                    if (needRemove1 == null) {
                                        removeTagMap[tag] = true
                                    }
                                }
                            }

                            if (!isTagExists) {
                                updateTagCount(newTag)
                            }
                        }
                        for ((tag, needRemove) in removeTagMap) {
                            if (needRemove) {
                                updateTagCount(tag, isAdd = false)
                            }
                        }
                    }


                }
            }
            if (!isTorrentExist) {
                updateItem(item)
                this.torrentList.add(item)
            }
        }
        val removeTorrentList = mutableListOf<TorrentModel>()

        this.torrentList.forEach { currentItem ->
            if (!qbDetail.removeTorrentHashList.isNullOrEmpty()) {
                if (qbDetail.removeTorrentHashList!!.contains(currentItem.hash)) {
                    if (!removeTorrentList.contains(currentItem)) {
                        removeTorrentList.add(currentItem)
                    }
                }
            }
        }


        removeTorrentList.forEach { item->
            updateItem(item, false)
            this.torrentList.remove(item)
        }

        //state
        this.stateList[0].count = this.torrentList.size
        for ((name, changeCount) in updateStateCountMap) {

            this.stateList.forEach { state ->
                if (name == state.state) {
                    var count = state.count
                    count += changeCount
                    state.count = count
                }
            }

        }
        updateStateCountMap.clear()

        //category
        this.categoryList[0].count = this.torrentList.size

        for ((name, changeCount) in updateCategoryCountMap) {
            var isCategoryExists = false
            this.categoryList.forEach { category ->
                if (name == category.name) {
                    isCategoryExists = true

                    category.addCount(changeCount)
                }
            }
            if (!isCategoryExists) {
                this.categoryList.add(CategoryModel(name = name, count = changeCount))
            }
        }
        updateCategoryCountMap.clear()
        //tag
        this.tagList[0].count = this.torrentList.size
        for ((name, changeCount) in updateTagCountMap) {
            var isTagExists = false
            this.tagList.forEach { tag ->
                if (name == tag.name) {
                    isTagExists = true
                    tag.addCount(changeCount)
                }
            }
            if (!isTagExists) {
                this.tagList.add(TagModel(name = name, count = changeCount))
            }
        }

        updateTagCountMap.clear()
        return this
    }

    private fun updateItem(item: TorrentModel, isAdd: Boolean = true) {
        item.apply {
            updateStateCount(state!!, isAdd = isAdd)

            val categoryName = getRealCategory()

            updateCategoryCount(categoryName, isAdd = isAdd)

            if (tagList.isEmpty()) {
                updateTagCount(TagModel.NONE, isAdd = isAdd)
            } else {
                tagList.forEach { tag ->
                    updateTagCount(tag, isAdd = isAdd)
                }
            }
        }
    }

    private fun updateStateCount(name: String, isAdd: Boolean = true) {
        var count = updateStateCountMap[name] ?: 0
        if (isAdd) {
            count++
        } else {
            count--
        }
        updateStateCountMap[name] = count
    }

    private fun updateCategoryCount(name: String, isAdd: Boolean = true) {
        var count = updateCategoryCountMap[name] ?: 0
        if (isAdd) {
            count++
        } else {
            count--
        }
        updateCategoryCountMap[name] = count
    }

    private fun updateTagCount(name: String, isAdd: Boolean = true) {
        var count = updateTagCountMap[name] ?: 0
        if (isAdd) {
            count++
        } else {
            count--
        }
        updateTagCountMap[name] = count
    }

    val stateList: MutableList<StateModel> = mutableListOf()
    val torrentList: MutableList<TorrentModel> = mutableListOf()
    val categoryList: MutableList<CategoryModel> = mutableListOf()
    val tagList: MutableList<TagModel> = mutableListOf()

    val categoryCountMap: MutableMap<String, Int> = mutableMapOf()
    val tagCountMap: MutableMap<String, Int> = mutableMapOf()

    var removeTorrentHashList: List<String>? = null
    var removeTorrentTrackersList: List<String>? = null

    private val updateStateCountMap: MutableMap<String, Int> = mutableMapOf()
    private val updateCategoryCountMap: MutableMap<String, Int> = mutableMapOf()
    private val updateTagCountMap: MutableMap<String, Int> = mutableMapOf()

    companion object {
        fun convertByResponse(
            response: QbDetailResponse,
            qb: QbEntity,
            isUpdate: Boolean = false
        ): QbDetailModel {

            response.run {
                val qbInfo = parseQbInfo(server_state, qb)


                val qbDetail = QbDetailModel(qbInfo, rid = rid)
                if (isUpdate) {
                    updateOthers(qbDetail, response)
                } else {
                    parseTorrentsAndStates(qbDetail, torrents)
                    parseCategories(qbDetail, categories)
                    parseTags(qbDetail, tags)
                }
                return qbDetail
            }
        }

        private fun updateOthers(qbDetail: QbDetailModel, response: QbDetailResponse) {
            qbDetail.removeTorrentHashList = response.torrents_removed
            qbDetail.removeTorrentTrackersList = response.trackers_removed

            qbDetail.apply {
                response.apply {
                    torrents?.let {
                        for ((uid, torrent) in torrents) {
                            val torrentModel = torrent.convertToTorrentModel(uid)
                            torrent.apply {
                                if (!tags.isNullOrEmpty()) {
                                    val tagList = tags.split(", ")
                                    tagList.forEach { tag ->
                                        var tagCount = tagCountMap[tag]
                                        if (tagCount != null) {
                                            tagCount++
                                            tagCountMap[tag] = tagCount
                                        } else {
                                            tagCountMap[tag] = 1
                                        }
                                    }
                                    torrentModel.tagList.addAll(tagList)
                                }
                            }
                            torrentList.add(torrentModel)
                        }


                    }

                }
            }
        }

        private fun parseQbInfo(server: ServerState?, qb: QbEntity): QbInfoModel {
            val qbInfo = QbInfoModel(
                url = qb.url,
                username = qb.username,
                password = qb.password,
                appVersion = qb.appVersion,
            )
            return qbInfo.apply {
                server?.apply {
                    totalDownloadSize = alltime_dl
                    totalUploadSize = alltime_ul
                    totalDownloadSpeed = dl_info_speed
                    totalUploadSpeed = up_info_speed
                    freeDiskSpace = free_space_on_disk
                }

            }
        }

        private fun parseTags(qbDetail: QbDetailModel, tags: List<String>?) {
            qbDetail.apply {
                val allTags = TagModel(
                    name = TagModel.ALL,
                    count = tagCountMap[TagModel.ALL] ?: 0,
                )
                val noneTags = TagModel(
                    name = TagModel.NONE,
                    count = tagCountMap[TagModel.NONE] ?: 0,
                )

                tagList.add(allTags)
                tagList.add(noneTags)

                tags?.let {
                    tags.forEach {
                        val tagModel = TagModel(name = it, count = tagCountMap[it] ?: 0)
                        tagList.add(tagModel)
                    }
                }
            }
        }

        private fun parseCategories(
            qbDetail: QbDetailModel,
            categories: Map<String, CategoryResponse>?
        ) {
            qbDetail.apply {
                val allCategories = CategoryModel(
                    name = CategoryModel.ALL,
                    count = categoryCountMap[CategoryModel.ALL] ?: 0,
                )
                val noneCategories = CategoryModel(
                    name = CategoryModel.NONE,
                    count = categoryCountMap[CategoryModel.NONE] ?: 0,
                )

                categoryList.add(allCategories)
                categoryList.add(noneCategories)
                categories?.let {
                    for ((_, category) in categories) {
                        category.run {
                            val categoryModel = CategoryModel(
                                name = name,
                                count = categoryCountMap[name] ?: 0,
                                savePath = savePath
                            )
                            categoryList.add(categoryModel)
                        }

                    }
                }
            }
        }

        private fun parseTorrentsAndStates(
            qbDetail: QbDetailModel,
            torrents: Map<String, Torrent>?
        ) {
            val allState = StateModel(state = TorrentModel.STATE_ALL)
            val errorState = StateModel(state = TorrentModel.STATE_ERROR)
            val missingFilesState = StateModel(state = TorrentModel.STATE_MISSING_FILES)
            val uploadingState = StateModel(state = TorrentModel.STATE_UPLOADING)
            val pausedUPState = StateModel(state = TorrentModel.STATE_PAUSED_UP)
            val queuedUPState = StateModel(state = TorrentModel.STATE_QUEUED_UP)
            val stalledUpState = StateModel(state = TorrentModel.STATE_STALLED_UP)
            val checkingUPState = StateModel(state = TorrentModel.STATE_CHECKING_UP)
            val forcedUPState = StateModel(state = TorrentModel.STATE_FORCED_UP)
            val allocatingState = StateModel(state = TorrentModel.STATE_ALLOCATING)
            val downloadingState = StateModel(state = TorrentModel.STATE_DOWNLOADING)
            val metaDLState = StateModel(state = TorrentModel.STATE_META_DL)
            val pausedDLState = StateModel(state = TorrentModel.STATE_PAUSED_DL)
            val queuedDLState = StateModel(state = TorrentModel.STATE_QUEUED_DL)
            val stalledDLState = StateModel(state = TorrentModel.STATE_STALLED_DL)
            val checkingDLState = StateModel(state = TorrentModel.STATE_CHECKING_DL)
            val forcedDLState = StateModel(state = TorrentModel.STATE_FORCED_DL)
            val checkingResumeDataState =
                StateModel(state = TorrentModel.STATE_CHECKING_RESUME_DATA)
            val movingState = StateModel(state = TorrentModel.STATE_MOVING)
            val unknownState = StateModel(state = TorrentModel.STATE_UNKNOWN)
            qbDetail.apply {
                //分类

                var noneCategoryCount = 0
                var noneTagCount = 0

                torrents?.let {
                    for ((uid, torrent) in torrents) {
                        val torrentModel = torrent.convertToTorrentModel(uid)
                        torrent.apply {
                            //状态
                            allState.addCount()
                            when (state) {
                                TorrentModel.STATE_ERROR -> {
                                    errorState.addCount()
                                }

                                TorrentModel.STATE_MISSING_FILES -> {
                                    missingFilesState.addCount()
                                }

                                TorrentModel.STATE_UPLOADING -> {
                                    uploadingState.addCount()
                                }

                                TorrentModel.STATE_PAUSED_UP -> {
                                    pausedUPState.addCount()
                                }

                                TorrentModel.STATE_QUEUED_UP -> {
                                    queuedUPState.addCount()
                                }

                                TorrentModel.STATE_STALLED_UP -> {
                                    stalledUpState.addCount()
                                }

                                TorrentModel.STATE_CHECKING_UP -> {
                                    checkingUPState.addCount()
                                }

                                TorrentModel.STATE_FORCED_UP -> {
                                    forcedUPState.addCount()
                                }

                                TorrentModel.STATE_ALLOCATING -> {
                                    allocatingState.addCount()
                                }

                                TorrentModel.STATE_DOWNLOADING -> {
                                    downloadingState.addCount()
                                }

                                TorrentModel.STATE_META_DL -> {
                                    metaDLState.addCount()
                                }

                                TorrentModel.STATE_PAUSED_DL -> {
                                    pausedDLState.addCount()
                                }

                                TorrentModel.STATE_QUEUED_DL -> {
                                    queuedDLState.addCount()
                                }

                                TorrentModel.STATE_STALLED_DL -> {
                                    stalledDLState.addCount()
                                }

                                TorrentModel.STATE_CHECKING_DL -> {
                                    checkingDLState.addCount()
                                }

                                TorrentModel.STATE_FORCED_DL -> {
                                    forcedDLState.addCount()
                                }

                                TorrentModel.STATE_CHECKING_RESUME_DATA -> {
                                    checkingResumeDataState.addCount()
                                }

                                TorrentModel.STATE_MOVING -> {
                                    movingState.addCount()
                                }

                                else -> {
                                    unknownState.addCount()
                                }
                            }

                            //分类
                            if (category.isNullOrEmpty()) {
                                noneCategoryCount++
                            } else {
                                var categoryCount = categoryCountMap[category]
                                if (categoryCount != null) {
                                    categoryCount++
                                    categoryCountMap[category] = categoryCount
                                } else {
                                    categoryCountMap[category] = 1
                                }
                            }

                            //标签"HIXhvGEnYc, 刷流, 已整理"
                            if (tags.isNullOrEmpty()) {
                                noneTagCount++
                            } else {
                                val tagList = tags.split(", ")
                                tagList.forEach { tag ->
                                    var tagCount = tagCountMap[tag]
                                    if (tagCount != null) {
                                        tagCount++
                                        tagCountMap[tag] = tagCount
                                    } else {
                                        tagCountMap[tag] = 1
                                    }
                                }
                                torrentModel.tagList.addAll(tagList)
                            }
                            torrentList.add(torrentModel)
                        }

                    }
                }

                //分类
                categoryCountMap[CategoryModel.ALL] = torrentList.size
                categoryCountMap[CategoryModel.NONE] = noneCategoryCount
                //标签
                tagCountMap[TagModel.ALL] = torrentList.size
                tagCountMap[TagModel.NONE] = noneTagCount
                //状态

                stateList.add(allState)
                stateList.add(downloadingState)
                stateList.add(uploadingState)
                stateList.add(stalledUpState)
                stateList.add(pausedDLState)
                stateList.add(stalledDLState)
                stateList.add(errorState)
                stateList.add(movingState)
                stateList.add(missingFilesState)
                stateList.add(pausedUPState)
                stateList.add(queuedUPState)
                stateList.add(checkingUPState)
                stateList.add(forcedUPState)
                stateList.add(allocatingState)
                stateList.add(metaDLState)
                stateList.add(queuedDLState)
                stateList.add(checkingDLState)
                stateList.add(forcedDLState)
                stateList.add(checkingResumeDataState)
                stateList.add(unknownState)


            }
        }
    }
}



