package com.bicifiapp.ui.fragments.signin

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bicifiapp.BuildConfig
import com.bicifiapp.R
import com.bicifiapp.databinding.FragmentSignInBinding
import com.bicifiapp.extensions.animSlideLeftToRight
import com.bicifiapp.ui.activity.signin.SignInActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.*
import timber.log.Timber


class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    private val callbackManager by lazy { CallbackManager.Factory.create() }

    private val providerTwitter by lazy { OAuthProvider.newBuilder(TWITTER_PROVIDER) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSignInBinding.bind(view)
        initValues()
        initListener()
        initViewsProperties()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN_GOOGLE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.let {
                    signInWithCredential(
                        GoogleAuthProvider.getCredential(
                            it.idToken,
                            null
                        )
                    )
                }
            } catch (e: ApiException) {
                authFailed()
            }
        } else if (requestCode == RC_SIGN_IN_FACEBOOK) {
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun initViewsProperties() {
        val txtPrivacy = SpannableString(getString(R.string.lbl_privacy_policy))
        txtPrivacy.setSpan(UnderlineSpan(), 0, txtPrivacy.length, 0)
        binding.lblPrivacyPolicy.text = txtPrivacy
    }

    private fun initListener() {
        binding.lblPrivacyPolicy.setOnClickListener {
            findNavController().navigate(
                SignInFragmentDirections.nextTermConditionAction(),
                animSlideLeftToRight()
            )
        }

        binding.signInGoogle.setOnClickListener {
            signInGoogle()
        }

        binding.signInTwitter.setOnClickListener {
            sigInTwitter()
        }
    }

    private fun sigInTwitter() {
        auth.startActivityForSignInWithProvider(getFragmentContext(), providerTwitter.build())
            .addOnSuccessListener {
                authSuccess()
            }
            .addOnFailureListener {
                Timber.e(it)
                authFailed()
            }
    }

    private fun signInGoogle() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(
            signInIntent,
            RC_SIGN_IN_GOOGLE
        )
    }

    private fun initValues() {
        initGoogleValues()
        initFacebookValues()
    }

    private fun initGoogleValues() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_GCP_KEY)
            .requestEmail()
            .requestProfile()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(getFragmentContext(), gso)
    }

    private fun initFacebookValues() {
        binding.signInFacebook.fragment = this
        binding.signInFacebook.setReadPermissions(
            EMAIL_READ_PERMISSION,
            PROFILE_READ_PERMISSION
        )

        binding.signInFacebook.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {

                override fun onSuccess(loginResult: LoginResult) {
                    signInWithCredential(FacebookAuthProvider.getCredential(loginResult.accessToken.token))
                }

                override fun onCancel() {
                    authFailed()
                }

                override fun onError(error: FacebookException) {
                    authFailed()
                }
            })
    }

    private fun signInWithCredential(credential: AuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            when {
                task.isSuccessful -> authSuccess()
                !task.isSuccessful -> authFailed()
                else -> authFailed()
            }
        }
    }

    private fun authSuccess() {
        showSnackBar(R.string.auth_success_social_network, R.color.success_snackbar)
        findNavController().navigate(
            SignInFragmentDirections.nextProfileAction(),
            animSlideLeftToRight()
        )
    }

    private fun authFailed() {
        showSnackBar(R.string.auth_success_social_failed, R.color.error_snackbar)
    }

    private fun showSnackBar(resId: Int, colorId: Int) = view?.rootView?.let {
        Snackbar.make(it, resId, Snackbar.LENGTH_SHORT).apply {
            view.setBackgroundColor(ContextCompat.getColor(context, colorId))
            show()
        }
    }

    private fun getFragmentContext() = (context as SignInActivity)

    companion object {
        const val RC_SIGN_IN_GOOGLE = 1636
        const val RC_SIGN_IN_FACEBOOK = 64206
        private const val TWITTER_PROVIDER = "twitter.com"
        private const val EMAIL_READ_PERMISSION = "email"
        private const val PROFILE_READ_PERMISSION = "public_profile"

        @JvmStatic
        fun getInstance(): SignInFragment {
            return SignInFragment()
        }

    }
}
