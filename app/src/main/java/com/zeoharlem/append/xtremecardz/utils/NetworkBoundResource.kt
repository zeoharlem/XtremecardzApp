package ng.com.zeoharlem.swopit.utils

import kotlinx.coroutines.flow.*

inline fun<ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
)   = flow {
    val data    = query().first()
    val flowRow = if(shouldFetch(data)){
        //emit(NetworkResults.Loading())
        try{
            saveFetchResult(fetch())
            query().map {
                NetworkResults.Success(it)
            }
        }
        catch(e: Exception){
            query().map {
                NetworkResults.Error(e.message, it)
            }
        }
    }
    else{
        query().map { NetworkResults.Success(it) }
    }
    emitAll(flowRow)
}