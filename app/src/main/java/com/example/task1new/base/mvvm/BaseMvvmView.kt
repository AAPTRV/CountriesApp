package com.example.task1new.base.mvvm

interface BaseMvvmView {

    fun showError(error: String, throwable: Throwable)

    fun showProgress()

    fun hideProgress()

}