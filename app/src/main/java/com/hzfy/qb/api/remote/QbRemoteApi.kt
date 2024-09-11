package com.hzfy.qb.api.remote

import com.hzfy.database.entity.QbEntity
import com.hzfy.library.net.HzfyCall
import com.hzfy.library.net.exception.HttpStatusCodeException
import com.hzfy.library.net.exception.RemoteServiceException
import com.hzfy.qb.api.request.AddQbRequestParam
import com.hzfy.qb.api.request.AddTorrentsParam
import com.hzfy.qb.api.request.BaseQbParam
import com.hzfy.qb.api.request.ChangeSavePathTorrentRequestParam
import com.hzfy.qb.api.request.DeleteTorrentRequestParam
import com.hzfy.qb.api.request.PauseTorrentRequestParam
import com.hzfy.qb.api.request.QbDetailRequestParam
import com.hzfy.qb.api.request.ResumeTorrentRequestParam
import com.hzfy.qb.api.result.QbResult
import com.hzfy.qb.model.detail.CategoryModel
import com.hzfy.qb.model.detail.QbDetailModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retry
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class QbRemoteApi @Inject constructor() : BaseQbRemoteApi(), IQbRemoteApi {

    @Inject
    lateinit var mService: QbService

    private fun <T> getQbFlowResult(qbEntity: QbEntity, resource: () -> HzfyCall<T>): Flow<T?> {
        return getFlowResult(resource)
            .retry(1) { cause ->
                if (cause is HttpStatusCodeException) {
                    val statusCode = cause.statusCode
                    if (statusCode == 403 && cause.message == QbResult.QB_ERROR_FORBIDDEN) {
                        val result = getResult {
                            mService.login(
                                baseUrl = qbEntity.url,
                                username = qbEntity.username,
                                password = qbEntity.password
                            )
                        }
                        return@retry true
                    }
                }
                return@retry false
            }
    }


    override fun login(param: AddQbRequestParam): Flow<QbEntity> {
        param.apply {
            return getFlowResult {
                mService.login(baseUrl = url, username = username, password = password)
            }.map {
                val version = getResult {
                    mService.getAppVersion(url)
                }
                val preferences = getResult {
                    mService.getAppPreferences(url)
                }
                val qbEntity = QbEntity(
                    url = url,
                    username = username,
                    password = password,
                )
                qbEntity.apply {
                    appVersion = version ?: "unknown version"
                    preferences?.apply {
                        save_path?.let { savePath = it }
                        up_limit?.let { uploadLimit = it }
                        dl_limit?.let { downloadLimit = it }
                    }
                }
            }
        }
    }

    override fun checkQbAvailable(param: AddQbRequestParam): Flow<Boolean> {
        param.apply {
            return getFlowResult {
                mService.login(baseUrl = url, username = username, password = password)
            }.map {
                true
            }.catch {
                emit(false)
            }
        }
    }


    override fun getQbDetail(param: QbDetailRequestParam): Flow<QbDetailModel> {
        param.qb.apply {
            return getQbFlowResult(qbEntity = param.qb) {
                mService.getMainData(url, param.rid)
            }.map { response ->
                if (response == null) {
                    throw RemoteServiceException(message = "response is empty")
                }
                QbDetailModel.convertByResponse(response, param.qb, param.rid > 0)
            }
        }
    }

    override fun pauseTorrent(param: PauseTorrentRequestParam): Flow<Boolean> {
        param.qb.apply {
            return getQbFlowResult(qbEntity = param.qb) {
                mService.pauseTorrent(url, param.getHashesParam())
            }.map { true }
        }
    }

    override fun resumeTorrent(param: ResumeTorrentRequestParam): Flow<Boolean> {
        param.qb.apply {
            return getQbFlowResult(qbEntity = param.qb) {
                mService.resumeTorrent(url, param.getHashesParam())
            }.map { true }
        }
    }

    override fun deleteTorrent(param: DeleteTorrentRequestParam): Flow<Boolean> {
        param.qb.apply {
            return getQbFlowResult(qbEntity = param.qb) {
                mService.deleteTorrent(
                    url,
                    param.getHashesParam(),
                    param.deleteFiles
                )
            }.map { true }
        }
    }

    override fun changeSavePathTorrent(param: ChangeSavePathTorrentRequestParam): Flow<Boolean> {
        param.qb.apply {
            return getQbFlowResult(qbEntity = param.qb) {
                mService.setLocation(url, param.getHashesParam(), param.newPath)
            }.map { true }
        }
    }

    override fun getCategoryList(param: BaseQbParam): Flow<List<CategoryModel>> {
        param.qb.apply {
            return getQbFlowResult(qbEntity = param.qb) {
                mService.getCategories(url)
            }.map { map ->
                val categoryList: MutableList<CategoryModel> = mutableListOf()
                map?.let {
                    for ((_, category) in map) {
                        categoryList.add(
                            CategoryModel(
                                name = category.name,
                                savePath = category.savePath
                            )
                        )
                    }
                }
                categoryList
            }.catch {
                val categoryList: MutableList<CategoryModel> = mutableListOf()
                emit(categoryList)
            }
        }
    }

    override fun addTorrents(param: AddTorrentsParam): Flow<Boolean> {
        param.apply {
            return getQbFlowResult(qbEntity = qb) {
                mService.addTorrents(
                    baseUrl = qb.url,
                    filepathList = filepathList,
                    autoTMM = autoTMM,
                    savePath = savePath,
                    rename = rename,
                    stopCondition = getStopConditionParam(),
                    contentLayout = getContentLayoutParam(),
                    category = category,
                    paused = paused,
                    dlLimit = getDownloadSpeedLimitParam(),
                    upLimit = getUploadSpeedLimitParam()
                )
            }.map { true }
        }
    }
}