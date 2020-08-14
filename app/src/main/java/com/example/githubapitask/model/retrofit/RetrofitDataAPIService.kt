package com.example.githubapitask.model.retrofit

import com.example.githubapitask.model.entity.UserQueryResult
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface RetrofitDataAPIService {

    companion object {
        private const val apiBaseUrl = "https://api.github.com/"

        fun create(): RetrofitDataAPIService {
            val client = OkHttpClient.Builder()
            .addNetworkInterceptor(HttpLoggingInterceptor())
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)

            val okHttpClient = client.build()
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .baseUrl(apiBaseUrl)
                .build()

            return retrofit.create(RetrofitDataAPIService::class.java)
        }
    }

    @GET("search/users")
    fun getUserList(@Query("q") query: String,
                    @Query("sort") sort: String,
                    @Query("order") order: String,
                    @Query("page") page: Int,
                    @Query("per_page") perPage: Int):
            Single<Response<UserQueryResult>>
}