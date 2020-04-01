package com.bicifiapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.bicifiapp.R
import com.bicifiapp.databinding.FragmentTermConditionBinding

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
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = TermConditionFragment()

    }
}
