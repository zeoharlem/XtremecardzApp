package com.zeoharlem.append.xtremecardz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.zeoharlem.append.xtremecardz.databinding.FragmentHomeDashboardBinding
import com.zeoharlem.append.xtremecardz.viewmodels.LoginViewModel
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