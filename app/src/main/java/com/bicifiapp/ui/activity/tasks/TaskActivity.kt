package com.bicifiapp.ui.activity.tasks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import co.devhack.presentation.BaseActivity
import com.bicifiapp.R
import com.bicifiapp.databinding.ActivityTaskBinding
import com.bicifiapp.extensions.safeString
import com.bicifiapp.extensions.userId
import com.bicifiapp.ui.dialogs.DialogLoading
import com.bicifiapp.ui.dialogs.showAnimLoading
import com.bicifiapp.ui.fragments.task.TaskFragment
import com.bicifiapp.ui.fragments.task.TaskListFragment

class TaskActivity : BaseActivity() {

    private lateinit var binding: ActivityTaskBinding
    private lateinit var userId: String
    private lateinit var dialogLoading: DialogLoading


    override fun hideProgress() {
        dialogLoading.dismiss()
    }

    override fun initView() {
        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        userId = userId().safeString()

        loadTaskListFragment()
        loadListener()

    }

    override fun showProgress() {
        dialogLoading = showAnimLoading()
    }

    private fun loadTaskListFragment(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
        transaction.replace(R.id.taskFrame, TaskListFragment.newInstance())
        transaction.commit()
    }

    private fun loadListener(){
        binding.btnFab.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
            transaction.replace(R.id.taskFrame, TaskFragment.newInstance())
            transaction.commit()
        }
    }

}
