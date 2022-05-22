package com.zeoharlem.append.xtremecardz.di.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.zeoharlem.append.xtremecardz.models.UserSimpleAccount
import com.zeoharlem.append.xtremecardz.sealed.AuthState
import com.zeoharlem.append.xtremecardz.sealed.NetworkResults
import dagger.hilt.android.scopes.ActivityRetainedScoped
import okhttp3.ResponseBody
import javax.inject.Inject

@ActivityRetainedScoped
class AuthenticityRepository @Inject constructor(private val repository: Repository) {

    private val _authState by lazy {
        MutableLiveData<AuthState<Boolean>>(AuthState.Idle())
    }
    val authState: LiveData<AuthState<Boolean>> = _authState

    private var _networkResults = MutableLiveData<NetworkResults<UserSimpleAccount>>()
    val networkResults: LiveData<NetworkResults<UserSimpleAccount>> get() = _networkResults

    private var _loginNetworkResults    = MutableLiveData<NetworkResults<UserSimpleAccount>>()
    val loginNetworkResults: LiveData<NetworkResults<UserSimpleAccount>> get() = _loginNetworkResults

    private var firebaseUserMutableLiveData = MutableLiveData<FirebaseUser>()
    private var userLoggedMutableLiveData   = MutableLiveData(false)
    private var auth: FirebaseAuth?         = FirebaseAuth.getInstance()

    init {
        if (auth!!.currentUser != null){
            firebaseUserMutableLiveData.value = auth!!.currentUser
        }
    }

    fun getMyFirebaseAuth(): FirebaseAuth? {
        return auth
    }

    fun getFirebaseUserMutableLiveData(): MutableLiveData<FirebaseUser> {
        return firebaseUserMutableLiveData
    }

    fun getUserLoggedMutableLiveData(): MutableLiveData<Boolean> {
        return userLoggedMutableLiveData
    }


    fun signInGoogle(idToken: String){
        Log.e("SignInGoogle", "signInGoogle: called")
        try{
            signInWithGoogleLoginSystem(idToken)
        }
        catch (e: ApiException){
            _authState.value    = AuthState.Error(e.localizedMessage)
            Log.e("LoginViewModel", "signIn: ${e.localizedMessage}")
        }
    }

    fun signInWithEmailAddressEvent(email: String, password: String) {
        try{
            signInUsingEmailAddressEvent(email, password)
        }
        catch (e: Exception){
            _authState.value    = AuthState.Error(e.localizedMessage)
            Log.e("LoginViewModel", "signInWithEmailAddressEvent: ${e.localizedMessage}")
        }
    }

    //higher function order
    fun resetPropertiesAction(isLogged: (Boolean) -> Unit){
        userLoggedMutableLiveData.value = false
        isLogged.invoke(userLoggedMutableLiveData.value!!)
    }

    suspend fun registerUserSilently(query: HashMap<String, String>){
        _networkResults.value   = NetworkResults.Loading()
        try {
            val response    = repository.remoteDataSource.registerUserSilently(query)
            _networkResults.value   = NetworkResults.Success(response.body())
        }
        catch (e: Exception){
            _networkResults.value = NetworkResults.Error(e.localizedMessage)
        }
    }

    suspend fun login(query: HashMap<String, String>){
        _loginNetworkResults.value   = NetworkResults.Loading()
        try {
            val response    = repository.remoteDataSource.login(query)
            _loginNetworkResults.value   = NetworkResults.Success(response.body())
            Log.e("AuthenticityRepo", "login: ${response.body()}", )
        }
        catch (e: Exception){
            _loginNetworkResults.value = NetworkResults.Error(e.localizedMessage)
            Log.e("AuthenticityRepo", "login error: ${e.localizedMessage}", )
        }
    }

    private fun signInUsingEmailAddressEvent(email: String, pass: String) {
        _authState.value    = AuthState.Loading()
        auth!!.signInWithEmailAndPassword(email.trim(), pass.trim())
            .addOnCompleteListener { task ->
                if(task.isSuccessful && auth?.currentUser?.isEmailVerified == true){
                    userLoggedMutableLiveData.value     = true
                    firebaseUserMutableLiveData.value   = auth!!.currentUser
                    _authState.value = AuthState.Success(true)
                }
                else{
                    userLoggedMutableLiveData.value = false
                    _authState.value    = AuthState.Error(task.exception?.localizedMessage)
                    throw Exception(task.exception?.localizedMessage)
                }
        }
    }

    private fun signInWithGoogleLoginSystem(idToken: String){
        _authState.value    = AuthState.Loading()
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth!!.signInWithCredential(credential).addOnCompleteListener{ task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                userLoggedMutableLiveData.value   = true
                firebaseUserMutableLiveData.value = auth!!.currentUser
                _authState.value    = AuthState.Success(true)
                Log.e("Authenticity", "successful:$task")
            }
            else {
                _authState.value    = AuthState.Error(task.exception?.localizedMessage)
                userLoggedMutableLiveData.value = false
            }
        }.addOnFailureListener {
            Log.e("Authenticity", "FailedListener:"+ it.localizedMessage)
            _authState.value    = AuthState.Error(it.localizedMessage)
        }
    }



}