package com.example.githubapitask.model

import android.util.Log
import com.example.githubapitask.model.retrofit.RetrofitDataAPIService

class DataRepository {
    private val tag = "DataRepository"

    val dataApiService: RetrofitDataAPIService

    init {
        Log.d(tag, "init")
        dataApiService = RetrofitDataAPIService.create()
    }
}