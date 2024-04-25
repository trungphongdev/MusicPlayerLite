package com.example.musicplayerlite.screen.onboarding

import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.musicplayerlite.R
import com.example.musicplayerlite.base.BaseFragment
import com.example.musicplayerlite.databinding.FragmentOnboardingSlideBinding

class OnboardingSlideFragment : BaseFragment<FragmentOnboardingSlideBinding>() {
    @StringRes
    private var titleResId: Int = 0

    @StringRes
    private var descriptionResId: Int = 0

    @DrawableRes
    private var imageResId: Int = 0
    private val position: Int by lazy {
        arguments?.getInt(ARG_POSITION_SCREEN) ?: 0
    }

    override fun getViewBinding(): FragmentOnboardingSlideBinding {
        return FragmentOnboardingSlideBinding.inflate(layoutInflater)
    }

    override fun initData() {
        when(position) {
            PAGE_0 -> {
                imageResId = R.drawable.img_ob_1
                titleResId = R.string.my_music
                descriptionResId = R.string.my_music
            }
            PAGE_1 -> {
                imageResId = R.drawable.img_ob_2
                titleResId = R.string.my_music
                descriptionResId = R.string.my_music
            }
            PAGE_2 -> {
                imageResId = R.drawable.img_ob_3
                titleResId = R.string.my_music
                descriptionResId = R.string.my_music
            }
        }
    }

    override fun initView() {
        with(binding) {
            tvTitle.text = getString(titleResId)
            tvDescription.text = getString(descriptionResId)
            imvOnboarding.setImageResource(imageResId)
        }
    }

    override fun initViewListener() {
        /* no-op */
    }

    override fun initObserver() {
        /* no-op */
    }

    companion object {
        const val ARG_POSITION_SCREEN = "ARG_POSITION_SCREEN"
        const val PAGE_0 = 0
        const val PAGE_1 = 1
        const val PAGE_2 = 2
        fun newInstance(bundle: Bundle): OnboardingSlideFragment {
            return OnboardingSlideFragment().apply {
                arguments = bundle
            }
        }
    }
}