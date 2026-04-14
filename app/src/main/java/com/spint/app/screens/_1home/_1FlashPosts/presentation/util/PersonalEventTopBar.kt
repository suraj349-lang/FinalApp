package com.spint.app.screens._1home._1FlashPosts.presentation.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spint.app.R
import com.spint.app.ui.theme.floatingActionBtnColor
import com.spint.app.utils.constants.Constants
@Composable
fun PingsTopBar(
    backgroundColor: Color,
    showIcon: Boolean,
    onBackClicked: () -> Unit = {},
    onSearchIconClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .statusBarsPadding()
            .height(40.dp).shadow(elevation = 10.dp, spotColor = Color.White, ambientColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back Icon
            Image(
                painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                contentDescription = "Back",
                colorFilter = ColorFilter.tint(Constants.HOME_TOP_BAR_ICON_COLOR),
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onBackClicked() }
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Title
            Text(
                text = "Pings",
                fontSize = 20.sp,
                color = Constants.HOME_TOP_BAR_ICON_COLOR,
                fontFamily = Constants.FONT_MEDIUM
            )

            Spacer(modifier = Modifier.weight(1f))

            // Optional Search Icon
            if (!showIcon) {
                Image(
                    painter = painterResource(id = R.drawable.search_new_filled),
                    contentDescription = "Search",
                    colorFilter = ColorFilter.tint(floatingActionBtnColor),
                    modifier = Modifier.padding(end = 10.dp)
                        .size(20.dp)
                        .clickable { onSearchIconClicked() }
                )
            }
        }
    }
}
