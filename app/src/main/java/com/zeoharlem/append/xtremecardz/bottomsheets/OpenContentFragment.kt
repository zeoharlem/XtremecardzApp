package com.zeoharlem.append.xtremecardz.bottomsheets

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.CircleCropTransformation
import com.zeoharlem.append.xtremecardz.R
import com.zeoharlem.append.xtremecardz.databinding.FragmentOpenContentBinding
import com.zeoharlem.append.xtremecardz.ui.ViewProjectsFragmentArgs
import com.zeoharlem.append.xtremecardz.utils.Constants
import com.zeoharlem.append.xtremecardz.utils.MyCustomExtUtils.capitalizeWords


class OpenContentFragment : Fragment() {
    private var _binding: FragmentOpenContentBinding? = null
    private val args: ViewProjectsFragmentArgs by navArgs()
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding    = FragmentOpenContentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.projectX?.let { projectX ->
            binding.fullname.text       = projectX.fullname!!.capitalizeWords()
            binding.companyAddress.text = projectX.companyAddress
            val imageUrlLink            = Constants.BASE_URL+ projectX.image
            //Log.e("Project", "binding: $imageUrlLink", )
            binding.profileImage.load(imageUrlLink){
                crossfade(true)
                transformations(CircleCropTransformation())
            }
            binding.websiteLink.text            = projectX.websiteLink.toString()
            binding.cardNumberRequested.text    = projectX.numberOfCards.toString()
            binding.designation2.text           = projectX.designation?.capitalizeWords()
            binding.designation.text            = projectX.designation?.capitalizeWords()
            binding.backContent.text            = projectX.backContent.toString()
            binding.companyAddress2.text        = projectX.companyAddress
            binding.cardType.text               = projectX.cardType
            binding.companyAddress.text         = projectX.email
        }
        startWhatsAppAction()
        setDirectCallAction()
    }

    @SuppressLint("LogNotTimber")
    private fun setDirectCallAction(){
        binding.shareProButton.setOnClickListener {
            val phoneNumber = "09030001851"
            val intent  = Intent(Intent.ACTION_DIAL)
            val phone   = phoneNumber.replaceFirst("^0+(?!$)".toRegex(),"234")
            requireActivity().startActivity(intent.setData(Uri.parse("tel:${phone}")))
        }
    }

    private fun startWhatsAppAction(){
        binding.shareButton1.setOnClickListener {
            val phoneNumber = "09030001851"
            val phone   = phoneNumber.replaceFirst("^0+(?!$)".toRegex(),"234")
            val url     = "https://api.whatsapp.com/send?phone=${phone}"
            try {
                val pm: PackageManager = requireActivity().packageManager
                pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
                val i       = Intent(Intent.ACTION_VIEW)
                i.data      = Uri.parse(url)
                startActivity(i)
            } catch (e: PackageManager.NameNotFoundException) {
                Toast.makeText(
                    requireContext(),
                    "Whatsapp app not installed in your phone",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }
}