import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.finalapp.R
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.ui.theme.DarkBlue
import com.example.finalapp.ui.theme.topAppBarTextColor

sealed class Destinations(
    val route: String,
    val icon: Int,
    val name:String
) {
    object HomeScreen : Destinations(
        route = SCREENS.HOME.route,
        icon = R.drawable.home,
        name = "Home"
    )
    object SearchProfileScreen : Destinations(
        route = SCREENS.SEARCH.route,
        icon = R.drawable.search,
        name = "Search"
    )
    object ProfileScreen : Destinations(
        route = SCREENS.PROFILE.route,
        icon = R.drawable.baseline_person_24,
        name = "Profile"
    )
    object CreateEvent : Destinations(
        route = SCREENS.CREATE_EVENT.route,
        icon = R.drawable.create_event,
        name = "Create Event"
    )


}

@Composable
fun BottomBar(
    navController: NavHostController, state: MutableState<Boolean>, modifier: Modifier = Modifier
) {
    val screens = listOf(
        Destinations.HomeScreen, Destinations.SearchProfileScreen,Destinations.CreateEvent,Destinations.ProfileScreen
    )

    NavigationBar(
        modifier = modifier
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        screens.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                icon = {
                    Column( verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(painter = painterResource(id = screen.icon), contentDescription ="",modifier=Modifier.size(24.dp) )
                        Text(text = screen.name, fontSize = 8.sp, style = MaterialTheme.typography.labelSmall, color = Color.DarkGray)
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
                    selectedIconColor = Color.Red,
                    unselectedIconColor = DarkBlue, indicatorColor = topAppBarTextColor
                )
            )
        }
    }

}