package com.zeoharlem.append.xtremecardz

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.zeoharlem.append.xtremecardz.databinding.ActivityRegisterBinding
import com.zeoharlem.append.xtremecardz.models.UserSimpleAccount
import com.zeoharlem.append.xtremecardz.utils.XtremeCardzUtils
import com.zeoharlem.append.xtremecardz.viewmodels.LoginViewModel
import com.zeoharlem.autonowartisans.sealed.AuthState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private var _binding: ActivityRegisterBinding? = null
    private var loginViewModel: LoginViewModel? = null
    private lateinit var googleSignInClient: GoogleSignInClient
    private var userSimpleAccount: UserSimpleAccount? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        _binding        = ActivityRegisterBinding.inflate(layoutInflater)
        loginViewModel  = ViewModelProvider(this).get(LoginViewModel::class.java)
        setContentView(binding.root)

        val googleSignOptions   = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.webclient_id)).requestEmail().build()

        googleSignInClient      = GoogleSignIn.getClient(applicationContext, googleSignOptions)

        binding.gotoSignInBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        clickRegisterAction()
    }

    private fun clickRegisterAction(){
        loginViewModel!!.authState.observe(this){
            when(it){
                is AuthState.Loading -> {
                    binding.registerBtn.isEnabled   = false
                    binding.registerBtn.text        = "Please wait!"
                }
                is AuthState.Success -> {
                    val message = "An activation link has been sent to ${userSimpleAccount?.email}. " +
                            "Check and click the link in the mail to Activate your Account"
                    XtremeCardzUtils.customAlertDialog(Dialog(this), message) {
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
                }
                is AuthState.Error -> {
                    XtremeCardzUtils.customAlertDialog(it.message, Dialog(this))
                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
        binding.registerBtn.setOnClickListener {
            userSimpleAccount   = UserSimpleAccount(
                firstName = binding.firstName.text.toString().trim(),
                lastName = binding.lastName.text.toString().trim(),
                email = binding.loginEmailid.text.toString().trim(),
                password = binding.loginPassword.text.toString().trim()
            )
            loginViewModel?.signUp(userSimpleAccount!!)
        }
    }
}