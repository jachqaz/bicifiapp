package com.bicifiapp.ui.activity

import android.os.Bundle
import com.bicifiapp.R
import com.codemybrainsout.onboarder.AhoyOnboarderActivity
import com.codemybrainsout.onboarder.AhoyOnboarderCard
import com.codemybrainsout.onboarder.AhoyOnboarderFragment
import java.util.*

class OnboardingActivity : AhoyOnboarderActivity() {

    private companion object {
        const val TITLE_TEXT_SIZE = 10
        const val DESCRIPTION_TEXT_SIZE = 8
        const val DESCRIPTION_TEXT_SIZE_2 = 7
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createCards()
        setGradientBackground()
        setFinishButtonTitle(R.string.lbl_challenge)
    }

    override fun onFinishButtonPressed() {

    }

    private fun createCards() {
        val pages = ArrayList<AhoyOnboarderCard>().also {
            it.add(
                createCard(
                    title = getString(R.string.lbl_what_is_test),
                    description = getString(R.string.lbl_description_test)
                )
            )
            it.add(
                createCard(
                    getString(R.string.lbl_reach_25),
                    getString(R.string.lbl_question_finances),
                    R.drawable.onboarding_card1
                )
            )
            it.add(
                createCard(
                    getString(R.string.lbl_reach_25_50),
                    getString(R.string.lbl_dreamer),
                    R.drawable.onboarding_card2
                )
            )
            it.add(
                createCard(
                    getString(R.string.lbl_reach_50_75),
                    getString(R.string.lbl_executor),
                    R.drawable.onboarding_card3
                )
            )
            it.add(
                createCard(
                    getString(R.string.lbl_reach_75),
                    getString(R.string.lbl_leader),
                    R.drawable.onboarding_card4
                )
            )
        }

        setOnboardPages(pages)

    }

    private fun createCard(title: String, description: String, icon: Int = 0) =
        when (icon) {
            0 -> AhoyOnboarderCard(title, description).also {
                it.backgroundColor = R.color.black_transparent
                it.titleColor = R.color.white
                it.descriptionColor = R.color.grey_200
                it.titleTextSize = dpToPixels(TITLE_TEXT_SIZE, this)
                it.descriptionTextSize = dpToPixels(DESCRIPTION_TEXT_SIZE_2, this)
            }
            else -> {
                AhoyOnboarderCard(title, description, icon).also {
                    it.backgroundColor = R.color.black_transparent
                    it.titleColor = R.color.white
                    it.descriptionColor = R.color.grey_200
                    it.titleTextSize = dpToPixels(TITLE_TEXT_SIZE, this)
                    it.descriptionTextSize = dpToPixels(DESCRIPTION_TEXT_SIZE, this)
                }
            }
        }

}
