package com.bicifiapp.ui.activity.homescreen

import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import co.devhack.presentation.BaseActivity
import com.bicifiapp.R
import com.bicifiapp.databinding.ActivityHomeScreenBinding

class HomeScreenActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeScreenBinding

    override fun initView() {
        setContentView(R.layout.activity_home_screen)
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setupNavigation()
    }

    private fun setupNavigation() {
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return

        val navController = host.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeScreenFragment, R.id.emotionalQuestionFragment, R.id.statisticsFragment
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
    }

}
