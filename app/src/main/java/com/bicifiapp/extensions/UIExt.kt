package com.bicifiapp.extensions

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.navigation.navOptions
import com.bicifiapp.R
import com.google.firebase.auth.FirebaseAuth

const val LAST_LEVEL = "level"
const val LAST_DATE_TEST = "date"
const val LAST_DATE_EMOTIONAL_TEST = "emotionalstatedate"
const val LAST_EMOTIONAL_STATE = "emotionalstate"

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

fun Fragment.getSharedPreferences() =
    activity?.let {
        it.getSharedPreferences(
            it.getString(R.string.name_file_shared_preferences),
            Context.MODE_PRIVATE
        )
    }

fun Fragment.activity() = this.requireActivity()

fun Fragment.claims(block: (HashMap<String, String>) -> Unit) {
    userAuth()?.let { user ->
        user.getIdToken(true)
            .addOnSuccessListener { token ->
                block(
                    HashMap<String, String>().apply {
                        put(LAST_LEVEL, token.claims[LAST_LEVEL].toString())
                        put(LAST_DATE_TEST, token.claims[LAST_DATE_TEST].toString())
                        put(LAST_EMOTIONAL_STATE, token.claims[LAST_EMOTIONAL_STATE].toString())
                    }
                )

            }
    }
}