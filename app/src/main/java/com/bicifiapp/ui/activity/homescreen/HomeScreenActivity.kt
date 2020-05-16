package com.bicifiapp.ui.activity.homescreen

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import co.devhack.presentation.BaseActivity
import com.bicifiapp.R
import com.bicifiapp.databinding.ActivityHomeScreenBinding
import com.bicifiapp.ui.fragments.home.HomeScreenFragment

class HomeScreenActivity : BaseActivity(), HomeScreenFragment.HomeListener {

    private lateinit var binding: ActivityHomeScreenBinding

    override fun initView() {
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpNavigation()
    }

    override fun repeatTest() {

    }

    private fun setUpNavigation() {
        try {
            val host: NavHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_home_screen) as NavHostFragment? ?: return

            val navController = host.navController

            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.homeScreenFragment, R.id.emotionalQuestionFragment, R.id.statisticsFragment
                )
            )

            binding.navView.setupWithNavController(navController)
        }catch (e: Exception){
            print(e)
        }
    }

}
