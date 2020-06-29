package com.bicifiapp.ui.fragments.task

import android.view.View
import androidx.fragment.app.Fragment
import co.devhack.presentation.BaseFragment
import com.bicifiapp.R
import com.bicifiapp.databinding.FragmentTaskListBinding
import com.bicifiapp.ui.dialogs.DialogLoading
import com.bicifiapp.ui.dialogs.showAnimLoading

class TaskListFragment : BaseFragment(R.layout.fragment_task_list) {

    private lateinit var dialogLoading: DialogLoading
    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    override fun hideProgress() {
        dialogLoading.dismiss()
    }

    override fun initView(view: View) {
        _binding = FragmentTaskListBinding.bind(view)
        initListeners()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun showProgress() {
        dialogLoading = showAnimLoading()
    }

    private fun initListeners() {
        binding.btnFab.setOnClickListener {  }
    }
}
