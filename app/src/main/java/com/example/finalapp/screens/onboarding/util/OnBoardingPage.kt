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
        image = R.drawable.directchat,
        title = "Direct Chat",
        description = "Send a direct message to the person you saw and liked."
    )

    object Second : OnBoardingPage(
        image = R.drawable.offer,
        title = "Private Event",
        description = "Create an event to the person on your feed."
    )

    object Third : OnBoardingPage(
        image = R.drawable.offerforeverybody,
        title = "Public Event",
        description = "Create an event to everyone in your nearby."
    )
    object Fourth : OnBoardingPage(
        image = R.drawable.nightclub,
        title = "Drop profile",
        description = "Drop your profile to match later"
    )
}
