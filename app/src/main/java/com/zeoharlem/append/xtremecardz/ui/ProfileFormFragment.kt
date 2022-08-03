package com.zeoharlem.append.xtremecardz.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.zeoharlem.append.xtremecardz.notifications.MyAppsNotificationManager
import com.zeoharlem.append.xtremecardz.utils.Constants
import com.zeoharlem.append.xtremecardz.viewmodels.LoginViewModel
import com.zeoharlem.append.xtremecardz.utils.MyCustomExtUtils.capitalizeWords
import com.zeoharlem.append.xtremecardz.viewmodels.ZipDirectoryViewModel

class ProfileFormFragment : Fragment() {

    private var profile: Profile? = null
    private var _binding: FragmentProfileFormBinding? = null
    private val loginView by activityViewModels<LoginViewModel>()
    private val zipDirectoryView by activityViewModels<ZipDirectoryViewModel>()

    private lateinit var myAppNotificationManager: MyAppsNotificationManager

    private val argsLazy: ProfileFormFragmentArgs by navArgs()
    private var selectedCardType = "ID Cards"
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myAppNotificationManager = MyAppsNotificationManager(requireContext())
    }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("ProfileFormFragment", "onViewCreated: $argsLazy", )
        binding.cardType.setOnCheckedStateChangeListener { group, checkedIds ->
            for(checkSelectedId in checkedIds){
                if(binding.chipIdCard.id != checkSelectedId){
                    selectedCardType = "Business Card"
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding    = null
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
                binding.backContent,
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
                    cardType = selectedCardType,
                )
                //perform a network call here(optional)
                requireActivity().startActivity(
                    Intent(requireContext(), PhotoCamera::class.java)
                        .putExtra("profileForm", profile)
                        .putExtra("projectCode", argsLazy.projectFiles)
                        .putExtra("projectFiles", argsLazy.cardType)
                )
            }
            //startActivity(Intent(requireContext(), PhotoCamera::class.java))

        }
    }
}