package com.example.task1new.base.mvvm

sealed class Outcome <T> {

    data class Progress<T>(var loading: Boolean): Outcome<T>()
    data class Success<T>(var data: T): Outcome<T>()
    data class Next<T>(var data: T): Outcome<T>()
    data class Failure<T>(var e: Throwable): Outcome <T>()

    companion object{
        fun <T> loading(isLoading: Boolean): Outcome<T> = Progress(isLoading)
        fun <T> success(data: T): Outcome<T> = Success(data)
        fun <T> failure(e: Throwable): Outcome<T> = Failure(e)
        fun <T> next(data: T): Outcome<T> = Next(data)
    }
}