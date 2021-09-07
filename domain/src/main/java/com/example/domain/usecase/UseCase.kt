package com.example.domain.usecase

import io.reactivex.rxjava3.core.Flowable
import java.io.Serializable

abstract class UseCase<Params: Any, Result> : Serializable {

    private var mParams: Params? = null

    protected abstract fun buildFlowable(params: Params?): Flowable<Result>

    abstract val mIsParamsRequired: Boolean

    fun setParams(params: Params): UseCase<Params, Result>{
        mParams = params
        return this
    }

    fun execute():Flowable<Result>{
        require(!(mIsParamsRequired && mParams == null)) {"Params are required"}
        return buildFlowable(mParams)
    }

}