package com.zeoharlem.append.xtremecardz.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.wajahatkarim3.easyvalidation.core.collection_ktx.nonEmptyList
import com.zeoharlem.append.xtremecardz.PhotoCamera
import com.zeoharlem.append.xtremecardz.databinding.FragmentProfileFormBinding
import com.zeoharlem.append.xtremecardz.models.Profile
import com.zeoharlem.append.xtremecardz.viewmodels.LoginViewModel
import com.zeoharlem.append.xtremecardz.utils.MyCustomExtUtils.capitalizeWords

class ProfileFormFragment : Fragment() {

    private var profile: Profile? = null
    private var _binding: FragmentProfileFormBinding? = null
    private val loginView by activityViewModels<LoginViewModel>()
    private val argsLazy: ProfileFormFragmentArgs by navArgs()
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding    = FragmentProfileFormBinding.inflate(layoutInflater)
        binding.setupAction.text    = argsLazy.cardType.capitalizeWords()
        gotoNextPhotoCameraScreen()
        return binding.root
    }

    private fun gotoNextPhotoCameraScreen(){
        binding.nextToCamera.setOnClickListener {
            val nonEmptyAction  = nonEmptyList(
                binding.fullname,
                binding.companyName,
                binding.designation,
                binding.email,
                binding.phone,
                binding.companyAddress,
                binding.websiteLink,
                binding.backContent
            ) { view: EditText, message: String ->
                view.error  = message
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
            if(nonEmptyAction){
                profile = Profile(
                    email = binding.email.text.toString().trim(),
                    phone = binding.phone.text.toString().trim(),
                    fullname = binding.fullname.text.toString().trim(),
                    companyName = binding.companyName.text.toString().trim(),
                    websiteLink = binding.websiteLink.text.toString().trim(),
                    designation = binding.designation.text.toString().trim(),
                    companyAddress = binding.companyAddress.text.toString().trim(),
                    backContentDesc = binding.backContent.text.toString().trim(),
                    uid = loginView.getMyFirebaseAuth()?.currentUser!!.uid,
                    cardNumbers = binding.cardNumbers.text.toString().trim(),
                    cardType = argsLazy.cardType,
                )
                //perform a network call here(optional)
                startActivity(Intent(requireContext(), PhotoCamera::class.java)
                    .putExtra("profileForm", profile)
                )
            }
            //startActivity(Intent(requireContext(), PhotoCamera::class.java))

        }
    }
}