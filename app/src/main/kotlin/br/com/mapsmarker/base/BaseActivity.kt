package br.com.mapsmarker.base

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import dagger.android.AndroidInjection

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(layoutResId)
    }

    @get:LayoutRes
    protected abstract val layoutResId: Int

    protected fun observeLoadingStatus(viewModel: BaseViewModel, resultView: View,
                                       progressView: View) {
        viewModel.loadingStatus.observe(this, Observer<Boolean> {
            isLoading -> isLoading?.let {
                resultView.visibility = if (it) GONE else VISIBLE
                progressView.visibility = if (it) VISIBLE else GONE
            }
        })
    }

}