package com.example.proxy

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.proxy.models.ProxyDBItem
import com.example.proxy.models.ProxyItem
import com.example.proxy.presentation.AddProxyScreen
import com.example.proxy.presentation.AppBar
import com.example.proxy.presentation.ListProxyScreen
import com.example.proxy.presentation.MainViewModel
import com.example.proxy.ui.theme.ProxyTheme
import com.example.proxy.ui.theme.containerColor
import com.example.proxy.ui.theme.selectedNavColor
import com.example.proxy.ui.theme.selectedNavIconColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //navigation bar items
        val items = listOf(
            BottomNavigationItem(
                title = "Proxies",
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home
            ),
            BottomNavigationItem(
                title = "Logging",
                selectedIcon = Icons.Filled.Search,
                unselectedIcon = Icons.Outlined.Search
            ),
        )
        setContent {
            ProxyTheme {
                val viewModel by viewModels<MainViewModel>()

                val navController = rememberNavController()

                var selectedItemIndex by rememberSaveable {
                    mutableIntStateOf(0)
                }

                val navBackStackEntry by navController.currentBackStackEntryAsState()

                LaunchedEffect(navBackStackEntry) {
                    Toast.makeText(this@MainActivity, navBackStackEntry?.destination?.route, Toast.LENGTH_SHORT).show()
                }


                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = selectedNavColor,
                    floatingActionButton = {
                        if(navBackStackEntry?.destination?.route == ListProxyDest::class.qualifiedName)
                        AddProxyFAB(navController = navController)
                    },
                    bottomBar = {
                        NavigationBar(
                            containerColor = containerColor
                        )
                            {
                            items.forEachIndexed { index,item->
                                NavigationBarItem(
                                    selected = selectedItemIndex == index,
                                    onClick = {
                                        selectedItemIndex = index

                                        //do navigation
                                        //navcontroller.navigate(item.title)

                                        if(selectedItemIndex == 0) navController.navigate(ListProxyDest)
//                                        else navController.navigate(WelcomeScreenDest)
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = if(index == selectedItemIndex) item.selectedIcon else item.unselectedIcon,
                                            contentDescription = item.title
                                        )
                                    },
                                    label = {
                                        Text(text = item.title)
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = selectedNavIconColor,
                                        unselectedIconColor = Color.Gray,
                                        selectedTextColor = selectedNavIconColor,
                                        unselectedTextColor = Color.Gray,
                                        indicatorColor = selectedNavColor
                                    ).copy()


                                )
                            }
                        }
                    }
                    ) { innerPadding ->

                    NavHost(
                        modifier = Modifier.fillMaxSize(),
                        navController = navController,
                        startDestination = ListProxyDest
                    ){
                        composable<ListProxyDest>{
                            ListProxyScreen(
                                viewModel = viewModel,
                                navController = navController
                            )
                        }
                        
                        composable<AddProxyDest>(
                            typeMap = mapOf(typeOf<ProxyItem>() to ProxyType)
                        ) {
                            val args = it.toRoute<AddProxyDest>()
                            AddProxyScreen(
                                navController = navController,
                                viewModel = viewModel,
                                isEditMode = args.isEditMode
                            )
                        }

                        composable<EditProxyDest>(
                            typeMap = mapOf(typeOf<ProxyItem>() to ProxyType)
                        ) {
                            val args = it.toRoute<EditProxyDest>()
                            AddProxyScreen(
                                navController = navController,
                                viewModel = viewModel,
                                isEditMode = true,
                                proxyItem = args.proxyItem
                            )
                        }


                    }
                }
            }
        }
    }
}

@Composable
fun AddProxyFAB(navController: NavController){
    FloatingActionButton(
        onClick = { navController.navigate(
            AddProxyDest(
                isEditMode = false
            )
        ) },
        containerColor = Color(0xFF0078B7),
        contentColor = Color.White
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "Add Proxy",
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Serializable
object ListProxyDest


@Serializable
data class AddProxyDest(
    val isEditMode: Boolean
)

@Serializable
data class EditProxyDest(
    val isEditMode: Boolean,
    val proxyItem: ProxyItem
)

val ProxyType = object : NavType<ProxyItem>(isNullableAllowed = true) {
    override fun get(bundle: Bundle, key: String): ProxyItem? {
        return if(Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU){
            bundle.getParcelable(key)
        } else{
            bundle.getParcelable(key, ProxyItem::class.java)
        }

    }
    override fun parseValue(value: String): ProxyItem {
//        return Gson().fromJson(value, ProxyItem::class.java)

        return Json.decodeFromString<ProxyItem>(value)
    }
    override fun put(bundle: Bundle, key: String, value: ProxyItem) {
        bundle.putParcelable(key, value)
    }

    override fun serializeAsValue(value: ProxyItem): String = Json.encodeToString(value)
}

