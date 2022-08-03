package com.zeoharlem.append.xtremecardz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.zeoharlem.append.xtremecardz.databinding.FragmentUploadZipBinding

class UploadZipFragment : Fragment() {
    private var isTimerRunning = false
    private var _binding: FragmentUploadZipBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding    = FragmentUploadZipBinding.inflate(layoutInflater)
        binding.floatingActionButton.setOnClickListener {
            //toggleUploadAction()
            findNavController().navigate(R.id.uploadBottomSheetFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}