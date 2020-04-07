package com.bicifiapp.ui.fragments.questions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.bicifiapp.R
import com.bicifiapp.databinding.FragmentEmotionalQuestionBinding

class EmotionalQuestionFragment : Fragment(R.layout.fragment_emotional_question) {

    private var _binding: FragmentEmotionalQuestionBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEmotionalQuestionBinding.bind(view)
        initListeners()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initListeners() {
        binding.btnEmotionalVeryDissatisfied.setOnClickListener {
            Toast.makeText(context, R.string.i_am_defeated, Toast.LENGTH_SHORT).show()
        }
        binding.btnEmotionalDissatisfied.setOnClickListener {
            Toast.makeText(context, R.string.i_am_soso, Toast.LENGTH_SHORT).show()
        }
        binding.btnEmotionalSatisfied.setOnClickListener {
            Toast.makeText(context, R.string.i_am_almost_fine, Toast.LENGTH_SHORT).show()
        }
        binding.btnEmotionalVerySatisfied.setOnClickListener {
            Toast.makeText(context, R.string.i_am_fine, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = EmotionalQuestionFragment()
    }
}
