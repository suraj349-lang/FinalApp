import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.finalapp.R
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.ui.theme.floatingActionBtnColor

sealed class Destinations(
    val route: String,
    val icon: Int,
    val name:String
) {
    object HomeScreen : Destinations(
        route = SCREENS.HOME.route,
        icon = R.drawable.home_new,
        name = "Home"
    )
    object Trending : Destinations(
        route = SCREENS.SEARCH.route,
        icon = R.drawable.trending,
        name = "Trending"
    )
    object CreateEvent : Destinations(
        route = "",
        icon = R.drawable.create_event_new,
        name = "Create Event"
    )
    object ProfileScreen : Destinations(
        route = SCREENS.PROFILE.route,
        icon = R.drawable.profile_new_empty,
        name = "Profile"
    )

    object Settings : Destinations(
        route = SCREENS.SETTINGS.route,
        icon = R.drawable.settings_empty,
        name = "Settings"
    )


}

@Composable
fun BottomBar(
    navController: NavHostController, state: MutableState<Boolean>, modifier: Modifier = Modifier, onCreateEventClick: () -> Unit={}) {
    val screens = listOf(
        Destinations.HomeScreen, Destinations.Trending,Destinations.CreateEvent,Destinations.ProfileScreen,Destinations.Settings
    )
//0xFFE4E4F1   0xFFF9F9FF -> screen color
    NavigationBar(containerColor = Color.White, modifier = Modifier.shadow(elevation = 40.dp).height(48.dp).fillMaxWidth()){
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        screens.forEach { screen ->
            val active=currentRoute==screen.route
            NavigationBarItem(
                selected = active,
                icon = {
                    Column( verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        if(active){
                            Image(painter = painterResource(id = screen.icon), contentDescription ="",modifier=Modifier.size(24.dp), colorFilter = ColorFilter.tint( color =  floatingActionBtnColor ) )
                            Text(text = screen.name.uppercase(), fontSize = 11.sp, style = MaterialTheme.typography.labelSmall, color =  floatingActionBtnColor, fontWeight = FontWeight.Bold  )
                        }else{
                            Image(painter = painterResource(id = screen.icon), contentDescription ="",modifier=Modifier.size(24.dp), colorFilter = ColorFilter.tint( color =  Color.Gray) )
                            Text(text = screen.name, fontSize = 8.sp, style = MaterialTheme.typography.labelSmall, color =  Color.Gray)
                        }
                    }

                },
                onClick = {
                    if (screen == Destinations.CreateEvent) {
                        onCreateEventClick()
                    } else {
                        if (currentRoute != screen.route) {
                            navController.navigate(screen.route)
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }

}