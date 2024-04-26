package com.example.musicplayerlite.screen.onboarding

import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.art.aiartgenerator.extention.setGradientText
import com.example.musicplayerlite.R
import com.example.musicplayerlite.adapter.BaseFragmentPagerAdapter
import com.example.musicplayerlite.base.BaseFragment
import com.example.musicplayerlite.databinding.FragmentOnboardingBinding

class OnboardingFragment : BaseFragment<FragmentOnboardingBinding>() {

    private lateinit var adapterFragment: BaseFragmentPagerAdapter
    private val fragments = mutableListOf<Fragment>()
    override fun getViewBinding(): FragmentOnboardingBinding {
        return FragmentOnboardingBinding.inflate(layoutInflater)
    }

    override fun initData() {
        initFragments()
    }

    private fun initFragments() {
        buildList<Fragment> {
            add(
                OnboardingSlideFragment.newInstance(
                    bundle = bundleOf(OnboardingSlideFragment.ARG_POSITION_SCREEN to OnboardingSlideFragment.PAGE_0)
                )
            )
            add(
                OnboardingSlideFragment.newInstance(
                    bundle = bundleOf(OnboardingSlideFragment.ARG_POSITION_SCREEN to OnboardingSlideFragment.PAGE_1)
                )
            )
            add(
                OnboardingSlideFragment.newInstance(
                    bundle = bundleOf(OnboardingSlideFragment.ARG_POSITION_SCREEN to OnboardingSlideFragment.PAGE_2)
                )
            )
        }.let(fragments::addAll)

    }

    override fun initView() {
        setUpDataAdapter()
        setUpViewPagerOnboarding()
    }

    private fun setUpDataAdapter() {
        adapterFragment = BaseFragmentPagerAdapter(childFragmentManager, lifecycle)
        adapterFragment.setFragments(fragments)
    }

    private fun setUpViewPagerOnboarding() {
        with(binding.vpOnboarding) {
            adapter = adapterFragment
            offscreenPageLimit = 1
            binding.indPager.attachTo(this)
        }
    }

    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            configUiButtonNextAction()
        }
    }

    private fun configUiButtonNextAction() {
        if (hasLastPage()) {
            binding.tvNextAction.text = "Get Started"
            binding.tvNextAction.setGradientText(
                ContextCompat.getColor(baseContext, R.color.gradient_start_color),
                ContextCompat.getColor(baseContext, R.color.gradient_end_color)
            )
        } else {
            binding.tvNextAction.text = "Next"
            binding.tvNextAction.setTextColor(
                ContextCompat.getColor(
                    baseContext,
                    R.color.white
                )
            )
        }
    }

    override fun onStart() {
        super.onStart()
        binding.vpOnboarding.registerOnPageChangeCallback(pageChangeCallback)
    }

    override fun onStop() {
        super.onStop()
        binding.vpOnboarding.unregisterOnPageChangeCallback(pageChangeCallback)
    }

    override fun initViewListener() {
        binding.tvNextAction.setOnClickListener {
            if (hasLastPage()) {
                navigateToHomeScreen()
            } else {
                openNextSlide()
            }
        }
    }

    private fun openNextSlide() {
        binding.vpOnboarding.currentItem += 1
    }

    private fun navigateToHomeScreen() {
        findNavController().navigate(R.id.homeFragment)
    }

    private fun hasLastPage(): Boolean {
        return binding.vpOnboarding.currentItem == (adapterFragment.itemCount - 1)
    }

    override fun initObserver() {
        /* no-op */
    }
}