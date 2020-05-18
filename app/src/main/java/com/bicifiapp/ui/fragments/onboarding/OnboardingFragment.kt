package com.bicifiapp.ui.fragments.onboarding

import android.content.ClipDescription
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.bicifiapp.R
import com.bicifiapp.databinding.FragmentOnboardingBinding
import kotlinx.android.synthetic.main.fragment_onboarding.*

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
            if (it.getInt(IMAGE_RES_ID) == 0) {
                //image is not required and show description static only
                binding.imgIcon.visibility = View.GONE
                binding.imgOnboarding.visibility = View.GONE
                binding.textDescriptionOnboarding.visibility = View.GONE
                binding.textDescOnboardingNoImage.visibility = View.VISIBLE
            } else {
                binding.imgIcon.setImageResource(it.getInt(ICON))
                binding.imgOnboarding.setImageResource(it.getInt(IMAGE_RES_ID))
                binding.textDescriptionOnboarding.text = it.getString(DESCRIPTION)
            }
            binding.textTitleOnboarding.text = it.getString(TITLE)
        }
    }

    companion object {
        private const val TITLE = "title"
        private const val DESCRIPTION = "description"
        private const val IMAGE_RES_ID = "imageResId"
        private const val ICON = "imageIcon"

        @JvmStatic
        fun newInstance(
            imageIcon: Int,
            imageResId: Int,
            title: String,
            description: String
        ) = OnboardingFragment().apply {
            arguments = Bundle().apply {
                putInt(ICON, imageIcon)
                putString(TITLE, title)
                putString(DESCRIPTION, description)
                putInt(IMAGE_RES_ID, imageResId)
            }
        }
    }
}