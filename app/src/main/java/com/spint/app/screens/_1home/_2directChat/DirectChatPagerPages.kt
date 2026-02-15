package com.spint.app.screens._1home._2directChat

import androidx.annotation.DrawableRes
import com.spint.app.R

sealed class DirectChatPagerPages(
    @DrawableRes
    val image: Int,
    val title: String,
    val description: String
) {
    object First : DirectChatPagerPages(
        image = R.drawable.direct_chat,
        title = "Connect to proximity people.",
        description = "Click connect to share your profile."
    )

    object Second : DirectChatPagerPages(
        image = R.drawable.mutual_acceptance,
        title = "Mutual opt-in.",
        description = "Chat once you accept the request."
    )

    object Third : DirectChatPagerPages(
        image = R.drawable.ephemeral,
        title = "Ephemeral Chat.",
        description = "Chat will expire after 24hrs."
    )
    object Fourth : DirectChatPagerPages(
        image = R.drawable.privacy_new,
        title = "Privacy first.",
        description = "Your privacy is protected."
    )
}