package com.zeoharlem.append.xtremecardz.ui

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.zeoharlem.append.xtremecardz.LoginActivity
import com.zeoharlem.append.xtremecardz.PhotoCamera
import com.zeoharlem.append.xtremecardz.R
import com.zeoharlem.append.xtremecardz.adapters.ImageAdapter
import com.zeoharlem.append.xtremecardz.databinding.FragmentDashboardBinding
import com.zeoharlem.append.xtremecardz.notifications.MyAppsNotificationManager
import com.zeoharlem.append.xtremecardz.sealed.TimerEvent
import com.zeoharlem.append.xtremecardz.services.MyUploadService
import com.zeoharlem.append.xtremecardz.ui.sliderfragment.SliderFragmentOne
import com.zeoharlem.append.xtremecardz.ui.sliderfragment.SliderFragmentThree
import com.zeoharlem.append.xtremecardz.ui.sliderfragment.SliderFragmentTwo
import com.zeoharlem.append.xtremecardz.utils.Constants
import com.zeoharlem.append.xtremecardz.utils.MyCustomExtUtils.capitalizeWords
import com.zeoharlem.append.xtremecardz.utils.XtremeCardzUtils
import com.zeoharlem.append.xtremecardz.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val loginViewModel by activityViewModels<LoginViewModel>()
    private lateinit var googleSignInClient: GoogleSignInClient
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val googleSignOptions   = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.webclient_id)).requestEmail().build()

        googleSignInClient      = GoogleSignIn.getClient(requireActivity(), googleSignOptions)
        getSetActiveTokenUsingGoogleSignIn()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding    = FragmentDashboardBinding.inflate(layoutInflater)

        gotoUploadZipFileScreen()
        signOutAction()

        val fragmentSlideScreen = arrayListOf<Fragment>(
            SliderFragmentOne(),
            SliderFragmentTwo(),
            SliderFragmentThree()
        )

        val fmBoardingAdapter   = ScreenSlidePagerAdapter(
            fragmentSlideScreen,
            requireActivity().supportFragmentManager,
            lifecycle
        )
        binding.homePagerSlider.adapter = fmBoardingAdapter

        binding.homePagerSlider.setPageTransformer(ZoomOutPageTransformer())

        binding.getTemplates.setOnClickListener{
            Toast.makeText(requireContext(), "Not Available Yet. Coming Soon!", Toast.LENGTH_SHORT)
                .show()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val firstname = XtremeCardzUtils.readKey("firstname", requireContext())
        val lastname = XtremeCardzUtils.readKey("lastname", requireContext())
        binding.username.text = "$firstname $lastname".capitalizeWords()
        binding.emailAddress.text = "${XtremeCardzUtils.readKey("email", requireContext())}"
        gotoProfileFormScreen()
    }

    private fun getSetActiveTokenUsingGoogleSignIn(){
        val checkJwtoken: String? = XtremeCardzUtils.readKey("token", requireContext())
        if(checkJwtoken!!.isEmpty()){
            findNavController().navigate(R.id.activateAccountFragment)
        }
    }

    private fun gotoUploadZipFileScreen(){
        binding.designRequest.setOnClickListener {
            val action = DashboardFragmentDirections.actionDashboardFragmentToUploadZipFragment()
            findNavController().navigate(action)
        }
    }

    private fun gotoProfileFormScreen(){
        /**
        binding.submitDesignRequest.setOnClickListener {
            val action  = DashboardFragmentDirections.actionDashboardFragmentToProfileFormFragment("id card")
            findNavController().navigate(action)
        }
        **/

        /**
        binding.submitBuzDesign.setOnClickListener {
            val action  = DashboardFragmentDirections.actionDashboardFragmentToProfileFormFragment("buz card")
            findNavController().navigate(action)
        }**/

        binding.checkProjects.setOnClickListener {
            val action  = DashboardFragmentDirections.actionDashboardFragmentToViewProjectsFragment(null)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding    = null
    }

    private fun signOutAction(){
        binding.signOutNow.setOnClickListener {
            loginViewModel.resetAuthenticity()
            XtremeCardzUtils.removeKey("token", requireContext())
            XtremeCardzUtils.removeKey("firstname", requireContext())
            XtremeCardzUtils.removeKey("lastname", requireContext())
            XtremeCardzUtils.removeKey("email", requireContext())
            Auth.GoogleSignInApi.signOut(googleSignInClient.asGoogleApiClient())
            requireActivity().startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private inner class ScreenSlidePagerAdapter(
        private val listFragment: ArrayList<Fragment>,
        fm: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle) {
        override fun getItemCount(): Int {
            return listFragment.size
        }

        override fun createFragment(position: Int): Fragment {
            return listFragment[position]
        }
    }

    companion object {
        private const val MIN_SCALE = 0.85f
        private const val MIN_ALPHA = 0.5f
    }

    private inner class ZoomOutPageTransformer : ViewPager2.PageTransformer {

        override fun transformPage(view: View, position: Float) {
            view.apply {
                val pageWidth = width
                val pageHeight = height
                when {
                    position < -1 -> { // [-Infinity,-1)
                        // This page is way off-screen to the left.
                        alpha = 0f
                    }
                    position <= 1 -> { // [-1,1]
                        // Modify the default slide transition to shrink the page as well
                        val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                        val vertMargin = pageHeight * (1 - scaleFactor) / 2
                        val horzMargin = pageWidth * (1 - scaleFactor) / 2
                        translationX = if (position < 0) {
                            horzMargin - vertMargin / 2
                        } else {
                            horzMargin + vertMargin / 2
                        }

                        // Scale the page down (between MIN_SCALE and 1)
                        scaleX = scaleFactor
                        scaleY = scaleFactor

                        // Fade the page relative to its size.
                        alpha = (MIN_ALPHA +
                                (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                    }
                    else -> { // (1,+Infinity]
                        // This page is way off-screen to the right.
                        alpha = 0f
                    }
                }
            }
        }
    }


}