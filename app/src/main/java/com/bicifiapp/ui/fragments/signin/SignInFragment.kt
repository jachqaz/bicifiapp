package com.bicifiapp.ui.fragments.signin

import android.content.Intent
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import co.devhack.androidextensions.components.liveDataObserve
import co.devhack.androidextensions.ui.startActivity
import co.devhack.base.State
import co.devhack.base.error.Failure
import co.devhack.presentation.BaseFragment
import com.bicifiapp.BuildConfig
import com.bicifiapp.R
import com.bicifiapp.databinding.FragmentSignInBinding
import com.bicifiapp.extensions.animSlideLeftToRight
import com.bicifiapp.extensions.userId
import com.bicifiapp.notificationssettings.repository.Profile
import com.bicifiapp.ui.activity.homescreen.HomeScreenActivity
import com.bicifiapp.ui.activity.signin.SignInActivity
import com.bicifiapp.ui.dialogs.DialogLoading
import com.bicifiapp.ui.dialogs.showAnimLoading
import com.bicifiapp.ui.viewmodels.profile.ProfileViewModel
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import org.koin.android.ext.android.inject
import timber.log.Timber

class SignInFragment : BaseFragment(R.layout.fragment_sign_in) {

    private val profileViewModel by inject<ProfileViewModel>()

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var dialogLoading: DialogLoading

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    private val callbackManager by lazy { CallbackManager.Factory.create() }

    private val providerTwitter by lazy { OAuthProvider.newBuilder(TWITTER_PROVIDER) }

    override fun initView(view: View) {
        _binding = FragmentSignInBinding.bind(view)
        initLiveData()
        initValues()
        initListener()
        initViewsProperties()
    }

    override fun showProgress() {
        dialogLoading = showAnimLoading()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun hideProgress() {
        dialogLoading.dismiss()
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
                Timber.e(e)
                authFailed()
            }
        } else if (requestCode == RC_SIGN_IN_FACEBOOK) {
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun initLiveData() {
        liveDataObserve(profileViewModel.getProfileLiveData, ::onGetProfileStateChange)
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
        profileViewModel.getProfileByUserId(userId())
    }

    private fun authFailed() {
        hideProgress()
        showSnackBar(R.string.auth_success_social_failed, R.color.error_snackbar)
    }

    private fun showSnackBar(resId: Int, colorId: Int) = view?.rootView?.let {
        Snackbar.make(it, resId, Snackbar.LENGTH_SHORT).apply {
            view.setBackgroundColor(ContextCompat.getColor(context, colorId))
            show()
        }
    }

    private fun onGetProfileStateChange(state: State?) =
        when (state) {
            is State.Failed -> {
                when (state.failure) {
                    is Failure.CustomError -> {
                        handlerNewUser()
                        hideProgress()
                    }
                    else -> {
                        handleFailure(state.failure)
                        hideProgress()
                    }
                }
            }
            State.Loading -> showProgress()
            State.Empty -> hideProgress()
            is State.Success -> {
                successfulGetProfile(state.responseTo())
                hideProgress()
            }
            null -> hideProgress()
        }

    private fun handlerNewUser() {
        findNavController().navigate(
            SignInFragmentDirections.nextProfileAction(),
            animSlideLeftToRight()
        )
    }

    private fun successfulGetProfile(profile: Profile) {
        showSnackBar(R.string.auth_success_social_network, R.color.success_snackbar)
        startActivity<HomeScreenActivity>()
    }

    private fun getFragmentContext() = (context as SignInActivity)

    companion object {
        private const val RC_SIGN_IN_GOOGLE = 1636
        private const val RC_SIGN_IN_FACEBOOK = 64206
        private const val TWITTER_PROVIDER = "twitter.com"
        private const val EMAIL_READ_PERMISSION = "email"
        private const val PROFILE_READ_PERMISSION = "public_profile"

        @JvmStatic
        fun getInstance(): SignInFragment {
            return SignInFragment()
        }
    }
}
