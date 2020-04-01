package com.bicifiapp.ui.activity.signin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bicifiapp.R
import com.bicifiapp.ui.fragments.TermConditionFragment
import com.bicifiapp.ui.fragments.signin.SignInFragment
import timber.log.Timber

class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        setFragment(fragmentSignIn())
    }

    private fun fragmentSignIn() =
        SignInFragment.getInstance { options ->
            when (options) {
                SignInFragment.OPTIONS.PRIVACY_POLICY ->
                    setFragment(TermConditionFragment.newInstance())
                else -> Timber.i("Autenticado")
            }
        }

    private fun removeFragment(fragment: Fragment) {
        val fragmentTx = this.supportFragmentManager.beginTransaction()
        fragmentTx.remove(fragment)
        fragmentTx.commit()
    }

    private fun setFragment(fragment: Fragment) {
        val fragmentTx = this.supportFragmentManager.beginTransaction()
        fragmentTx.replace(R.id.signInActivity, fragment)
        fragmentTx.addToBackStack(null)
        fragmentTx.commit()
    }
}
