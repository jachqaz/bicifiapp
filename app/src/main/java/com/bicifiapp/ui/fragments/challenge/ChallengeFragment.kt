package com.bicifiapp.ui.fragments.challenge

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.bicifiapp.R
import com.bicifiapp.databinding.FragmentChallengeBinding


class ChallengeFragment : Fragment(R.layout.fragment_challenge) {

    private var _binding: FragmentChallengeBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentChallengeBinding.bind(view)
        initListener()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initListener() {
        binding.btnFreemium.setOnClickListener {
            callback?.invoke()
        }

        binding.btnBuy.setOnClickListener {
            callback?.invoke()
        }

    }

    companion object {
        private var callback: (() -> Unit)? = null

        @JvmStatic
        fun getInstance(callback: (() -> Unit)? = null): ChallengeFragment {
            this.callback = callback
            return ChallengeFragment()
        }
    }
}
