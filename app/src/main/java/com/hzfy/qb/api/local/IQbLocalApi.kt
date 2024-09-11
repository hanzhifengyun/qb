package com.hzfy.qb.api.local


import com.hzfy.database.entity.QbEntity

interface IQbLocalApi {

    fun selectAll() : List<QbEntity>


    fun selectQbByUrl(url: String) : QbEntity?

    fun selectQbByUid(uid: Int) : QbEntity?

    fun insert(qb: QbEntity): Boolean


    fun update(qb: QbEntity): Boolean


    fun delete(qb: QbEntity): Boolean
}