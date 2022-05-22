package com.zeoharlem.append.xtremecardz.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*

object MyCustomExtUtils {
    @JvmStatic
    fun String.capitalizeWords(): String    = split(" ").joinToString(" "){
        it.lowercase().replaceFirstChar {  character ->
            character.uppercase()
        }
    }

    fun<T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T) {
                removeObserver(this)
                observer.onChanged(t)
            }
        })
    }

    fun <T> LiveData<T>.observeOnceAfterInit(lifecycleOwner: LifecycleOwner, observer:Observer<T>) {
        var firstObservation = true

        observe(lifecycleOwner, object: Observer<T> {
            override fun onChanged(value: T) {
                if(firstObservation) {
                    firstObservation = false
                }
                else {
                    removeObserver(this)
                    observer.onChanged(value)
                }
            }
        })
    }

    //Run without progress report
    fun <R> CoroutineScope.executeAsyncTask(
        onPreExecute: () -> Unit,
        doInBackground: () -> R,
        onPostExecute: (R) -> Unit
    ) = launch {
        onPreExecute()
        val result  = withContext(Dispatchers.IO){
            doInBackground()
        }
        onPostExecute(result)
    }

    //Runs with progress report
    fun <P, R> CoroutineScope.executeAsyncTask(
        onPreExecute: () -> Unit,
        doInBackground: suspend (suspend (P) -> Unit) -> R,
        onPostExecute: (R) -> Unit,
        onProgressUpdate: (P) -> Unit
    ) = launch {
        onPreExecute()
        val result = withContext(Dispatchers.IO) {
            doInBackground {
                withContext(Dispatchers.Main) { onProgressUpdate(it) }
            }
        }
        onPostExecute(result)
    }

    //Generic Response Created
    fun<T> LiveData<NetworkResults<T>>.networkHandler(response: Response<T>): NetworkResults<T>? {
        return when{
            response.message().toString().contains("timeout") -> NetworkResults.Error("Timeout")
            response.code() == 402 -> NetworkResults.Error("Api Key Not Correct")
            response.isSuccessful -> NetworkResults.Success(response.body())
            else -> NetworkResults.Error(response.message())
        }
    }

    fun getLocalIpAddress(): String? {
        try {
            val en: Enumeration<NetworkInterface>   = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val intf: NetworkInterface = en.nextElement()
                val enumIpAddr: Enumeration<InetAddress> = intf.inetAddresses
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress: InetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                        return inetAddress.getHostAddress()
                    }
                }
            }
        }
        catch (ex: SocketException) {
            ex.printStackTrace()
        }
        return null
    }
}