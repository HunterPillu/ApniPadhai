package com.meripadhai.ui.onboardingscreen.feature.onboarding.entity

import androidx.annotation.RawRes
import androidx.annotation.StringRes
import com.meripadhai.R

enum class OnBoardingPage(
    @StringRes val titleResource: Int,
    @StringRes val subTitleResource: Int,
    @StringRes val descriptionResource: Int,
    @RawRes val logoResource: Int
) {

    ONE(
        R.string.welcome,
        R.string.intro_text_1,
        R.string.intro_text_11,
        R.raw.welcome_screen
    ),
    TWO(
        R.string.intro_text_22,
        R.string.intro_text_2,
        R.string.intro_text_4,
        R.raw.back_to_school
    ),
    THREE(
        R.string.intro_text_33,
        R.string.intro_text_3,
        R.string.intro_text_44,
        R.raw.learning_future
    )/*,
    FOUR(
    R.string.intro_text_44,
    R.string.intro_text_4,
    R.string.empty,
    R.drawable.ic_a_day_at_the_park
    )*/

}