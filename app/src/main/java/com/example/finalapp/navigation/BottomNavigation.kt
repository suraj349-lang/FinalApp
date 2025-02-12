import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.finalapp.R
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.ui.theme.DarkBlue
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.ui.theme.statusBarColor
import com.example.finalapp.ui.theme.statusBarColorBlue
import com.example.finalapp.ui.theme.topAppBarTextColor

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
    object SearchProfileScreen : Destinations(
        route = SCREENS.SEARCH.route,
        icon = R.drawable.search_new,
        name = "Search"
    )
    object CreateEvent : Destinations(
        route = SCREENS.CREATE_EVENT.route,
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
    navController: NavHostController, state: MutableState<Boolean>, modifier: Modifier = Modifier
) {
    val screens = listOf(
        Destinations.HomeScreen, Destinations.SearchProfileScreen,Destinations.CreateEvent,Destinations.ProfileScreen,Destinations.Settings
    )
//0xFFE4E4F1   0xFFF9F9FF -> screen color
    NavigationBar(containerColor = Color(0xFFF9F9FF)) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        screens.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                icon = {
                    Column( verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(painter = painterResource(id = screen.icon), contentDescription ="",modifier=Modifier.size(24.dp), colorFilter = ColorFilter.tint( color = if(currentRoute == screen.route) Color.Black  else Color.Gray) )
                        Text(text = screen.name, fontSize = 8.sp, style = MaterialTheme.typography.labelSmall, color = if(currentRoute == screen.route) Color.Black  else Color.Gray)
                    }

                },
                onClick = {
                    if(currentRoute!=screen.route) {
                        navController.navigate(screen.route)
                    }
//                    {
//                        popUpTo(navController.graph.findStartDestination().id) {
//                            saveState = true
//                        }
//                        launchSingleTop = true
//                        restoreState = true
//                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }

}