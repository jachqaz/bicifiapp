package com.bicifiapp.ui.activity.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bicifiapp.R
import com.bicifiapp.databinding.ActivityOnboardingBinding
import com.bicifiapp.ui.activity.signin.SignInActivity
import com.bicifiapp.ui.fragments.challenge.ChallengeFragment
import com.bicifiapp.ui.fragments.onboarding.OnboardingFragment

class OnboardingActivity : FragmentActivity(), ViewPager.OnPageChangeListener {

    private lateinit var binding: ActivityOnboardingBinding

    private val fragmentList = mutableListOf<Fragment>()
    private val indicatorList = mutableListOf<ImageView>()
    private lateinit var pagerAdapter: FragmentStatePagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListFragment()
        initIndicatorOnboarding()
        initOnboarding()
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        binding.lytButtons.visibility =
            if (position + 1 == pagerAdapter.count) View.GONE else View.VISIBLE

        if (indicatorList.size > 0) {
            indicatorList.forEach { it.setImageResource(R.drawable.indicator_unselected) }
            indicatorList[position].setImageResource(R.drawable.indicator_selected)
        }
    }

    private fun initOnboarding() {
        pagerAdapter = OnBoardingAdapter(supportFragmentManager)
        binding.viewPagerContainer.adapter = pagerAdapter
        binding.viewPagerContainer.addOnPageChangeListener(this)
        binding.viewPagerContainer.currentItem = 0
        binding.introBtnNext.setOnClickListener {
            binding.viewPagerContainer.currentItem.let {
                binding.viewPagerContainer.currentItem = it + 1
            }
        }
    }

    private inner class OnBoardingAdapter(fm: FragmentManager) :
        FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment = fragmentList[position]
        override fun getCount(): Int = fragmentList.size
    }

    private fun initIndicatorOnboarding() {
        binding.indicatorContainer.let {
            if (fragmentList.size > 1)
                for (i in 0 until fragmentList.size) {
                    val indicator = LayoutInflater.from(this)
                        .inflate(R.layout.view_indicator, binding.indicatorContainer, false)
                    indicatorList.add(i, indicator as ImageView)
                    binding.indicatorContainer.addView(indicator)
                }
        }
    }

    private fun initListFragment() {
        addFragment(
            OnboardingFragment.newInstance(
                0,
                this@OnboardingActivity.resources.getString(R.string.lbl_what_is_test),
                this@OnboardingActivity.resources.getString(R.string.lbl_description_test)
            )
        )
        addFragment(
            OnboardingFragment.newInstance(
                R.drawable.onboarding_card1,
                this@OnboardingActivity.resources.getString(R.string.lbl_reach_25),
                this@OnboardingActivity.resources.getString(R.string.lbl_question_finances)
            )
        )
        addFragment(
            OnboardingFragment.newInstance(
                R.drawable.onboarding_card2,
                this@OnboardingActivity.resources.getString(R.string.lbl_reach_25_50),
                this@OnboardingActivity.resources.getString(R.string.lbl_dreamer)
            )
        )
        addFragment(
            OnboardingFragment.newInstance(
                R.drawable.onboarding_card3,
                this@OnboardingActivity.resources.getString(R.string.lbl_reach_50_75),
                this@OnboardingActivity.resources.getString(R.string.lbl_executor)
            )
        )
        addFragment(
            OnboardingFragment.newInstance(
                R.drawable.onboarding_card4,
                this@OnboardingActivity.resources.getString(R.string.lbl_reach_75),
                this@OnboardingActivity.resources.getString(R.string.lbl_leader)
            )
        )
        addFragment(
            ChallengeFragment.getInstance {
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
                finish()
            }
        )
    }

    fun addFragment(fragment: Fragment) = fragmentList.add(fragment)

}
