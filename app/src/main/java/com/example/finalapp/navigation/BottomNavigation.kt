import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.example.finalapp.R
import com.example.finalapp.navigation.SCREENS

sealed class Destinations(
    val route: String,
    val activeIcon: Int,
    val inactiveIcon:Int,
    val name:String
) {
    object HomeScreen : Destinations(
        route = SCREENS.HOME.route,
        activeIcon = R.drawable.home_colored,
        inactiveIcon=R.drawable.home_new,
        name = "Home"
    )
    object Pings : Destinations(
        route = SCREENS.PINGS.route,
        activeIcon = R.drawable.ping,
        inactiveIcon=R.drawable.ping,
        name = "Pings"
    )
    object CreateEvent : Destinations(
        route = "",
        activeIcon = R.drawable.create_colored,
        inactiveIcon=R.drawable.create_event_new,
        name = "Create"
    )
    object ProfileScreen : Destinations(
        route = SCREENS.PROFILE.route,
        activeIcon = R.drawable.profile_colored,
        inactiveIcon=R.drawable.profile_new_empty,
        name = "Profile"
    )

    object Settings : Destinations(
        route = SCREENS.SETTINGS.route,
        activeIcon = R.drawable.settings_colored,
        inactiveIcon=R.drawable.settings_empty,
        name = "Settings"
    )


}

@Composable
fun BottomBar(
    navController: NavHostController, state: MutableState<Boolean>, modifier: Modifier = Modifier, containerColor:Color= Color.DarkGray, highlightedTextColor:Color=Color.White, inactiveIconColor: Color= Color.White, inactiveTextColor:Color= Color.LightGray,onCreateEventClick: () -> Unit={}) {
    val screens = listOf(
        Destinations.HomeScreen, Destinations.Pings,Destinations.CreateEvent,Destinations.ProfileScreen,Destinations.Settings
    )
//0xFFE4E4F1   0xFFF9F9FF -> screen color
    NavigationBar(containerColor = containerColor, modifier = Modifier.shadow(elevation = 40.dp).height(50.dp).fillMaxWidth()){
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        screens.forEach { screen ->
            val active=currentRoute==screen.route
            NavigationBarItem(
                selected = active,
                icon = {
                    Column( verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

                        if(active){
                            AsyncImage(model = screen.activeIcon, contentDescription ="",modifier=Modifier.size(24.dp) )
                            Text(text = screen.name, fontSize = 11.sp, style = TextStyle(textDecoration= TextDecoration.Underline ) , color =  highlightedTextColor, fontWeight = FontWeight.Bold  )
                        }else{
                            Image(painter = painterResource(id = screen.inactiveIcon), contentDescription ="",modifier=Modifier.size(20.dp), colorFilter = ColorFilter.tint(inactiveIconColor) )

                            Text(text = screen.name, fontSize = 11.sp, style = MaterialTheme.typography.labelSmall, color = inactiveTextColor)
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