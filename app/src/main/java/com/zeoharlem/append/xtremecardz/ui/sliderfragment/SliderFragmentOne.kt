package com.zeoharlem.append.xtremecardz.ui.sliderfragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.zeoharlem.append.xtremecardz.R

class SliderFragmentOne  : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.first_container, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val clickedButton = view.findViewById<Button>(R.id.ask_for_this)
        clickedButton.setOnClickListener {
            val phoneNumber = "09030001851"
            val intent  = Intent(Intent.ACTION_DIAL)
            val phone   = phoneNumber.replaceFirst("^0+(?!$)".toRegex(),"234")
            requireActivity().startActivity(intent.setData(Uri.parse("tel:${phone}")))
        }
    }
}