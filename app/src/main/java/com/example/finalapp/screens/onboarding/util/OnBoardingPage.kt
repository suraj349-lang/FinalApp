package com.example.finalapp.screens.onboarding.util

import androidx.annotation.DrawableRes
import com.example.finalapp.R


sealed class OnBoardingPage(
    @DrawableRes
    val image: Int,
    val title: String,
    val description: String
) {
    object First : OnBoardingPage(
        image = R.drawable.offer,
        title = "Flash Posts",
        description = "Create an flash post for any request or offer."
    )
    object Second : OnBoardingPage(
        image = R.drawable.direct_chat,
        title = "Direct Chat",
        description = "Send a direct message to the person you saw and liked."
    )
    object Third : OnBoardingPage(
        image = R.drawable.event,
        title = "Public Event",
        description = "Create an event to everyone in your nearby."
    )
    object Fourth : OnBoardingPage(
        image = R.drawable.drop_profile_new,
        title = "Drop profile",
        description = "Drop your profile to match later."
    )
}
