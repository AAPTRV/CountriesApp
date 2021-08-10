package com.example.task1new.base.mvp

import android.os.Bundle
import org.koin.androidx.scope.ScopeFragment

abstract class BaseKoinMvpFragment<View : BaseMvpView, PresenterType : BaseMvpPresenter<View>> : ScopeFragment() {
    protected lateinit var mPresenter: PresenterType

    abstract fun createPresenter()

    abstract fun getPresenter(): PresenterType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createPresenter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        getPresenter().onDestroyView()
        getPresenter().detachView()
    }
}