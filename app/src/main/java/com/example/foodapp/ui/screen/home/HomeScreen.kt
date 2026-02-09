
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.foodapp.core.UserRoutes
import com.example.foodapp.core.bottomRouteFromIndex
import com.example.foodapp.ui.components.HomeBottomBar
import com.example.foodapp.ui.screen.home.HomeNavGraph

@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    var selectedIndex by remember { mutableStateOf(0) }


    Scaffold(
        bottomBar = {
            HomeBottomBar(
                selectedIndex = selectedIndex,
                onItemSelected = { index ->
                    selectedIndex = index
                    navController.navigate(bottomRouteFromIndex(index)) {
                        launchSingleTop = true
                         restoreState = true
                        popUpTo(UserRoutes.HOME) {saveState = true}
                    }
                }
            )
        }
    ) { paddingValue ->
            HomeNavGraph(
                navController = navController,
                modifier = Modifier.padding(paddingValue)
            )
        }
    }

