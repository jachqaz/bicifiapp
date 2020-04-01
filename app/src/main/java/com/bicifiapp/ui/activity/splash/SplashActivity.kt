package com.bicifiapp.ui.activity.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.bicifiapp.R
import com.bicifiapp.databinding.ActivitySplashBinding
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
                val intent = Intent(applicationContext, OnboardingActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun onAnimationStart(animation: Animation?) {

            }

        })
    }
}
