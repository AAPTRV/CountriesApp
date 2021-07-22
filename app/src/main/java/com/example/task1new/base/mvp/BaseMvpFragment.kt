package com.example.task1new.base.mvp

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

abstract class BaseMvpFragment<View : BaseMvpView> : Fragment() {

    protected lateinit var mPresenter: BaseMvpPresenter<View>

    abstract fun createPresenter()

    abstract fun getPresenter(): BaseMvpPresenter<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createPresenter()
    }

}