package com.example.githubapitask.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    var progressBarVisible = MutableLiveData<Boolean>().apply { value = false }
    var progressText = MutableLiveData<String>()

    val disposables = CompositeDisposable()

    fun showProgressBar(showText: String? = null) {
        progressBarVisible.postValue(true)
        if (!showText.isNullOrBlank())
            progressText.postValue(showText)
        else
            progressText.postValue("")
    }

    fun hideProgressBar() {
        progressBarVisible.postValue(false)
        progressText.postValue("")
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}