package com.example.githubapitask.view.main

import android.util.Log
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.example.githubapitask.model.DataRepository
import com.example.githubapitask.model.entity.UserModel
import com.example.githubapitask.model.entity.UserQueryResult
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Response

enum class Result {
    SUCCESS,
    ERROR
}

enum class DataSourceState {
    INITIAL,
    AFTER,
    BEFORE
}

data class DataSourceEvent(
    var state: DataSourceState,
    var result: Result,
    val data: Any? = null
)

class UsersDataSource(
    private val disposables: CompositeDisposable,
    private val dataSourceEventSubject: PublishSubject<DataSourceEvent>,
    private val queryValue: String
) : PageKeyedDataSource<Int, UserModel>(), KoinComponent {
    private val tag = this::class.java.simpleName

    companion object {
        private const val PAGE_SIZE = 30

        private val config =
            PagedList.Config.Builder().setPageSize(PAGE_SIZE).setInitialLoadSizeHint(2 * PAGE_SIZE)
                .setEnablePlaceholders(true).build()

        fun createPagedList(
            disposables: CompositeDisposable,
            eventSubject: PublishSubject<DataSourceEvent>,
            query: String
        ): Observable<PagedList<UserModel>> {
            val usersDataSourceFactory = UsersDataSourceFactory(disposables,eventSubject, query)
            return RxPagedListBuilder(usersDataSourceFactory, config).buildObservable()
        }
    }

    private val repository: DataRepository by inject()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, UserModel>
    ) {
        Log.d(tag, "loadInitial -  queryValue: $queryValue")
        getUserList(queryValue, 1, { response ->
            //onSuccess
            Log.d(tag, "loadInitial - callback")
            val result = response.body()?.items ?: emptyList()
            callback.onResult(result, 0, 2)
            dataSourceEventSubject.onNext(
                DataSourceEvent(DataSourceState.INITIAL, Result.SUCCESS)
            )
        }, {
            //onError
            dataSourceEventSubject.onNext(
                DataSourceEvent(DataSourceState.INITIAL, Result.ERROR, it)
            )
        })

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, UserModel>) {
        Log.d(tag, "loadAfter - page: ${params.key}")
        getUserList(queryValue, params.key, { response ->
            //onSuccess
            Log.d(tag, "loadAfter - callback")
            val result = response.body()?.items ?: emptyList()
            callback.onResult(result, params.key.plus(1))
            dataSourceEventSubject.onNext(
                DataSourceEvent(DataSourceState.AFTER, Result.SUCCESS)
            )
        }, {
            //onError
            dataSourceEventSubject.onNext(
                DataSourceEvent(DataSourceState.AFTER, Result.ERROR, it)
            )
        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, UserModel>) {
        //
    }

    private fun getUserList(
        query: String,
        page: Int,
        onSuccess: ((Response<UserQueryResult>) -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null
    ) {
        Log.d(tag, "getUserList - page: $page")

        disposables.add(
            repository.dataApiService.getUserList("$query", "", "desc", page, PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    Log.d(tag, "getUserList ...")
                }
//                .doOnDispose { }
//                .doOnError { }
//                .doOnSuccess { }
                .subscribeWith(object :
                    DisposableSingleObserver<Response<UserQueryResult>>() {
                    override fun onSuccess(response: Response<UserQueryResult>) {
                        val result = response.body()
                        Log.d(tag, "getUserList.onSuccess - $response, result: $result")
                        result?.let {
                            onSuccess?.invoke(response)
                        }
                    }

                    override fun onError(error: Throwable) {
                        Log.d(tag, "getUserList.onError - $error")
                        onError?.invoke(error)
                    }
                })
        )
    }
}

class UsersDataSourceFactory(
    private val compositeDisposable: CompositeDisposable,
    private val dataSourceEventSubject: PublishSubject<DataSourceEvent>,
    var queryValue: String
) : DataSource.Factory<Int, UserModel>() {
    override fun create(): DataSource<Int, UserModel> {
        return UsersDataSource(
            compositeDisposable,
            dataSourceEventSubject,
            queryValue
        )
    }
}
