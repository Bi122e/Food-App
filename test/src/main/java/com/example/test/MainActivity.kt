package com.example.test

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val auth: AuthViewModel = viewModel()
            val appState by auth.appState.collectAsStateWithLifecycle()

            var startDestination by remember {  mutableStateOf("splash") }
            if (appState is AppState.Guest) startDestination = "login"
            if (appState is AppState.LoggedIn) {
                startDestination = when ((appState as AppState.LoggedIn).user.role) {
                    UserRole.ADMIN -> Route.ADMIN
                    UserRole.CUSTOMER -> Route.USER
                    UserRole.RESTAURANT -> Route.RESTAURANT
                    UserRole.DRIVER -> Route.DRIVER
                }
            }
            AppNavGraph(startDestination, auth)
        }
    }
}

@Composable
fun AppNavGraph(startDestination: String, auth: AuthViewModel) {
    val navController = rememberNavController()
    val state by auth.appState.collectAsStateWithLifecycle()
    LaunchedEffect(state) {
        when (state) {
            is AppState.Loading -> {
                auth.finishLoading()

                Log.d("TESTSTATE", "LOADING $state")

            }

            is AppState.Guest -> {
                navController.navigate(Route.LOGIN) {
                    popUpTo(route = Route.LOADING) { inclusive = true }
                }
                Log.d("TESTSTATE", "GUEST $state")
            }

            is AppState.LoggedIn -> {
                val role = when ((state as AppState.LoggedIn).user.role) {
                    UserRole.CUSTOMER -> {
                        Route.USER
                    }

                    UserRole.DRIVER -> {
                        Route.DRIVER                    }

                    UserRole.RESTAURANT -> {
                        Route.RESTAURANT                    }

                    UserRole.ADMIN -> {
                        Route.ADMIN                    }
                }
                navController.navigate(role) {
                    popUpTo(Route.LOGIN) { inclusive = true }
                }
                Log.d("TESTSTATE", "$state - $role")
            }
        }
    }
        NavHost(navController = navController, route = "empty", startDestination = Route.LOADING) {
            composable(route = Route.LOADING) {
                SplashScreen {  }
            }
            composable(route = Route.LOGIN) {
                LoginScreen(
                    onClick = { email, role ->
                        auth.login(email, role)
                    },
                    onGuestClick = { auth.goGuest() })            }
            composable(Route.USER) {
                RoleRootScreen("userRoot") { auth.logOut() }

            }
            composable(Route.RESTAURANT) {
                RoleRootScreen("restaurantRoot") { auth.logOut() }
            }
            composable(Route.ADMIN) {
                RoleRootScreen("adminRoot") { auth.logOut() }
            }
            composable(Route.DRIVER) {
                RoleRootScreen("driverRoot") { auth.logOut() }
            }

        }
//        }
//        composable("userRoot") { RoleRootScreen("Customer") { auth.logOut() } }
//        composable("restaurantRoot") { RoleRootScreen("Restaurant") { auth.logOut() } }
//        composable("driverRoot") { RoleRootScreen("Driver") { auth.logOut() } }
//        composable("adminRoot") { RoleRootScreen("Admin") { auth.logOut() } }
    }

@Composable
fun RoleRootScreen(roleName: String, onLogout: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to $roleName Root")

        Button(onClick = onLogout) {
            Text("Logout")
        }

        val items = listOf(
            BottomNavItem(
                "home",
                Icons.Outlined.Home,
                badgeCount = 1,
                unselectedIcon = Icons.Default.Home),
            BottomNavItem(
                "Chat",
                Icons.Outlined.Email,
                badgeCount = 1,
                unselectedIcon = Icons.Default.Email),
            BottomNavItem(
                "home",
                Icons.Outlined.Settings,
                badgeCount = 1,
                unselectedIcon = Icons.Default.Settings)
        )
        Scaffold(
            bottomBar = {
                var selectedIdx by rememberSaveable { mutableStateOf(0) }
                NavigationBar {
                    items.forEachIndexed {index, item ->
                        NavigationBarItem(
                            selected =  selectedIdx == index,
                            onClick = {selectedIdx = index},
                            icon = {
                                Icon(imageVector = if (selectedIdx == index) {
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                    contentDescription = null)
                            },
                            label =  {Text(item.title)}

                        )
                    }
                }
            }
        ) { paddingValues ->

            LazyColumn(
                contentPadding = paddingValues,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(50.dp)) {
                var count = 0
                items(20) { index ->
                    Text(" $index")

                }
            }
        }
    }
}
@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(1000) // fake splash delay
        onTimeout()
    }
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Text("Splash Screen")
    }
}
@Composable
fun LoginScreen(
    onClick: (String, UserRole) -> Unit, onGuestClick: () -> Unit)
{
    Column {
        Button(onClick = { onClick("customer@example.com", UserRole.CUSTOMER) }) {
            Text("Login as Customer")
        }
        Button(onClick = { onClick("restaurant@example.com", UserRole.RESTAURANT) }) {
            Text("Login as Restaurant")
        }
        Button(onClick = { onGuestClick() }) {
            Text("Continue as Guest")
        }
    }

}

enum class UserRole(val displayName: String) {
    CUSTOMER("KHACH HANG"),
    RESTAURANT("NHA HANG"),
    DRIVER("TAI XE"),
    ADMIN("ADMIN"),
}

data class User(
    val uid: String = "",
    val email: String = "",
    val role: UserRole = UserRole.CUSTOMER,
)

data class Profile(
    val uid: String = "",
    val name: String = "",
    val address: String = "",
)

sealed class AppState {
    object Loading: AppState()
    object Guest: AppState()
    data class LoggedIn(val user: User): AppState()
}

