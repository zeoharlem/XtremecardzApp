package com.zeoharlem.append.xtremecardz.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.zeoharlem.append.xtremecardz.LoginActivity
import com.zeoharlem.append.xtremecardz.PhotoCamera
import com.zeoharlem.append.xtremecardz.R
import com.zeoharlem.append.xtremecardz.databinding.FragmentDashboardBinding
import com.zeoharlem.append.xtremecardz.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val loginViewModel by activityViewModels<LoginViewModel>()
    private lateinit var googleSignInClient: GoogleSignInClient
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val googleSignOptions   = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.webclient_id)).requestEmail().build()

        googleSignInClient      = GoogleSignIn.getClient(requireActivity(), googleSignOptions)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding    = FragmentDashboardBinding.inflate(layoutInflater)

        gotoProfileFormScreen()
        signOutAction()
        return binding.root
    }

    private fun gotoProfileFormScreen(){
        binding.submitDesignRequest.setOnClickListener {
            val action  = DashboardFragmentDirections.actionDashboardFragmentToProfileFormFragment("id card")
            findNavController().navigate(action)
        }

        binding.submitBuzDesign.setOnClickListener {
            val action  = DashboardFragmentDirections.actionDashboardFragmentToProfileFormFragment("buz card")
            findNavController().navigate(action)
        }

        binding.myRequestProject.setOnClickListener {
            findNavController().navigate(R.id.projectsFragment)
        }
    }

    private fun signOutAction(){
        binding.signOut.setOnClickListener {
            loginViewModel.resetAuthenticity()
            Auth.GoogleSignInApi.signOut(googleSignInClient.asGoogleApiClient())
            requireActivity().startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }
}