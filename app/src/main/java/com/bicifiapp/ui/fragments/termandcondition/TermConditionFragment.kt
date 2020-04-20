package com.bicifiapp.ui.fragments.termandcondition

import android.os.Bundle
import android.view.View
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
}