class AuthViewModel: ViewModel() {
    private val _appState = MutableStateFlow<AppState>(AppState.Loading)
    val appState = _appState.asStateFlow()

    fun login(email: String, role: UserRole) {
        _appState.value = AppState.LoggedIn(User("1", email, role))
    }

    fun logOut() {
        _appState.value = AppState.Guest
    }

    fun goGuest() {
        _appState.value = AppState.Guest
    }

    fun finishLoading() {
        _appState.value = AppState.Guest
    }
}

data class BottomNavItem(
    val title: String,
    val selectedIcon: ImageVector,
    val badgeCount: Int,
    val unselectedIcon: ImageVector
)



object Route {
    const val LOADING = "LOADING"
    const val LOGIN = "LOGIN"
    const val USER = "USER_ROOT"
    const val RESTAURANT = "RESTAURANT_ROOT"
    const val DRIVER = "DRIVER_ROOT"
    const val ADMIN = "ADMIN_ROOT"
}




































@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp() {
    val navController = rememberNavController()

    // --- 1. LOG DUY NHẤT TOÀN BỘ STACK ---
    val fullBackStack by navController.currentBackStack.collectAsState()
    LaunchedEffect(fullBackStack) {
        val stackList = fullBackStack
            .map { it.destination.route ?: "root" }
            .filter { it != "root" } // Ẩn bớt root mặc định cho sạch log
        Log.d("NAV_DEBUG", "STACK: ${stackList.joinToString(" -> ")}")
    }

    val listItems = listOf(
        NavBottomItem("Trang chủ", "home_root", Icons.Filled.Home, Icons.Outlined.Home),
        NavBottomItem("Tin nhắn", "message_root", Icons.Filled.Email, Icons.Outlined.Email),
        NavBottomItem("Cài đặt", "setting_root", Icons.Filled.Settings, Icons.Outlined.Settings)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("gemini Navigation") })
        },
        bottomBar = {
            NavigationBar {
                listItems.forEach { item ->
                    // Kiểm tra Hierarchy để highlight icon
                    val isSelected = navBackStackEntry?.destination?.hierarchy?.any {
                        it.route == item.route
                    } == true

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            navController.navigate(item.route) {
                                // Giải pháp LIFO sạch: Pop về Start Destination
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) item.selected else item.unSelected,
                                contentDescription = null
                            )
                        },
                        label = { Text(item.title) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home_root",
            modifier = Modifier.padding(innerPadding)
        ) {
            // Cấu trúc lồng nhau (Nested Graphs)
//            navigation(startDestination = "home", route = "home_root") {
//                composable("home") { HomeScreen(navController) }
//                composable("detail") { DetailScreen() }
//            }
//
//            navigation(startDestination = "message", route = "message_root") {
//                composable("message") {
//                    SimpleScreen("Message Screen")
//                }
//            }
//
//            navigation(startDestination = "setting", route = "setting_root") {
//                composable("setting") {
//                    SimpleScreen("Setting Screen")
//                }
//            }


            composable("home_root") { HomeScreen(navController,
                onClick = { int ->
                navController.navigate("detail_root/$int")
            } ) }
            composable("detail_root/{int}",
                arguments = listOf(
                    navArgument("int") {type = NavType.IntType}
                )) { backStackEntry ->
                val numberIndex = backStackEntry.arguments?.getInt("int")

                DetailScreen(navController, numberIndex) }
            composable("message_root") {
                SimpleScreen("Message Screen", navController)
            }
            composable("setting_root") {
                SimpleScreen("Setting Screen",navController)
            }
            composable("detail_email"){SimpleScreen("Detail Screen", navController)}
            composable("detail_detail"){Text("detail detail")}

        }
    }
}

@Composable
fun HomeScreen(navController: NavController, onClick: (number: Int) -> Unit) {
    var counter by rememberSaveable { mutableStateOf(0) }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Text(
                "Màn hình chính - Bấm để vào Chi tiết",
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
        items((1..20).toList()) { i ->
            Text("Mục Home $i", Modifier.clickable {
                onClick(i)
            })
            Spacer(modifier = Modifier.height(10.dp))
        }
        item {
            Text(
                "State: $counter (Bấm để tăng)",
                modifier = Modifier.clickable { counter++ }
            )
        }
    }
}

@Composable
fun DetailScreen(
    navController: NavController, index: Int?) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Text("ĐÂY LÀ MÀN HÌNH CHI TIẾT", modifier = Modifier
                .padding(bottom = 20.dp)
                .clickable { navController.navigate("detail_detail") })
        }

        item {
            Text("Index home =: $index")
        }
        items((1..20).toList()) { i ->
            Text("Dữ liệu chi tiết $i", modifier = Modifier.clickable {

            })
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun SimpleScreen(text: String, navController: NavController) {
    var counter by rememberSaveable { mutableStateOf(0) }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {     Text(text, modifier = Modifier
            .padding(16.dp)
            .clickable { navController.navigate("__") })
        }
        item {
            Text("ĐÂY LÀ MÀN chi tieet  chi tiet", modifier = Modifier.padding(bottom = 20.dp))
        }
        items((1..20).toList()) { i ->
            Text("Dữ liệu chi tiết $i")
            Spacer(modifier = Modifier.height(10.dp))
        }
        item {
            Text(
                "State: $counter (Bấm để tăng)",
                modifier = Modifier.clickable { counter++ }
            )
        }
    }
}

data class NavBottomItem(
    val title: String, // Sửa lỗi chính tả tittle -> title
    val route: String,
    val selected: ImageVector,
    val unSelected: ImageVector
)