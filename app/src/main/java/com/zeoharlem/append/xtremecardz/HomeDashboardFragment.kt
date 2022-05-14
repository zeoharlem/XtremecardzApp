package com.zeoharlem.append.xtremecardz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.wajahatkarim3.easyvalidation.core.collection_ktx.nonEmptyList
import com.zeoharlem.append.xtremecardz.databinding.FragmentHomeDashboardBinding
import com.zeoharlem.append.xtremecardz.viewmodels.LoginViewModel
import com.zeoharlem.autonowartisans.sealed.AuthState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeDashboardFragment : Fragment() {

    private var _binding: FragmentHomeDashboardBinding? = null
    private val loginViewModel by activityViewModels<LoginViewModel>()
    private val binding get() = _binding!!
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding    = FragmentHomeDashboardBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding    = null
    }


}