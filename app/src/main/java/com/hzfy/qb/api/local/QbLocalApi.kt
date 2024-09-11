package com.hzfy.qb.api.local

import com.hzfy.common.api.BaseLocalApi
import com.hzfy.database.dao.QbDao
import com.hzfy.database.entity.QbEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QbLocalApi @Inject constructor() : BaseLocalApi(), IQbLocalApi {
    @Inject
    lateinit var mDao: QbDao
    override fun selectAll(): List<QbEntity> {
        return mDao.selectAll()
    }

    override fun selectQbByUrl(url: String): QbEntity? {
        return mDao.selectQbByUrl(url)
    }

    override fun selectQbByUid(uid: Int): QbEntity? {
        return mDao.selectQbByUid(uid)
    }

    override fun insert(qb: QbEntity): Boolean {
        return mDao.insert(qb) > 0
    }

    override fun update(qb: QbEntity): Boolean {
        return mDao.update(qb) > 0
    }

    override fun delete(qb: QbEntity): Boolean {
        return mDao.delete(qb) > 0
    }

}