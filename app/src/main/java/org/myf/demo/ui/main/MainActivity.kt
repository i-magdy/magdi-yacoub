package org.myf.demo.ui.main

import android.content.res.Configuration
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.elevation.SurfaceColors
import dagger.hilt.android.AndroidEntryPoint
import org.myf.demo.databinding.ActivityMainBinding
import org.myf.demo.feature.registration.R as registrationResource
import org.myf.demo.feature.home.R as homeResource
import org.myf.demo.feature.healthcare.R as healthResource
import org.myf.demo.ui.BaseActivity

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.mainToolBar)
        val navHostFragment = supportFragmentManager.findFragmentById(
            binding.navMainHostContainer.id
        ) as NavHostFragment
        when(resources.configuration.orientation){
            Configuration.ORIENTATION_PORTRAIT -> window.navigationBarColor = SurfaceColors.SURFACE_2.getColor(this)
            Configuration.ORIENTATION_LANDSCAPE -> window.navigationBarColor = SurfaceColors.SURFACE_0.getColor(this)
            Configuration.ORIENTATION_UNDEFINED -> {}
        }
        navController = navHostFragment.navController
        binding.bottomNav?.setupWithNavController(navController)
        binding.navRail?.setupWithNavController(navController)
        binding.mainBar.addLiftOnScrollListener { _, backgroundColor ->
            window.statusBarColor = backgroundColor
        }
        appBarConfiguration = AppBarConfiguration(
            setOf(
                homeResource.id.home_start_screen,
                registrationResource.id.registration_start_fragment,
                healthResource.id.health_care_start_screen
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }
}