package com.zeoharlem.append.xtremecardz.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.zeoharlem.append.xtremecardz.di.repository.AuthenticityRepository
import com.zeoharlem.append.xtremecardz.models.UserSimpleAccount
import com.zeoharlem.append.xtremecardz.di.repository.Repository
import com.zeoharlem.autonowartisans.sealed.AuthState
import com.zeoharlem.autonowartisans.sealed.NetworkResults
import com.zeoharlem.autonowartisans.sealed.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: AuthenticityRepository): BaseViewModel<UiState>() {

    private val _authState by lazy {
        MutableLiveData<AuthState<Boolean>>(AuthState.Idle())
    }
    val authState: LiveData<AuthState<Boolean>>  = _authState

    private val _userSimpleAccount: MutableLiveData<NetworkResults<UserSimpleAccount>> = MutableLiveData()
    val userSimpleAccount: LiveData<NetworkResults<UserSimpleAccount>>  = _userSimpleAccount

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        get() = field


    fun signIn(email: String, password: String){
        repository.signInWithEmailAddressEvent(email.trim(), password.trim())
    }

    fun signInGoogle(idToken: String){
        repository.signInGoogle(idToken)
    }

    fun getAuthStateRepository(): LiveData<AuthState<Boolean>> {
        return repository.authState
    }

    fun getUserIsLoggedLiveData(): MutableLiveData<Boolean> {
        return repository.getUserLoggedMutableLiveData()
    }

    fun getMyFirebaseAuth(): FirebaseAuth? {
        return repository.getMyFirebaseAuth()
    }

    //Move to the Authenticity Repository
    fun signUp(userSimpleAccount: UserSimpleAccount){
        _authState.value    = AuthState.Loading()
        try {
            mAuth.createUserWithEmailAndPassword(
                userSimpleAccount.email!!,
                userSimpleAccount.password!!
            ).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    mAuth.currentUser!!.sendEmailVerification()
                        .addOnCompleteListener { emailVerifyTask ->
                        if(emailVerifyTask.isSuccessful){
                            _authState.value    = AuthState.Success(true)
                        }
                    }
                }
                else{
                    task.exception?.let {
                        _authState.value    = AuthState.Error(it.localizedMessage)
                    }
                }
            }
        }
        catch (e: ApiException){
            _authState.value    = AuthState.Error(e.localizedMessage)
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun resetAuthenticity(){
        repository.resetPropertiesAction {
            if(!it) repository.getMyFirebaseAuth()?.signOut()
        }
    }

}