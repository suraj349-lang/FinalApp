package com.example.finalapp.testing

import com.example.finalapp.R
import com.example.finalapp.model.NavigationItem
import com.example.finalapp.navigation.SCREENS

val items = listOf(
    NavigationItem(
        title = "Home",
        selectedIcon = R.drawable.home,
        unselectedIcon = R.drawable.home,
        route= SCREENS.SIGNUP.route
    ),
    NavigationItem(
        title = "Cinema",
        selectedIcon = R.drawable.cinema,
        unselectedIcon = R.drawable.cinema,
        route= SCREENS.SIGNUP.route
    ),
    NavigationItem(
        title = "Coffee",
        selectedIcon = R.drawable.coffeecup,
        unselectedIcon = R.drawable.coffeecup,
        badgeCount = 45,
        route= SCREENS.LOGIN.route
    ),
    NavigationItem(
        title = "Travel",
        selectedIcon = R.drawable.airplane,
        unselectedIcon = R.drawable.airplane,
        route= SCREENS.SPLASH.route
    ),
    NavigationItem(
        title = "Date",
        selectedIcon = R.drawable.dinner,
        unselectedIcon = R.drawable.dinner,
        route= SCREENS.SPLASH.route
    ),
    NavigationItem(
        title = "Sports",
        selectedIcon = R.drawable.sports,
        unselectedIcon = R.drawable.sports,
        route= SCREENS.SPLASH.route
    ),
    NavigationItem(
        title = "Clubs",
        selectedIcon = R.drawable.nightclubnew,
        unselectedIcon = R.drawable.nightclubnew,
        route= SCREENS.SPLASH.route
    ),
    NavigationItem(
        title = "Personal",
        selectedIcon = R.drawable.personal,
        unselectedIcon = R.drawable.personal,
        route= SCREENS.SPLASH.route
    ),
)