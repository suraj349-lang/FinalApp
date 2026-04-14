package com.spint.app.screens._1home._1FlashPosts.others.pingsItem


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.spint.app.R
import com.spint.app.model.flashPost.FlashPostResponse
import com.spint.app.ui.imagePrefix
import com.spint.app.ui.theme.floatingActionBtnColor
import com.spint.app.utils.constants.Constants
import kotlinx.coroutines.delay


@Composable
fun PingsItemUI(item: FlashPostResponse, onShareClicked: () -> Unit, onRespondClicked: () -> Unit) {

    Card(
        modifier = Modifier
            .padding(4.dp)
            .wrapContentHeight()
            .fillMaxWidth(),
        elevation=CardDefaults.cardElevation(60.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161515)),
//        border = BorderStroke(width = 1.dp, color = Color.DarkGray)
    ) {
        Column {

            if(item.user !=null && item.user.userName.isNotEmpty() && item.user.profileImage?.isNotEmpty() == true){
                UserImageNameTime2(item.user.userName,item.user.profileImage ?: "",item.expirationTime)
            }
            Column(
                modifier = Modifier
                    .wrapContentSize(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item.title?.let { TypeLocationDate2(it) }
              //  StatusBadge(status = "Active", urgency = "Expiring soon")
                EventImage2(item.image)
                item.category?.let { CaptionHeader(it) }
              //  CountdownTimer(eventTimeMillis = System.currentTimeMillis() + 3600000L) // 1 hour from now
                ShareLikeRespond(item.totalUpVotes,{onShareClicked()},onRespondClicked = {onRespondClicked()})
                Divider(Modifier.fillMaxWidth(), thickness = 1.dp, color = Color.LightGray)
            }
        }
    }
}

