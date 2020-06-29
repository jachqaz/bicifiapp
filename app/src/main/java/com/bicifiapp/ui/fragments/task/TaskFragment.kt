package com.bicifiapp.ui.fragments.task

import android.view.View
import androidx.core.content.ContextCompat
import co.devhack.presentation.BaseFragment
import com.bicifiapp.R
import com.bicifiapp.databinding.FragmentTaskBinding
import com.bicifiapp.ui.dialogs.DialogLoading
import com.bicifiapp.ui.dialogs.showAnimLoading
import com.google.android.material.snackbar.Snackbar


class TaskFragment : BaseFragment(R.layout.fragment_task) {

    private lateinit var dialogLoading: DialogLoading
    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!

    override fun hideProgress() {
        dialogLoading.dismiss()
    }

    override fun initView(view: View) {
        _binding = FragmentTaskBinding.bind(view)
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
        binding.btnCreateTask.setOnClickListener {
            createTask()
        }
    }

    private fun createTask(){
        showSnackBar(R.string.app_name, R.color.error_snackbar)
    }

    private fun showSnackBar(resId: Int, colorId: Int) = view?.rootView?.let {
        Snackbar.make(it, resId, Snackbar.LENGTH_SHORT).apply {
            view.setBackgroundColor(ContextCompat.getColor(context, colorId))
            show()
        }
    }
}
