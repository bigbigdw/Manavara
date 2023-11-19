package com.bigbigdw.manavara.retrofit

interface RetrofitDataListener<T> {
    fun onSuccess(data: T)
}