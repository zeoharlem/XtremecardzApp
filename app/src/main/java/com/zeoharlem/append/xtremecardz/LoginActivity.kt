package com.zeoharlem.append.xtremecardz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.wajahatkarim3.easyvalidation.core.collection_ktx.nonEmptyList
import com.zeoharlem.append.xtremecardz.databinding.ActivityLoginBinding
import com.zeoharlem.append.xtremecardz.viewmodels.LoginViewModel
import com.zeoharlem.autonowartisans.sealed.AuthState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private var _binding: ActivityLoginBinding? = null
    private var loginViewModel:LoginViewModel? = null
    private val binding get() = _binding!!
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        _binding        = ActivityLoginBinding.inflate(layoutInflater)
        loginViewModel  = ViewModelProvider(this).get(LoginViewModel::class.java)

        setContentView(binding.root)

        val googleSignOptions   = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.webclient_id)).requestEmail().build()

        googleSignInClient      = GoogleSignIn.getClient(applicationContext, googleSignOptions)

        binding.googleSignIn.setOnClickListener {
            loginUsingGoogleSignInApplication()
        }

        loginViewModel!!.getAuthStateRepository().observe(this){
            when(it){
                is AuthState.Loading -> {
                    binding.googleSignIn.isEnabled  = false
                    binding.googleSignIn.text       = "Please Wait..."
                    Toast.makeText(applicationContext, "Please wait...", Toast.LENGTH_SHORT).show()
                }
                is AuthState.Success -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                is AuthState.Error -> {
                    binding.googleSignIn.isEnabled  = true
                    binding.googleSignIn.text       = "Google Sign In"
                    Toast.makeText(applicationContext, "Error ${it.message}", Toast.LENGTH_LONG).show()
                }
                else -> {}
            }
        }

        binding.signUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun loginWithEmailPasswordAction(){
        binding.loginBtn.setOnClickListener {
            val nonEmptyAction  = nonEmptyList(
                binding.loginEmailid,
                binding.loginPassword
            ){ view: EditText, message: String ->
                view.error  = message
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
            }
            if(nonEmptyAction)
                loginInUsingEmailPasswordApplication()
        }
    }

    private fun loginUsingGoogleSignInApplication() {
        getActivityForResult.launch(googleSignInClient.signInIntent)
    }

    private fun loginInUsingEmailPasswordApplication(){
        loginViewModel?.signIn(
            binding.loginEmailid.text.toString().trim(),
            binding.loginPassword.text.toString().trim()
        )
    }


    private val getActivityForResult    = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == RESULT_OK){
            val data    = result.data
            val task    = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                loginViewModel?.signInGoogle(account.idToken!!)
            }
            catch (e: ApiException) {
                Log.e("HomeDashboard", "signIn: ${e.localizedMessage}")
                e.message?.let {
                    customAlertDialog("Google sign in error", it)
                }
            }
        }
        else{
            Log.e("HomeDashboard", "Result $result")
            customAlertDialog("Error response", "Seems no data was " +
                    "returned or check your internet connection")
        }
    }

    override fun onStart() {
        super.onStart()
        if(loginViewModel!!.getMyFirebaseAuth()!!.currentUser != null)
            startActivity(Intent(this, MainActivity::class.java))
    }

    private fun customAlertDialog(title: String, message: String){

    }
}