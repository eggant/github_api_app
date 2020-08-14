package com.example.githubapitask.view.main

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubapitask.R
import com.example.githubapitask.databinding.ActivityMainBinding
import com.example.githubapitask.view.BaseActivity
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity() {
    private val tag = this::class.java.simpleName

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    private val viewModel: MainViewModel by viewModel()

    @SuppressLint("WrongConstant", "CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(tag, "onCreate")

        val layoutBinding = (binding as ActivityMainBinding)
        layoutBinding.vm = viewModel

        val recyclerView = layoutBinding.recyclerView
        recyclerView.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val usersAdapter = UsersAdapter()
        recyclerView.adapter = usersAdapter

        val hDivider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        hDivider.setDrawable(getDrawable(R.drawable.divider_users)!!)
        recyclerView.addItemDecoration(hDivider)

        viewModel.usersSubject.subscribe { list ->
            Log.d(tag, "usersSubject.subscribe")
            usersAdapter.submitList(list)
        }

        viewModel.usersDataSourceEventSubject.subscribe { event ->
            Log.d(tag, "usersDataSourceEventSubject.subscribe")
            event?.let {
                when (it.result) {
                    Result.ERROR -> {
                        val msg = "[ERROR] ${event.state} - ${event.data}"
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                    }
                    else -> {

                    }
                }

                when (it.state) {
                    DataSourceState.INITIAL -> {
                        viewModel.hideProgressBar()
                    }
                    else -> {

                    }
                }
            }
        }

        layoutBinding.searchIcon.setOnClickListener {
            search(layoutBinding.searchInput.text.toString())
        }

        layoutBinding.searchInput.setOnKeyListener { _, keyCode, keyEvent ->
            Log.d(tag, "searchInput.setOnKeyListener - keyCode: $keyCode, keyEvent: $keyEvent")
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                search(layoutBinding.searchInput.text.toString())
                true
            } else {
                false
            }
        }

        layoutBinding.searchInput.setOnEditorActionListener { _, keyCode, keyEvent ->
            Log.d(
                tag,
                "searchInput.setOnEditorActionListener - keyCode: $keyCode, keyEvent: $keyEvent"
            )
            if (keyCode == KeyEvent.KEYCODE_ENDCALL) {
                search(layoutBinding.searchInput.text.toString())
                true
            } else {
                false
            }
        }
    }

    private fun search(query: String) {
        Log.d(tag, "search - query: $query")
        if (query.isNullOrEmpty()) return
        hideKeyboard()
        viewModel.search(query)
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
    }
}
