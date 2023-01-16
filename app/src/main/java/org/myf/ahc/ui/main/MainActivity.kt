package org.myf.ahc.ui.main

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import org.myf.ahc.R
import org.myf.ahc.feature.registration.R as registrationResource
import org.myf.ahc.feature.home.R as homeResource
import org.myf.ahc.feature.healthcare.R as healthResource
import org.myf.ahc.ui.BaseActivity

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.main_tool_bar))
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_main_host_container
        ) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.setupWithNavController(navController)


        appBarConfiguration = AppBarConfiguration(
            setOf(homeResource.id.home_screen,registrationResource.id.main_screen,healthResource.id.health_care_list_screen)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }
}