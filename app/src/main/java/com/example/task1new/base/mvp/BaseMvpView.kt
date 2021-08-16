package com.example.task1new.base.mvp

interface BaseMvpView {

    fun showError(error: String, throwable: Throwable)

    fun showProgress()

    fun hideProgress()

}