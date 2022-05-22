package com.zeoharlem.append.xtremecardz.utils

class Constants {
    companion object{
        const val QUERY     = "q"
        const val BASE_URL  = "http://192.168.0.112:3000/api/"
//        const val BASE_URL  = "https://cr8.com.ng/swopitapiv1/"
        const val GOOGLE_BASE_URL   = "https://fcm.googleapis.com/"
        const val API_KEY   = "tkQ03VCcziNtG5qmrh1cKXKQDEEkqojUGnL7tYgW"
//        const val WEB_KEY   = (R.string.web_key).toString()

        const val CONTENT_TYPE = "application/json"

        const val AVATAR    = "https://png.pngtree.com/png-clipart/20190516/original/pngtree-man-avatar-icon-professional-man-character-business-man-avatar-carton-symbol-png-image_3669490.jpg"

        //ROOM Database
        const val DATABASE_NAME     = "swopitng_db"
        const val CATEGORIES_TABLE  = "categories_table"
        const val STATES_TABLE      = "states_table"

        const val REQUEST_CODE_LOCATION_PERMISSION  = 0
        const val PERMISSION_ID                     = 1010
        const val PERMISSION_REQ_ID_RECORD_AUDIO    = 22
        const val PERMISSION_REQ_ID_CAMERA          = PERMISSION_REQ_ID_RECORD_AUDIO + 1
    }
}