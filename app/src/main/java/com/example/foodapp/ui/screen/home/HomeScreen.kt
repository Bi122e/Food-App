
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.foodapp.core.UserRoutes
import com.example.foodapp.core.bottomRouteFromIndex
import com.example.foodapp.ui.components.HomeBottomBar
import com.example.foodapp.ui.screen.home.HomeNavGraph


//@Composable
//fun HomeScreen() {
//    val navController = rememberNavController()
//    var selectedIndex by remember { mutableStateOf(0) }
//
//
//    Scaffold(
//        bottomBar = {
//            HomeBottomBar(
//                selectedIndex = selectedIndex,
//                onItemSelected = { index ->
//                    selectedIndex = index
//                    navController.navigate(bottomRouteFromIndex(index)) {
//                        launchSingleTop = true
//                        restoreState = true
//                        popUpTo(UserRoutes.HOME) { saveState = true }
//                    }
//                }
//            )
//        }
//    ) { paddingValue ->
//        HomeNavGraph(
//            navController = navController,
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValue),
//
//        )
//    }
//}
@SuppressLint("RestrictedApi")
@Composable
fun HomeScreen(
    parentNavController: NavHostController
) {

    val homeNavController = rememberNavController()

    val navBackStackEntry by homeNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val selectedIndex = when {
        //get destination
        currentDestination?.hierarchy?.any { it.route == UserRoutes.HOME_ROOT || it.route == UserRoutes.HOME } == true -> 0
        currentDestination?.hierarchy?.any { it.route == UserRoutes.CHAT_ROOT || it.route == UserRoutes.CHAT } == true -> 1
        currentDestination?.hierarchy?.any { it.route == UserRoutes.CART_ROOT || it.route == UserRoutes.CART } == true -> 2
        currentDestination?.hierarchy?.any { it.route == UserRoutes.PROFILE_ROOT || it.route == UserRoutes.PROFILE } == true -> 3
        else -> 0
    }


    LaunchedEffect(homeNavController) {

        homeNavController.currentBackStackEntryFlow.collect { entry ->
            try {
                val routes = homeNavController.currentBackStack.value.mapNotNull { it.destination.route }
                Log.d("NavigationLog", "Home BackStack: ${routes.joinToString(" -> ")}")
            } catch (e: Exception) {
                Log.d("NavigationLog",  "Current route: ${entry.destination.route}")
            }
        }
    }


    //logic valid bottom bar for navigation
    val isShowBottomBar = navBackStackEntry?.destination?.hierarchy?.any {
        it.route in setOf(
            UserRoutes.HOME_ROOT,
            UserRoutes.CHAT_ROOT,
            UserRoutes.CART_ROOT,
            UserRoutes.PROFILE_ROOT
        )
    } == true

    val isShowBottomBar2 = when (currentDestination?.route) {
        UserRoutes.HOME,
        UserRoutes.CHAT,
        UserRoutes.CART,
        UserRoutes.PROFILE -> true
        else -> false
    }


    Scaffold(
        bottomBar = {
            if (isShowBottomBar2) {
                HomeBottomBar(
                    selectedIndex = selectedIndex, //-> current idx nav
                    onItemSelected = { index -> // -> item was on click in list
                        val route = bottomRouteFromIndex(index) // -> map idx to route
                        if (selectedIndex == index) { //-> flow pop loop current click vd: home home - pop 1 home
                            homeNavController.popBackStack(route, inclusive = false)

                        } else {
                            homeNavController.navigate(route) {
                                popUpTo(homeNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }

    ) { paddingValues ->

        //apply pading
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            HomeNavGraph(
                navController = homeNavController,
                parentNavController = parentNavController
            )
        }
    }
}

