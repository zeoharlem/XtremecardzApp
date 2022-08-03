package com.zeoharlem.append.xtremecardz.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class BaseAppViewModel @Inject constructor(application: Application): AndroidViewModel(application) {
    protected val context
        get() = getApplication<Application>()

}