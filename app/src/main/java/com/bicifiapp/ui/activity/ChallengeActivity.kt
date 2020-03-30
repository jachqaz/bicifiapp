package com.bicifiapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.bicifiapp.R

class ChallengeActivity : AppCompatActivity() {
    constructor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge)

    }

    private fun setFragment(fragment: Fragment){
        supportFragmentManager.commit {
            add(R.id.challengeActivity, fragment)
        }
    }
}
