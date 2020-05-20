package com.bicifiapp.ui.activity.splash

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.bicifiapp.R
import com.bicifiapp.common.Constants
import com.bicifiapp.databinding.ActivitySplashBinding
import com.bicifiapp.extensions.empty
import com.bicifiapp.extensions.getSharedPreferences
import com.bicifiapp.extensions.userId
import com.bicifiapp.ui.activity.homescreen.HomeScreenActivity
import com.bicifiapp.ui.activity.onboarding.OnboardingActivity
import com.bicifiapp.ui.activity.signin.SignInActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAnimation()
    }

    private fun initAnimation() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.splash_transition)
        binding.imvLogo.animation = animation
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                val intent = Intent(applicationContext, OnboardingActivity::class.java)
                startActivity(intent)
                if (userId().isNullOrEmpty()) {
                    val onBoardingDone =
                        getSharedPreferences()?.getString(Constants.ONBOARDING_DONE, String.empty())
                    if (onBoardingDone.isNullOrEmpty()) {
                        val intentOnBoarding =
                            Intent(applicationContext, OnboardingActivity::class.java)
                        startActivity(intentOnBoarding)
                    } else {
                        val intentHome = Intent(applicationContext, SignInActivity::class.java)
                        startActivity(intentHome)
                    }
                } else {
                    initHomeScreen()
                }
                finish()
            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })
    }

    private fun initHomeScreen() {
        val intentHome = Intent(applicationContext, HomeScreenActivity::class.java)
        startActivity(intentHome)
    }
}
