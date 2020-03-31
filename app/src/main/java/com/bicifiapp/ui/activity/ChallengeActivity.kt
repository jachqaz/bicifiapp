package com.bicifiapp.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bicifiapp.R
import com.bicifiapp.ui.fragments.ChallengeFragment

class ChallengeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge)
        setFragment(ChallengeFragment.newInstance())
    }

    private fun setFragment(fragment: Fragment) {
        val fragmentTx = this.supportFragmentManager.beginTransaction()
        fragmentTx.replace(R.id.challengeActivity, fragment)
        fragmentTx.addToBackStack(null)
        fragmentTx.commit()
    }
}
