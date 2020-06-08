package com.bicifiapp.ui.fragments.termandcondition

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bicifiapp.R
import com.bicifiapp.databinding.FragmentTermConditionBinding
import com.bicifiapp.extensions.animSlideLeftToRight

class TermConditionFragment : Fragment(R.layout.fragment_term_condition) {

    private var _binding: FragmentTermConditionBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTermConditionBinding.bind(view)
        initListeners()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initListeners() {

        setTextDescriptionKnowledge(getString(R.string.terms_conditions), binding.textTermsConditions)
        setTextDescriptionKnowledge(getString(R.string.privacy_policy), binding.textPolicyPrivacy)

        binding.btnAccept.setOnClickListener {
                findNavController().navigate(
                    R.id.signInFragment,
                    null,
                    animSlideLeftToRight()
                )
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            TermConditionFragment()

    }

    private fun setTextDescriptionKnowledge(text: String, view: TextView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            view.text = Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
        } else {
            view.text = Html.fromHtml(text)
        }
    }
}
