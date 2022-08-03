package com.zeoharlem.append.xtremecardz.services

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.zeoharlem.append.xtremecardz.R
import com.zeoharlem.append.xtremecardz.notifications.MyAppsNotificationManager
import com.zeoharlem.append.xtremecardz.sealed.TimerEvent
import com.zeoharlem.append.xtremecardz.utils.Constants
import com.zeoharlem.append.xtremecardz.utils.Constants.Companion.NOTIFICATION_CHANNEL_ID
import com.zeoharlem.append.xtremecardz.utils.Constants.Companion.NOTIFICATION_CHANNEL_NAME
import com.zeoharlem.append.xtremecardz.utils.Constants.Companion.NOTIFICATION_ID
import com.zeoharlem.append.xtremecardz.utils.TimerUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyUploadService: LifecycleService() {
    private lateinit var myAppsNotificationManager: MyAppsNotificationManager
    private var isServiceStopped = false

    companion object {
        val TAG = "MyUploadService"
        val timerEvent = MutableLiveData<TimerEvent>()
        val timerInMillis = MutableLiveData<Long>()
    }

    override fun onCreate() {
        super.onCreate()
        myAppsNotificationManager = MyAppsNotificationManager(this)
        initValues()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when(it.action){
                Constants.ACTION_START_SERVICE -> {
                    Log.e(TAG, "onStartCommand: Started Service", )
                    startForegroundService()
                }
                Constants.ACTION_STOP_SERVICE -> {
                    Log.e(TAG, "onStartCommand: Stopped Service", )
                    stopForegroundService()
                }
                else -> {
                    Log.e(TAG, "onStartCommand: Nothing worked")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun initValues(){
        timerEvent.postValue(TimerEvent.STOP)
        timerInMillis.postValue(0L)
    }

    private fun startForegroundService(){
        timerEvent.postValue(TimerEvent.START)
        startTimer()
        myAppsNotificationManager.setHasRemoveView(true)
        myAppsNotificationManager.createNotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            "Upload in progress"
        )
        startForeground(NOTIFICATION_ID, myAppsNotificationManager.getNotificationBuilder(NOTIFICATION_CHANNEL_ID).build())
        timerInMillis.observe(this){
            if(!isServiceStopped){
                val builder = myAppsNotificationManager.getNotificationBuilder(NOTIFICATION_CHANNEL_ID)
                val contentView = builder.contentView
                contentView.setTextViewText(R.id.timer_counter, TimerUtil.getFormattedTime(it, false))
                myAppsNotificationManager.getNotificationManager().notify(NOTIFICATION_ID, builder.build())
            }
        }
    }

    private fun stopForegroundService(){
        isServiceStopped = true
        initValues()
        myAppsNotificationManager.cancelNotification(NOTIFICATION_ID)
        //timerEvent.postValue(TimerEvent.STOP)
        stopForeground(true)
        stopSelf()
    }

    private fun startTimer(){
        val timeStarted = System.currentTimeMillis()
        CoroutineScope(Dispatchers.Main).launch {
            while (!isServiceStopped && timerEvent.value!! == TimerEvent.START){
                val lapTime = System.currentTimeMillis() - timeStarted
                timerInMillis.postValue(lapTime)
                delay(50L)
            }
        }
    }
}