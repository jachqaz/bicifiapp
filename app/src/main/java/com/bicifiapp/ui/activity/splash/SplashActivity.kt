package com.bicifiapp.ui.activity.splash

import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import co.devhack.androidextensions.ui.startActivity
import com.bicifiapp.R
import com.bicifiapp.databinding.ActivitySplashBinding
import com.bicifiapp.extensions.userId
import com.bicifiapp.ui.activity.homescreen.HomeScreenActivity
import com.bicifiapp.ui.activity.onboarding.OnboardingActivity

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
                if (userId().isNullOrEmpty()) {
                    startActivity<OnboardingActivity>()
                } else {
                    startActivity<HomeScreenActivity>()
                }
                finish()
            }

            override fun onAnimationStart(animation: Animation?) {
            }
        })
    }
}
