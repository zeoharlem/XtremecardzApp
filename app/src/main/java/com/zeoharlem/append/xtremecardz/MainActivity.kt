package com.zeoharlem.append.xtremecardz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.wajahatkarim3.easyvalidation.core.collection_ktx.nonEmptyList
import com.zeoharlem.append.xtremecardz.databinding.ActivityMainBinding
import com.zeoharlem.append.xtremecardz.viewmodels.LoginViewModel
import com.zeoharlem.autonowartisans.sealed.AuthState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var activityMainBinding: ActivityMainBinding? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        loginViewModel      = ViewModelProvider(this)[LoginViewModel::class.java]

        setSupportActionBar(activityMainBinding!!.toolbar)

        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setHomeButtonEnabled(false)
            setDisplayHomeAsUpEnabled(false)
        }

        setContentView(activityMainBinding!!.root)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_favorites,
            )
        )
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController       = navHostFragment.findNavController()
        setupActionBarWithNavController(navController)
        //activityMainBinding.navView.setupWithNavController(navController)
//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            when(destination.id){
//
//            }
//        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun resetMaterialToolbar(toolbar: MaterialToolbar){
        val materialShapeDrawable   = toolbar.background as MaterialShapeDrawable
        materialShapeDrawable.shapeAppearanceModel = materialShapeDrawable.shapeAppearanceModel
            .toBuilder()
            .setAllCorners(CornerFamily.ROUNDED, android.R.attr.radius.toFloat())
            .build()
    }
}