package com.zeoharlem.append.xtremecardz.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zeoharlem.append.xtremecardz.R
import com.zeoharlem.append.xtremecardz.databinding.FragmentProjectsBinding

class ProjectsFragment : Fragment() {

    private var _binding: FragmentProjectsBinding?  = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding    = FragmentProjectsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding    = null
    }
}