package com.bicifiapp.ui.activity.homescreen

import android.content.Intent
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import co.devhack.presentation.BaseActivity
import com.bicifiapp.R
import com.bicifiapp.databinding.ActivityHomeScreenBinding
import com.bicifiapp.extensions.empty
import com.bicifiapp.extensions.getSharedPreferences
import com.bicifiapp.extensions.userAuth
import com.bicifiapp.ui.activity.signin.SignInActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth

class HomeScreenActivity : BaseActivity() {

    private companion object {
        const val TEXT_LAST_LEVEL = "text_last_level"
    }

    private lateinit var binding: ActivityHomeScreenBinding

    override fun initView() {
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_home_screen) as NavHostFragment? ?: return

        val navController = host.navController

        setUpNavigation(navController)

        setupActionBar()

        setNavigationItemSelectedListener()

        loadHeaderNavigationView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            android.R.id.home -> {
                binding.drawerLayoutHome.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    private fun setUpNavigation(navController: NavController) {
        binding.navView.setupWithNavController(navController)
    }

    private fun setupActionBar() {
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setNavigationItemSelectedListener() {
        binding.navViewHome.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.signOut -> {
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(applicationContext, SignInActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            binding.drawerLayoutHome.closeDrawers()
            true
        }
    }

    private fun loadHeaderNavigationView() {
        val header = binding.navViewHome.getHeaderView(0)
        header.findViewById<ImageView>(R.id.imgProfile)?.apply {
            Glide.with(this)
                .load(userAuth()?.photoUrl)
                .circleCrop()
                .into(this)
        }

        header.findViewById<TextView>(R.id.lblName)?.apply {
            text = userAuth()?.displayName
        }

        header.findViewById<TextView>(R.id.lblMemberType)?.apply {
            text = getString(R.string.lbl_member_free)
        }

        header.findViewById<TextView>(R.id.lblLevel)?.apply {
            text = getSharedPreferences().getString(TEXT_LAST_LEVEL, String.empty())
        }
    }
}
