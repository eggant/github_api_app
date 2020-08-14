package com.example.githubapitask.view

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import java.lang.ref.WeakReference

abstract class BaseActivity : AppCompatActivity() {

    private lateinit var weakSelf: WeakReference<BaseActivity>
    lateinit var binding: ViewDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        weakSelf = WeakReference(this)
        binding = DataBindingUtil.setContentView(weakSelf.get()!!, getLayoutId())
        binding.lifecycleOwner = weakSelf.get()
    }

    @LayoutRes
    abstract fun getLayoutId(): Int
}