package com.bicifiapp.ui.fragments.questions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.bicifiapp.R
import com.bicifiapp.databinding.FragmentQuestionBinding

class QuestionFragment : Fragment(R.layout.fragment_question) {

    private var _binding: FragmentQuestionBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentQuestionBinding.bind(view)
        initListeners()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initListeners() {
        binding.lblExtraInformation.setOnClickListener {
            if( binding.textExtraDescription.visibility != View.VISIBLE) {
                binding.textExtraDescription.visibility = View.VISIBLE
            }else {
                binding.textExtraDescription.visibility = View.GONE
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = QuestionFragment()
    }
}
