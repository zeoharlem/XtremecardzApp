package com.zeoharlem.append.xtremecardz.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.zeoharlem.append.xtremecardz.adapters.ProjectInfoAdapter
import com.zeoharlem.append.xtremecardz.databinding.FragmentViewProjectsBinding
import com.zeoharlem.append.xtremecardz.utils.XtremeCardzUtils
import com.zeoharlem.append.xtremecardz.viewmodels.ProjectsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ViewProjectsFragment : Fragment() {

    private var _binding: FragmentViewProjectsBinding? = null
    private lateinit var projectInfoAdapter: ProjectInfoAdapter
    private var projectsViewModel: ProjectsViewModel? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        projectInfoAdapter  = ProjectInfoAdapter()
        projectsViewModel   = ViewModelProvider(requireActivity())[ProjectsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding    = FragmentViewProjectsBinding.inflate(layoutInflater)
        binding.projectItemRecyclerView.adapter = projectInfoAdapter
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding    = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getProjectInfoPagingItems()

        projectInfoAdapter.projectItemClickListener = {
            val action  = ViewProjectsFragmentDirections.actionViewProjectsFragmentToOpenContentFragment(it)
            findNavController().navigate(action)
        }
    }

    private fun getProjectInfoPagingItems(){
        XtremeCardzUtils.readKey("token", requireContext())?.let { token ->
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                    projectsViewModel!!.getProjectXPagingItem("Bearer $token").collectLatest {
                        projectInfoAdapter.submitData(it)
                    }
                }
            }
        }
    }
}