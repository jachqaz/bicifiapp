package com.bicifiapp.ui.fragments.onboarding

import android.content.ClipDescription
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.bicifiapp.R
import com.bicifiapp.databinding.FragmentOnboardingBinding

class OnboardingFragment : Fragment(R.layout.fragment_onboarding) {

    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentOnboardingBinding.bind(view)
        setDataFragment()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setDataFragment() {
        arguments?.let {
            if (arguments!!.getInt("imageResId") == 0) {
                binding.imgOnboarding.visibility = View.GONE
                binding.textDescriptionOnboarding.visibility = View.GONE
                binding.textDescOnboardingNoImage.visibility = View.VISIBLE
            } else {
                binding.imgOnboarding.setImageResource(it.getInt("imageResId"))
                binding.textDescriptionOnboarding.text = it.getString("description")
            }
            binding.textTitleOnboarding.text = it.getString("title")
        }
    }

    companion object {
        private const val TITLE = "title"
        private const val DESCRIPTION = "description"
        private const val IMAGE_RES_ID = "imageResId"

        @JvmStatic
        fun newInstance(
            imageResId: Int,
            title: String,
            description: String
        ) = OnboardingFragment().apply {
            arguments = Bundle().apply {
                putString(TITLE, title)
                putString(DESCRIPTION, description)
                putInt(IMAGE_RES_ID, imageResId)
            }
        }
    }
}