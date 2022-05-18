package com.zeoharlem.append.xtremecardz.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.preference.PreferenceManager
import com.zeoharlem.append.xtremecardz.R
import kotlinx.coroutines.Runnable
//import androidx.preference.PreferenceManager
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


object XtremeCardzUtils {
    //Create the RequestBody for the form
    fun createPartFromString(string: String): RequestBody {
        return RequestBody.create(MultipartBody.FORM, string)
    }

    //Create the MultipartBody.Part for the image file to be uploaded
    fun prepareFilePart(partName: String, file: File): MultipartBody.Part {
        val requestBody = RequestBody.create(MediaType.parse("image/*"), file)
        return MultipartBody.Part.createFormData(partName, file.name, requestBody)
    }
//
//    suspend fun save(key: String, value: String, context: Context){
//        val dataStoreKey    = stringPreferencesKey(key)
//        context.dataStore.edit { settings ->
//            settings[dataStoreKey] = value
//        }
//    }
//
//    suspend fun read(key: String, context: Context): String?{
//        val preferences = context.dataStore.data.first()
//        return preferences[stringPreferencesKey(key)]
//    }
//
    //Using the sharedPreference logic
    fun saveKey(key: String, value: String, context: Context){
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun readKey(key: String, context: Context): String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getString(key, "")
    }

    fun randomKeyString(length: Int): String {
        val allowedCharset  = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length).map {
            allowedCharset.random()
        }.joinToString("")
    }

    private fun getAllChildren(v: View): ArrayList<View> {
        if (v !is ViewGroup) {
            val viewArrayList: ArrayList<View> = ArrayList<View>()
            viewArrayList.add(v)
            return viewArrayList
        }
        val result: ArrayList<View> = ArrayList<View>()
        for (i in 0 until v.childCount) {
            val child: View = v.getChildAt(i)
            val viewArrayList: ArrayList<View> = ArrayList<View>()
            viewArrayList.add(v)
            viewArrayList.addAll(getAllChildren(child))
            result.addAll(viewArrayList)
        }
        return result
    }

    //Method Overload
    fun customAlertDialog(message: String?, dialog: Dialog){
        dialog.setCancelable(false)
        val backColor       = ColorDrawable(Color.TRANSPARENT)
        val insetDrawable   = InsetDrawable(backColor, 0)
        dialog.window?.setBackgroundDrawable(insetDrawable)
        //dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.custom_alert_dialog)

        val errorMessage    = dialog.findViewById<TextView>(R.id.errorMessage)
        val closeButton     = dialog.findViewById<Button>(R.id.alertCustomCloseBtn)

        errorMessage.text   = message
        closeButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    //Higher function order(okEvent()->Unit)
    fun customAlertDialog(dialog: Dialog, message: String, okEvent: () -> Unit){
        dialog.setCancelable(false)
        val backColor       = ColorDrawable(Color.TRANSPARENT)
        val insetDrawable   = InsetDrawable(backColor, 0)
        dialog.window?.setBackgroundDrawable(insetDrawable)
        //dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.custom_alert_dialog)

        val errorMessage    = dialog.findViewById<TextView>(R.id.errorMessage)
        val closeButton     = dialog.findViewById<Button>(R.id.alertCustomCloseBtn)

        errorMessage.text   = message
        closeButton.setOnClickListener {
            dialog.dismiss()
            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                //Called higher function
                okEvent.invoke()
            }, 1000)
        }
        dialog.show()
    }

}