@Composable
fun UserImageNameTime2(username:String,image:String,time:String) {
    Row(
        modifier = Modifier
            //  .background(color = floatingActionBtnColor)
            .fillMaxWidth()
            .height(60.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Card(modifier = Modifier.size(40.dp), shape = CircleShape) {
                AsyncImage(
                    model= imagePrefix+image,
                    contentDescription = null,
                    modifier=Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(username, fontFamily = Constants.FONT_MEDIUM, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
        Text(time, fontFamily = Constants.FONT_MEDIUM, fontSize = 14.sp, color = Color.White)
    }
}



@Composable
fun TypeLocationDate2(title:String,eventTimeMillis: Long = System.currentTimeMillis() + 3600000L) {
    val timeRemaining = remember(eventTimeMillis) {
        eventTimeMillis - System.currentTimeMillis()
    }
    val hours = (timeRemaining / (1000 * 60 * 60)).toInt()
    val minutes = ((timeRemaining / (1000 * 60)) % 60).toInt()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        InfoChipForType(text = title)
        InfoChip(iconRes = R.drawable.location_new, text = "Delhi" )
        InfoChip(
            iconRes = R.drawable.clock_filled,
            text = "In ${hours}h ${minutes}m",
        )
    }
}

@Composable
fun InfoChipForType(text: String) {
    Row(
        modifier = Modifier
            .background(Color(0xFF061CA8), shape = RoundedCornerShape(6.dp)),
            verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            color = Color.White,
            fontWeight=FontWeight.Bold,
            modifier=Modifier.padding(4.dp),
            fontFamily = Constants.FONT_MEDIUM
        )
    }
}
@Composable
fun InfoChip(iconRes: Int? = null, text: String) {
    Row(
        modifier = Modifier
//            .background(Color.Gray, shape = RoundedCornerShape(6.dp))
//            .padding(horizontal = 10.dp, vertical = 6.dp),
       , verticalAlignment = Alignment.CenterVertically
    ) {
        if(iconRes !=null) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp)
                    .padding(end = 4.dp)
            )
        }
        Text(
            text = text,
            fontSize = 14.sp,
            color = Color.White,
            fontFamily = Constants.FONT_MEDIUM
        )
    }
}
@Composable
fun StatusBadge(status: String?, urgency: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        status?.let {
            Box(
                modifier = Modifier
                    .background(Color.Yellow, shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = it,
                    fontSize = 12.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        Text(
            text = urgency,
            color = if (status == "Active") Color.Green else Color.Red,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

    }
}

@Composable
fun CaptionHeader( caption: String?) {
    var active by remember { mutableStateOf(false) }
    Card(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .wrapContentSize()
            ) {

                caption?.let {
                    Text(
                        text = it,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = Constants.FONT_EXTRA_LIGHT,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White
                    )
                }
            }
            Image(
                painter = painterResource(id = if(active)R.drawable.baseline_expand_less_24 else R.drawable.baseline_expand_more_24 ),
                contentDescription ="",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { active = !active },
                colorFilter = ColorFilter.tint(Color.White)
            )
        }
    }

}


@Composable
fun CountdownTimer(eventTimeMillis: Long) {
    var timeLeft by remember { mutableStateOf(eventTimeMillis - System.currentTimeMillis()) }

    LaunchedEffect(eventTimeMillis) {
        while (timeLeft > 0) {
            delay(1000)
            timeLeft = eventTimeMillis - System.currentTimeMillis()
        }
    }

    val hours = (timeLeft / (1000 * 60 * 60)).toInt()
    val minutes = ((timeLeft / (1000 * 60)) % 60).toInt()
    val seconds = ((timeLeft / 1000) % 60).toInt()

    if (timeLeft > 0) {
        Text(
            text = String.format("\u23F3 %02dh %02dm %02ds left", hours, minutes, seconds),
            fontSize = 14.sp,
            color = Color.Red,
            fontFamily = Constants.FONT_MEDIUM
        )
    } else {
        Text(text = "\uD83C\uDF89 Event Started!", fontSize = 14.sp, color = Color.Green)
    }
}

@Composable
fun ResponderAvatars(responderImageIds: List<Int>) {
    Box(modifier = Modifier.height(40.dp)) {
        responderImageIds.forEachIndexed { index, imageId ->
            Image(
                painter = painterResource(id = imageId),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .offset(x = (index * 24).dp) // 24dp instead of 40dp to create overlap
                    .clip(CircleShape)
                    .zIndex(index.toFloat()), // Ensure correct layering
                contentScale = ContentScale.Crop
            )
        }
    }
}




@Composable
fun ShareLikeRespond(totalViews:Int?,onShareClicked:()->Unit,onRespondClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
//        ResponderAvatars(
//            responderImageIds = listOf(
//                R.drawable.profile_image_1,
//                R.drawable.profile_image_2,
//                R.drawable.profile_image_3
//            )
//        )
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(top=8.dp), verticalAlignment = Alignment.CenterVertically) {

            IconWithLabel(iconRes = R.drawable.people, label = totalViews.toString(),isShareButton = false)
            IconWithLabel(R.drawable.share, "Share",isShareButton = true,onShareClicked={onShareClicked()})
        }
        RespondButton {onRespondClicked()}
       // AnimatedRespondButton(onClick = onRespondClicked)
    }
}


@Composable
fun IconWithLabel(iconRes: Int, label: String,isShareButton:Boolean,onShareClicked: () -> Unit={}) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable {
        if(isShareButton){
            onShareClicked()
        }
    }) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = "",
            modifier = Modifier.size(24.dp),
            colorFilter = ColorFilter.tint(Color.LightGray)
        )
        Text(text = label, fontSize = 12.sp, fontFamily = Constants.DONGLE_NORMAL, color = Color.LightGray)
    }
}



@Composable
fun RespondButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(top = 8.dp)
            .height(40.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = floatingActionBtnColor)
    ) {
        Text(
            text = "RESPOND",
            fontFamily = Constants.FONT_MEDIUM,
            fontSize = 14.sp,
            color = Color.White
        )
    }
}
@Composable
fun EventImage2(image: String) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .height(400.dp), shape = RoundedCornerShape(0.dp),colors = CardDefaults.cardColors(containerColor = Color.Black)) {
        AsyncImage(model = imagePrefix + image, contentDescription ="", modifier = Modifier
            .fillMaxSize(), contentScale = ContentScale.Crop
        )
    }
}
