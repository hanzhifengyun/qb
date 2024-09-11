package com.hzfy.qb.api

import com.hzfy.common.api.BaseRepository
import com.hzfy.database.entity.QbEntity
import com.hzfy.library.log.HzfyLog
import com.hzfy.qb.api.local.IQbLocalApi
import com.hzfy.qb.api.remote.IQbRemoteApi
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QbRepository @Inject constructor(
    private val remoteApi: IQbRemoteApi,
    private val localApi: IQbLocalApi
) : BaseRepository(), IQbRepository {

    override fun login(param: AddQbRequestParam): Flow<QbEntity> {
        return remoteApi.login(param)
    }
    override fun checkQbAvailable(addQbRequestParam: AddQbRequestParam): Flow<Boolean> {
        return remoteApi.checkQbAvailable(addQbRequestParam)
    }

    override fun selectAll(): Flow<List<QbEntity>> {
        return flow {
            emit(localApi.selectAll())
        }
    }

    override fun selectQbByUrl(url: String): Flow<QbEntity?> {
        return flow {
            emit(localApi.selectQbByUrl(url))
        }
    }

    override fun selectQbByUid(uid: Int): Flow<QbEntity?> {
        return flow {
            emit(localApi.selectQbByUid(uid))
        }
    }

    override fun insert(qb: QbEntity): Flow<Boolean> {
        return flow { emit(localApi.insert(qb)) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun saveQb(qb: QbEntity): Flow<Boolean> {
        return selectQbByUrl(qb.url)
            .flatMapConcat { result ->
                if (result == null) {
                    HzfyLog.i("saveQb: this is a new qb, call insert method")
                    insert(qb)
                } else {
                    HzfyLog.i("saveQb: this is a old qb, call update method")
                    qb.uid = result.uid
                    update(qb)
                }
            }
    }

    override fun update(qb: QbEntity): Flow<Boolean> {
        return flow { emit(localApi.update(qb)) }
    }

    override fun delete(qb: QbEntity): Flow<Boolean> {
        return flow { emit(localApi.delete(qb)) }
    }

    override fun getQbDetail(param: QbDetailRequestParam): Flow<QbDetailModel> {
        return remoteApi.getQbDetail(param)
    }

    override fun pauseTorrent(param: PauseTorrentRequestParam): Flow<Boolean> {
        return remoteApi.pauseTorrent(param)
    }

    override fun resumeTorrent(param: ResumeTorrentRequestParam): Flow<Boolean> {
        return remoteApi.resumeTorrent(param)
    }

    override fun deleteTorrent(param: DeleteTorrentRequestParam): Flow<Boolean> {
        return remoteApi.deleteTorrent(param)
    }

    override fun changeSavePathTorrent(param: ChangeSavePathTorrentRequestParam): Flow<Boolean> {
        return remoteApi.changeSavePathTorrent(param)
    }

    override fun getCategoryList(param: BaseQbParam): Flow<List<CategoryModel>> {
        return remoteApi.getCategoryList(param)
    }

    override fun addTorrents(param: AddTorrentsParam): Flow<Boolean> {
        return remoteApi.addTorrents(param)
    }

}