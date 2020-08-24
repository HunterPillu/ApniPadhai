package com.meripadhai.ui.onboardingscreen.feature.onboarding.customView

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.viewpager2.widget.ViewPager2
import com.meripadhai.R
import com.meripadhai.ui.activity.LoginActivity
import com.meripadhai.ui.activity.MainActivity
import com.meripadhai.ui.onboardingscreen.feature.onboarding.OnBoardingPagerAdapter
import com.meripadhai.ui.onboardingscreen.feature.onboarding.entity.OnBoardingPage
import com.google.firebase.auth.FirebaseAuth
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import kotlinx.android.synthetic.main.onboarding_view.view.*
import setParallaxTransformation

class OnBoardingView @JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) :
    FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val numberOfPages by lazy { OnBoardingPage.values().size }

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.onboarding_view, this, true)
        setUpSlider(view)
        addingButtonsClickListeners()
    }

    private fun setUpSlider(view: View) {
        with(slider) {
            adapter = OnBoardingPagerAdapter()
            setPageTransformer { page, position ->
                setParallaxTransformation(page, position)
            }

            addSlideChangeListener()

            val wormDotsIndicator = view.findViewById<WormDotsIndicator>(R.id.page_indicator)
            wormDotsIndicator.setViewPager2(this)
        }
    }


    private fun addSlideChangeListener() {

        slider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                if (numberOfPages > 1) {
                    val newProgress = (position + positionOffset) / (numberOfPages - 1)
                    onboardingRoot.progress = newProgress
                }
            }
        })
    }

    private fun addingButtonsClickListeners() {
        nextBtn.setOnClickListener { navigateToNextSlide() }
        skipBtn.setOnClickListener { openNextActivity() }
        startBtn.setOnClickListener { openNextActivity() }
    }

    private fun openNextActivity() {
        val intent: Intent
        if (null != FirebaseAuth.getInstance().currentUser) {
            intent = Intent(context, MainActivity::class.java)
        } else {
            intent = Intent(context, LoginActivity::class.java)
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }

    private fun navigateToNextSlide() {
        val nextSlidePos: Int = slider?.currentItem?.plus(1) ?: 0
        slider?.setCurrentItem(nextSlidePos, true)
    }

    private fun navigateToMainACtivity() {
    }
}