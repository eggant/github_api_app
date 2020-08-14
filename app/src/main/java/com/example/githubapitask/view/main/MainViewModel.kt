package com.example.githubapitask.view.main

import android.app.Application
import android.util.Log
import androidx.paging.PagedList
import com.example.githubapitask.model.entity.UserModel
import com.example.githubapitask.view.BaseViewModel
import io.reactivex.observers.DisposableObserver
import io.reactivex.subjects.PublishSubject

class MainViewModel(application: Application) : BaseViewModel(application) {
    private val tag = this::class.java.simpleName

    val usersSubject = PublishSubject.create<PagedList<UserModel>>()

    val usersDataSourceEventSubject = PublishSubject.create<DataSourceEvent>()

    init {
        Log.d(tag, "init")
    }

    fun search(query: String) {
        Log.d(tag, "search - $query")
        val usersPageList =
            UsersDataSource.createPagedList(disposables, usersDataSourceEventSubject, query)
        showProgressBar()
        disposables.add(
            usersPageList.subscribeWith(object : DisposableObserver<PagedList<UserModel>>() {
                override fun onComplete() {
                    Log.d(tag, "fetchPages.onComplete")
                }

                override fun onNext(pagedUserList: PagedList<UserModel>) {
                    usersSubject.onNext(pagedUserList)
                }

                override fun onError(error: Throwable) {
                    Log.e(tag, "fetchPages.onError - onError: $error")
                }
            })
        )
    }

}