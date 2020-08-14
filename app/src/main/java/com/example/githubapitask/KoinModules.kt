package com.example.githubapitask

import com.example.githubapitask.model.DataRepository
import com.example.githubapitask.view.main.MainViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // Single instance of Repository
    single { DataRepository() }

    viewModel { MainViewModel(get()) }
}