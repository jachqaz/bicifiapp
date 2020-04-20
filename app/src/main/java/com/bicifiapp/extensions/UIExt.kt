package com.bicifiapp.extensions

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.navigation.navOptions
import com.bicifiapp.R
import com.google.firebase.auth.FirebaseAuth

fun Fragment.userId() =
    FirebaseAuth.getInstance().currentUser?.uid.safeString()

fun Fragment.userAuth() =
    FirebaseAuth.getInstance().currentUser

fun Activity.userId() =
    FirebaseAuth.getInstance().currentUser?.uid

fun Activity.userAuth() =
    FirebaseAuth.getInstance().currentUser

fun Fragment.animSlideLeftToRight() = navOptions {
    anim {
        enter = R.anim.slide_in_right
        exit = R.anim.slide_out_left
        popEnter = R.anim.slide_in_left
        popExit = R.anim.slide_out_right
    }
}
