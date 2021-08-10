package com.example.task1new.base.mvp

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import org.koin.androidx.scope.ScopeFragment

abstract class BaseMvpFragment<View : BaseMvpView, PresenterType : BaseMvpPresenter<View>> : Fragment() {

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