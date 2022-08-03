package com.zeoharlem.append.xtremecardz.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.zeoharlem.append.xtremecardz.MainActivity
import com.zeoharlem.append.xtremecardz.R
import com.zeoharlem.append.xtremecardz.utils.Constants

class MyAppsNotificationManager(private val context: Context) {

    private lateinit var notificationManager: NotificationManager
    private var hasRemoveView: Boolean = false

    fun createNotificationChannel(
        channelId: String?,
        channelName: String?,
        channelDescription: String?
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW).apply {
                description = channelDescription
            }
            notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

//    fun getNotificationBuilder(channelId: String?): NotificationCompat.Builder {
//        val builder = NotificationCompat.Builder(context, channelId!!)
//        builder.setSmallIcon(R.drawable.ic_round_notifications_24)
//            .setAutoCancel(false)
//            .setOngoing(true)
//            .setContentTitle("Upload InProgress")
//            .setContentText("Your file is being uploaded...")
//            .setContentIntent(getMainActivityPendingIntent())
//        with(NotificationManagerCompat.from(context)){
//            notify(Constants.NOTIFICATION_ID, builder.build())
//        }
//        return builder
//    }

    fun getNotificationBuilder(channelId: String?): NotificationCompat.Builder {
        val builder = NotificationCompat.Builder(context, channelId!!)
        builder.setSmallIcon(R.drawable.ic_round_notifications_24)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(getRemoteViewLayout())
            .setAutoCancel(false)
            .setOngoing(true)
            .setContentTitle("Upload InProgress")
            .setContentIntent(getMainActivityPendingIntent())
        with(NotificationManagerCompat.from(context)){
            notify(Constants.NOTIFICATION_ID, builder.build())
        }
        return builder
    }

    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        context,0, Intent(context, MainActivity::class.java).apply {
            this.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }, PendingIntent.FLAG_UPDATE_CURRENT
    )

    fun cancelNotification(notificationId: Int) {
        notificationManager.cancel(notificationId)
    }

    fun setHasRemoveView(isBoolean: Boolean){
        hasRemoveView = isBoolean
    }

    fun getNotificationManager() = notificationManager

    private fun getRemoteViewLayout(): RemoteViews {
        return RemoteViews(context.packageName, R.layout.custom_upload_layout)
    }
}