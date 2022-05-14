package com.zeoharlem.append.xtremecardz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.zeoharlem.append.xtremecardz.databinding.ActivityRegisterBinding
import com.zeoharlem.append.xtremecardz.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private var _binding: ActivityRegisterBinding? = null
    private var loginViewModel: LoginViewModel? = null
    private lateinit var googleSignInClient: GoogleSignInClient

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    }
}