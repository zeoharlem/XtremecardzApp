package com.zeoharlem.append.xtremecardz.utils

class Constants {
    companion object{
        const val QUERY     = "q"
//        const val BASE_URL  = "http://192.168.0.104:3000/api/"
        const val BASE_URL  = "https://xtremeapi.xtremecardz.com/api/"
        const val GOOGLE_BASE_URL   = "https://fcm.googleapis.com/"
        const val API_KEY   = "tkQ03VCcziNtG5qmrh1cKXKQDEEkqojUGnL7tYgW"
//        const val WEB_KEY   = (R.string.web_key).toString()

        const val PERMISSION_REQ_ID_RECORD_AUDIO    = 22
        const val PERMISSION_REQ_ID_CAMERA          = PERMISSION_REQ_ID_RECORD_AUDIO + 1

        const val ACTION_START_SERVICE = "ACTION_START_OR_RESUME_SERVICE"
        const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
        const val NOTIFICATION_ID   = 1
        const val NOTIFICATION_CHANNEL_ID   = "upload_zip_channel"
        const val NOTIFICATION_CHANNEL_NAME = "UploadZip"
        const val BUFFER = 1024
    }
}