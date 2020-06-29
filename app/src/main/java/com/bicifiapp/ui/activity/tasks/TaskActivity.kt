package com.bicifiapp.ui.activity.tasks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import co.devhack.presentation.BaseActivity
import com.bicifiapp.R
import com.bicifiapp.databinding.ActivityTaskBinding

class TaskActivity : BaseActivity() {

    private lateinit var binding: ActivityTaskBinding

    override fun initView() {
        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

      /*  val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_home_screen) as NavHostFragment? ?: return*/

       // val navController = host.navController

    }


}
