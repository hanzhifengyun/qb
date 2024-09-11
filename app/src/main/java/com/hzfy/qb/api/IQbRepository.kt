package com.hzfy.qb.api

import com.hzfy.database.entity.QbEntity
import com.hzfy.qb.api.request.AddQbRequestParam
import com.hzfy.qb.api.request.AddTorrentsParam
import com.hzfy.qb.api.request.BaseQbParam
import com.hzfy.qb.api.request.ChangeSavePathTorrentRequestParam
import com.hzfy.qb.api.request.DeleteTorrentRequestParam
import com.hzfy.qb.api.request.PauseTorrentRequestParam
import com.hzfy.qb.api.request.QbDetailRequestParam
import com.hzfy.qb.api.request.ResumeTorrentRequestParam
import com.hzfy.qb.model.detail.CategoryModel
import com.hzfy.qb.model.detail.QbDetailModel
import kotlinx.coroutines.flow.Flow

interface IQbRepository {
    fun login(param: AddQbRequestParam): Flow<QbEntity>
    fun checkQbAvailable(addQbRequestParam: AddQbRequestParam): Flow<Boolean>
    fun selectAll(): Flow<List<QbEntity>>

    fun selectQbByUrl(url: String): Flow<QbEntity?>

    fun selectQbByUid(uid: Int): Flow<QbEntity?>

    fun insert(qb: QbEntity): Flow<Boolean>

    fun update(qb: QbEntity): Flow<Boolean>

    fun saveQb(qb: QbEntity): Flow<Boolean>

    fun delete(qb: QbEntity): Flow<Boolean>

    fun getQbDetail(param: QbDetailRequestParam): Flow<QbDetailModel>
    fun pauseTorrent(param: PauseTorrentRequestParam): Flow<Boolean>
    fun resumeTorrent(param: ResumeTorrentRequestParam): Flow<Boolean>
    fun deleteTorrent(param: DeleteTorrentRequestParam): Flow<Boolean>
    fun changeSavePathTorrent(param: ChangeSavePathTorrentRequestParam): Flow<Boolean>
    fun getCategoryList(param: BaseQbParam): Flow<List<CategoryModel>>

    fun addTorrents(param: AddTorrentsParam): Flow<Boolean>
}