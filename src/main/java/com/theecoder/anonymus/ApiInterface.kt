package com.theecoder.anonymus

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {

    @GET("/")
    fun getData(): Call<List<MyDataItem>>

    @POST("/")
    suspend fun sendMessage(@Body requestBody: RequestBody): Response<ResponseBody>
}