package com.zeoharlem.append.xtremecardz.utils

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.datastore.preferences.core.stringPreferencesKey
//import androidx.preference.PreferenceManager
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


object SwopitUtils {
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
//    //Using the sharedPreference logic
//    fun saveKey(key: String, value: String, context: Context){
//        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
//        sharedPreferences.edit().putString(key, value).apply()
//    }
//
//    fun readKey(key: String, context: Context): String? {
//        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
//        return sharedPreferences.getString(key, "")
//    }

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

}