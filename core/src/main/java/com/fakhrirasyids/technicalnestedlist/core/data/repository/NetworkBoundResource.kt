package com.fakhrirasyids.technicalnestedlist.core.data.repository

import com.fakhrirasyids.technicalnestedlist.core.utils.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

abstract class NetworkBoundResource<ResultType, RequestType> {

    fun asFlow(dispatcher: CoroutineDispatcher) = flow {
        emit(Resource.Loading)

        val localData = loadFromDb().firstOrNull()

        if (shouldFetch(localData)) {
            runCatching {
                val apiResponse = fetchFromNetwork()
                saveNetworkResult(apiResponse)
            }.onSuccess {
                emitAll(loadFromDb().map { Resource.Success(it) })
            }.onFailure { e ->
                handleNetworkFailure(e)
            }
        } else {
            emitAll(loadFromDb().map { Resource.Success(it) })
        }
    }.flowOn(dispatcher)

    private suspend fun FlowCollector<Resource<ResultType>>.handleNetworkFailure(e: Throwable) {
        val errorMessage = e.message.toString()
        if (errorMessage.contains("400")) {
            runCatching {
                val fallbackResponse = fetchFromNetworkWithoutParam()
                saveNetworkResult(fallbackResponse)
            }.onSuccess {
                emitAll(loadFromDb().map { Resource.Success(it) })
            }.onFailure {
                emit(Resource.Error(it.message.toString()))
            }
        } else {
            emit(Resource.Error(errorMessage))
        }
    }

    protected abstract fun loadFromDb(): Flow<ResultType>
    protected abstract suspend fun fetchFromNetwork(): RequestType
    protected abstract suspend fun fetchFromNetworkWithoutParam(): RequestType
    protected abstract suspend fun saveNetworkResult(data: RequestType)
    protected open fun shouldFetch(data: ResultType?): Boolean = true
}
