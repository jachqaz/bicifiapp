package com.bicifiapp.ui.fragments.profile

import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bicifiapp.R
import com.bicifiapp.databinding.FragmentProfileBinding
import com.bicifiapp.ui.fragments.signin.DatePickerFragment

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val MY_PERMISSIONS_LOCATION: Int = 100

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)
        initListeners()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initListeners() {
        // set fragment date picker
        binding.profileBirthday.setOnClickListener {
            val newFragment = DatePickerFragment.newInstance(DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                val selectedDate = dayOfMonth.toString() + " / " + (month + 1) + " / " + year
                binding.profileBirthday.setText(selectedDate)
            })

            newFragment.show(activity?.supportFragmentManager!!, "datePicker")
        }

        // verify and get permission location android if switch is checked
        binding.switchLocalPass.setOnClickListener {
            if (binding.switchLocalPass.isChecked) {
                if (ContextCompat.checkSelfPermission(activity!!, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!,android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        ActivityCompat.requestPermissions(activity!!,
                            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                            MY_PERMISSIONS_LOCATION)
                    }
                }
            }
        }

        // get data from send firebase when user accep buttom start text
        binding.btnStartTest.setOnClickListener {
            val birthday = binding.profileBirthday.text.toString()
            val familyRol = binding.familyRol.text.toString()
            val educationLevel = binding.educationLevel.text.toString()
            val yearExperience = binding.yearExperience.text.toString()
            var notificationPass = false
            var locationPass = false
            if (binding.switchLocalPass.isChecked) {
                locationPass = true
            }
            if (binding.switchNotificationPass.isChecked) {
                notificationPass = true
            }
            System.out.println("birthday: " + birthday + "\n" + "familyRol: " + familyRol + "\n" + "educationLevel: " + educationLevel + "\n" + "yearExperience: " + yearExperience + "\n"
            + "notificationPass: " + notificationPass + "\n" + "locationPass: " + locationPass)


        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }
}
