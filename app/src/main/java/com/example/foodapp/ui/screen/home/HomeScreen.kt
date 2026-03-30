import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.foodapp.core.UserRoutes
import com.example.foodapp.core.bottomRouteFromIndex
import com.example.foodapp.domain.model.ProfileCompleteness
import com.example.foodapp.presentation.viewmodel.UserProfileViewModel
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
    parentNavController: NavHostController, userProfile: UserProfileViewModel = hiltViewModel()
) {

    val homeNavController = rememberNavController()
    val profileState by userProfile.uiState.collectAsStateWithLifecycle()

    val navBackStackEntry by homeNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Sử dụng derivedStateOf để đảm bảo selectedIndex luôn phản ánh đúng trạng thái NavController
    val selectedIndex = remember(currentDestination) {
        val route = currentDestination?.route
        // Chuyển đổi Sequence sang List để tránh lỗi 'any' unresolved reference
        val hierarchy = currentDestination?.hierarchy?.map { it.route }?.toList() ?: emptyList()

        //  check các tab con trong HomeNavGraph,
        // các root khác đang được lồng bên trong HOME_ROOT.
        val index = when {
            route == UserRoutes.CHAT || hierarchy.any { it == UserRoutes.CHAT_ROOT } -> 1
            route == UserRoutes.CART || hierarchy.any { it == UserRoutes.CART_ROOT } -> 2
            route == UserRoutes.PROFILE || hierarchy.any { it == UserRoutes.PROFILE_ROOT } -> 3
            route == UserRoutes.HOME || hierarchy.any { it == UserRoutes.HOME_ROOT } -> 0
            else -> 0
        }
        Log.d("NAV_DEBUG", "Calculated SelectedIndex: $index for route: $route")
        index
    }



    LaunchedEffect(homeNavController) {

        homeNavController.currentBackStackEntryFlow.collect { entry ->
            try {
                val routes =
                    homeNavController.currentBackStack.value.mapNotNull { it.destination.route }
                Log.d("NavigationLog", "Home BackStack: ${routes.joinToString(" -> ")}")
            } catch (e: Exception) {
                Log.d("NavigationLog", "Current route: ${entry.destination.route}")
            }
        }
    }


    //logic valid bottom bar for navigation
    val isShowBottomBar = remember(currentDestination) {
        currentDestination?.hierarchy?.any {
            it.route in setOf(
                UserRoutes.HOME,
                UserRoutes.CHAT,
                UserRoutes.CART,
                UserRoutes.PROFILE,
                UserRoutes.HOME_ROOT,
                UserRoutes.CHAT_ROOT,
                UserRoutes.CART_ROOT,
                UserRoutes.PROFILE_ROOT
            )
        } == true
    }

    // Ưu tiên sử dụng logic linh hoạt và ổn định hơn cho việc hiển thị BottomBar
    val isShowBottomBar2 = isShowBottomBar && when (currentDestination?.route) {
        // Chỉ hiển thị ở các màn hình tab chính, không hiển thị ở màn hình chi tiết
        UserRoutes.HOME, UserRoutes.CHAT, UserRoutes.CART, UserRoutes.PROFILE -> true

        else -> false
    }


    Scaffold(
        bottomBar = {
            Log.d("NAV_DEBUG", "BottomBar visible: $isShowBottomBar2, index: $selectedIndex")
            if (isShowBottomBar2) {
                HomeBottomBar(
                    selectedIndex = selectedIndex,
                    badgeProfile = profileState.profileCompleteness == ProfileCompleteness.INCOMPLETE,
                    onItemSelected = { index ->
                        val route = bottomRouteFromIndex(index)

                        // Sử dụng logic điều hướng chuẩn cho Bottom Bar
                        homeNavController.navigate(route) {
                            popUpTo(homeNavController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    })
            }
        }

    ) { paddingValues ->

        //apply pading
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding()
        ) {
            HomeNavGraph(
                navController = homeNavController, parentNavController = parentNavController
            )
        }
    }
}

