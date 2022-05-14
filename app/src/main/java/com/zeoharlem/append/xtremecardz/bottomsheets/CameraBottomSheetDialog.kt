package com.zeoharlem.append.xtremecardz.bottomsheets

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zeoharlem.append.xtremecardz.databinding.FragmentCameraBottomSheetBinding
import com.zeoharlem.append.xtremecardz.models.CapturedImage
import com.zeoharlem.append.xtremecardz.models.Profile
import ng.com.zeoharlem.swopit.utils.MyCustomExtUtils.capitalizeWords

class CameraBottomSheetDialog: BottomSheetDialogFragment() {

    private var _binding: FragmentCameraBottomSheetBinding? = null
    private lateinit var capturedImageData: CapturedImage
    private var profileForm: Profile? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileForm         = requireArguments().getParcelable("profileForm")!!
        capturedImageData   = requireArguments().getParcelable("capturedImageData")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding    = FragmentCameraBottomSheetBinding.inflate(layoutInflater)

        startWhatsAppAction()
        setDirectCallAction()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.closeWindow.setOnClickListener {
            dialog?.dismiss()
        }
        binding.phoneNumber.text    = profileForm?.phone
        binding.email.text          = profileForm?.email
        binding.companyName.text    = profileForm?.companyName
        binding.companyAddress.text = profileForm?.companyAddress
        binding.backContent.text    = profileForm?.backContentDesc
        binding.designation.text    = profileForm?.designation
        binding.designation2.text   = profileForm?.designation
        binding.cardNumberRequested.text    = profileForm?.cardNumbers
        binding.fullname.text       = profileForm?.fullname?.capitalizeWords()
        binding.cardType.text       = profileForm?.cardType?.capitalizeWords()
        binding.profileImage.setImageURI(capturedImageData.capturedItem)

    }

    override fun onStart() {
        super.onStart()
        // remove black outer overlay, or change opacity
        dialog?.window?.also { window ->
            window.attributes?.also { attributes ->
                attributes.dimAmount    = 0f
                window.attributes       = attributes
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog  = BottomSheetDialog(requireContext())
        dialog.setOnShowListener {
            val bottomSheetDialog   = it as BottomSheetDialog
            val parentLayout        = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { parentBottom ->
                val behavior    = BottomSheetBehavior.from(parentBottom)
                setFullHeightState(parentBottom)
                behavior.state  = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    private fun setFullHeightState(bottomSheet: View){
        val layoutParams    = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams    = layoutParams
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
        binding.shareButton.setOnClickListener {
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