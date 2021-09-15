package com.example.task1new.ext

import com.example.domain.dto.SingleCapitalDto
import com.example.task1new.base.mvvm.Outcome
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

fun Flow<List<SingleCapitalDto>>.modifyFlowToOutcome(): Flow<Outcome<List<SingleCapitalDto>>> =
    this.flowOn(Dispatchers.IO)
        .map { list -> Outcome.success(list) }
        .onStart { emit(Outcome.loading(true)) }
        .onCompletion { emit(Outcome.loading(false)) }
        .catch { ex -> emit(Outcome.failure(ex)) }