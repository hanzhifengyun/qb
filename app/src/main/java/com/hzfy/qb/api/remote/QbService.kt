package com.hzfy.qb.api.remote

import android.net.Uri
import com.hzfy.library.net.HzfyCall
import com.hzfy.library.net.annotation.BaseUrl
import com.hzfy.library.net.annotation.FilePath
import com.hzfy.library.net.annotation.Filed
import com.hzfy.library.net.annotation.GET
import com.hzfy.library.net.annotation.MultiPOST
import com.hzfy.library.net.annotation.POST
import com.hzfy.qb.QbConfig
import com.hzfy.qb.api.result.QbAppPreferenceResponse
import com.hzfy.qb.api.result.detail.CategoryResponse
import com.hzfy.qb.api.result.detail.QbDetailResponse
import javax.inject.Singleton


@Singleton
interface QbService {

    @POST(QbConfig.URL_API + "auth/login")
    fun login(
        @BaseUrl("baseUrl") baseUrl: String,
        @Filed("username") username: String?,
        @Filed("password") password: String
    ): HzfyCall<String>


    @POST(QbConfig.URL_API_APP + "version")
    fun getAppVersion(@BaseUrl("baseUrl") baseUrl: String): HzfyCall<String>

    @POST(QbConfig.URL_API_APP + "preferences")
    fun getAppPreferences(@BaseUrl("baseUrl") baseUrl: String): HzfyCall<QbAppPreferenceResponse>


    @GET(QbConfig.URL_API + "sync/maindata")
    fun getMainData(
        @BaseUrl("baseUrl") baseUrl: String,
        @Filed("rid") rid: Long,
    ): HzfyCall<QbDetailResponse>


    /**
     *
     * @param baseUrl qb baseUrl
     * @param hashes  The hashes of the torrents you want to delete.
     *                 hashes can contain multiple hashes separated by |,
     *                 to delete multiple torrents, or set to all, to delete all torrents.
     * @return empty
     */
    @POST(QbConfig.URL_API_TORRENTS + "pause")
    fun pauseTorrent(
        @BaseUrl("baseUrl") baseUrl: String,
        @Filed("hashes") hashes: String
    ): HzfyCall<String>

    @POST(QbConfig.URL_API_TORRENTS + "resume")
    fun resumeTorrent(
        @BaseUrl("baseUrl") baseUrl: String,
        @Filed("hashes") hashes: String
    ): HzfyCall<String>

    @POST(QbConfig.URL_API_TORRENTS + "delete")
    fun deleteTorrent(
        @BaseUrl("baseUrl") baseUrl: String,
        @Filed("hashes") hashes: String,
        @Filed("deleteFiles") deleteFiles: Boolean,//If set to true, the downloaded data will also be deleted, otherwise has no effect.
    ): HzfyCall<String>

    @POST(QbConfig.URL_API_TORRENTS + "setLocation")
    fun setLocation(
        @BaseUrl("baseUrl") baseUrl: String,
        @Filed("hashes") hashes: String,
        @Filed("location") location: String,
    ): HzfyCall<String>

    @POST(QbConfig.URL_API_TORRENTS + "categories")
    fun getCategories(@BaseUrl("baseUrl") baseUrl: String): HzfyCall<Map<String, CategoryResponse>?>



    @MultiPOST(QbConfig.URL_API_TORRENTS + "add")
    fun addTorrents(
        @BaseUrl("baseUrl") baseUrl: String,
        @FilePath("fileselect") filepathList: List<Uri>,
        @Filed("autoTMM") autoTMM: Boolean,
        @Filed("savepath") savePath: String,
        @Filed("rename") rename: String,
        @Filed("category") category: String,
        @Filed("paused") paused: Boolean,
        @Filed("stopCondition") stopCondition: String, //None, MetadataReceived(已收到元数据)，FilesChecked（已检查的文件）
        @Filed("contentLayout") contentLayout: String, //Original, Subfolder（创建子文件夹），NoSubfolder（不创建子文件夹）
        @Filed("dlLimit") dlLimit: String, //NaN, 1024
        @Filed("upLimit") upLimit: String, //NaN, 1024
    ): HzfyCall<String>

    @POST(QbConfig.URL_API_TORRENTS + "add")
    fun addTorrentsByUrl(
        @BaseUrl("baseUrl") baseUrl: String,
        @Filed("urls") urls: String,
        @Filed("cookie") cookie: String,
        @Filed("autoTMM") autoTMM: Boolean,
        @Filed("savepath") savePath: String,
        @Filed("rename") rename: String,
        @Filed("category") category: String,
        @Filed("paused") paused: Boolean,
        @Filed("stopCondition") stopCondition: String, //None, MetadataReceived(已收到元数据)，FilesChecked（已检查的文件）
        @Filed("contentLayout") contentLayout: String, //Original, Subfolder（创建子文件夹），NoSubfolder（不创建子文件夹）
        @Filed("dlLimit") dlLimit: String, //NaN, 1024
        @Filed("upLimit") upLimit: String, //NaN, 1024
    ): HzfyCall<String>
}