package com.zeoharlem.append.xtremecardz.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.zeoharlem.append.xtremecardz.R
import com.zeoharlem.append.xtremecardz.databinding.FragmentActivateAccountBinding
import com.zeoharlem.append.xtremecardz.sealed.NetworkResults
import com.zeoharlem.append.xtremecardz.utils.XtremeCardzUtils
import com.zeoharlem.append.xtremecardz.viewmodels.LoginViewModel
import kotlinx.coroutines.launch

class ActivateAccountFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentActivateAccountBinding?   = null
    private val loginViewModel by activityViewModels<LoginViewModel>()
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.setCancelable(false)
        _binding    = FragmentActivateAccountBinding.inflate(layoutInflater)
//        if(XtremeCardzUtils.readKey("firstname", requireContext())?.isEmpty() == true ||
//            XtremeCardzUtils.readKey("email", requireContext())?.isEmpty() == true){
//
//        }
        setClientGeneratedToken()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.activateNowBtn.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                getTokenUsingEmailPassword()
            }
        }
    }

    private fun setClientGeneratedToken(){
        loginViewModel.getAuthRepository().loginNetworkResults.observe(this){ network ->
            when(network){
                is NetworkResults.Loading -> {
                    Toast.makeText(requireContext(), "Refreshing...", Toast.LENGTH_SHORT).show()
                    binding.activateNowBtn.text = "Please wait!"
                    binding.activateNowBtn.isEnabled = false
                }
                is NetworkResults.Success -> network.dataSource?.token?.let {
                    //Log.e("ActivateAccount", "setClientGeneratedToken: $it")
                    XtremeCardzUtils.saveKey("token", it, requireContext())
                    dialog?.dismiss()
                }
                is NetworkResults.Error -> {
                    Toast.makeText(requireContext(), network.message, Toast.LENGTH_LONG).show()
                    binding.activateNowBtn.text = "Refresh Token"
                    binding.activateNowBtn.isEnabled = true
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding    = null
    }

    private suspend fun getTokenUsingEmailPassword(){
        val queryMap        = HashMap<String, String>()
        queryMap["email"]   = mAuth.currentUser!!.email!!
        queryMap["password"]= "password"
        loginViewModel.getAuthRepository().login(queryMap)
    }

    override fun onStart() {
        super.onStart()
        // remove black outer overlay, or change opacity
        dialog?.window?.also { window ->
            window.attributes?.also { attributes ->
                attributes.dimAmount    = 0.05f
                window.attributes       = attributes
            }
        }
    }

}