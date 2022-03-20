package com.breavestsnail.wanandroid

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.breavestsnail.wanandroid.logic.model.User
import com.breavestsnail.wanandroid.ui.Project
import com.breavestsnail.wanandroid.ui.WebViewPage
import com.breavestsnail.wanandroid.ui.home.Home
import com.breavestsnail.wanandroid.ui.me.LoginPage
import com.breavestsnail.wanandroid.ui.me.Me
import com.breavestsnail.wanandroid.ui.question.Question
import com.breavestsnail.wanandroid.ui.theme.WanAndroidTheme


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WanAndroidTheme {
                WanAndroid()
            }
        }

    }

}

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun WanAndroid(){
    val navController = rememberNavController()
    val backStackEntry = navController.currentBackStackEntryAsState()
    val allScreens = WanAndroidScreen.values().toList()
    var currentScreen = WanAndroidScreen.fromRoute(backStackEntry.value?.destination?.route)
    Scaffold(
        bottomBar = {
            NavBar(
                allScreens,
                currentScreen,
                onTabSelected = {
                    currentScreen = it
                    navController.navigate(it.name)
                }
            )
        }
    ) { innerPadding->
        WanAndroidNavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController
        )
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun WanAndroidNavHost(modifier: Modifier,navController: NavHostController){
    val model: AppViewModel = viewModel()
    var user:User = remember {
        User(-1,false, listOf(), listOf(),"","","","登录/注册")
    }
    NavHost(
        navController = navController,
        startDestination = WanAndroidScreen.Home.name,
        modifier = modifier
    ){
        composable(WanAndroidScreen.Home.name){
            Home(onClickItem = {
                model.acticleWebPage.value = it
                navController.navigate("${WanAndroidScreen.Home.name}/${it.title.removeHtml()}")
            })
            // TODO: 使用Navigation为Chapter导航
        }
        // TODO: 实现问答功能
//        composable(WanAndroidScreen.Question.name){
//            Question()
//        }
        composable(WanAndroidScreen.Project.name){
            Project(
                onClickItem = {
                    model.acticleWebPage.value = it
                    navController.navigate("${WanAndroidScreen.Home.name}/${it.title.removeHtml()}")
                }
            )
        }
        composable(WanAndroidScreen.Me.name) {
            Me(
                user = user,
                onClickLogin = {
                    navController.navigate("${WanAndroidScreen.Me.name}/login")
                }
            )
        }
        composable(
            route = "${WanAndroidScreen.Home.name}/{acticle}",
            arguments = listOf(navArgument("acticle"){
                type = NavType.StringType
            })
        ){
            val webPage = model.acticleWebPage.observeAsState()
            val currentScreen = WanAndroidScreen.fromRoute(navController.currentBackStackEntryAsState().value?.destination?.route)
            webPage.value?.let { it1 -> WebViewPage(it1,onClickBackBtn = {navController.navigate(currentScreen.name)}) }
        }
        composable(
            route = "${WanAndroidScreen.Me.name}/{login}",
            arguments = listOf(navArgument("login"){
                type = NavType.StringType
            })
        ){
            val currentScreen = WanAndroidScreen.fromRoute(navController.currentBackStackEntryAsState().value?.destination?.route)
            LoginPage(onClickBackBtn = {navController.navigate(currentScreen.name)},onClickLoginBtn = { username, password ->
                val tmp = model.login(username, password).value?.getOrNull()
                if (tmp!=null&&tmp.id!=-1){
                    user = tmp
                    navController.navigate(WanAndroidScreen.Me.name)
                }else{
                    Toast.makeText(WanAndroidApplication.context,"登录失败",Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}

@Composable
fun NavBar(allScreens: List<WanAndroidScreen>,currentScreen: WanAndroidScreen,onTabSelected:(WanAndroidScreen)->Unit) {

    Row(
        Modifier
            .height(84.dp)
            .padding(vertical = 10.dp)
    ) {
        var selectedItem = currentScreen.name
        for (screen in allScreens){
            NavItem(screen, selectedItem) {
                selectedItem = it.name
                onTabSelected(it)
            }
        }
    }
}

@Composable
fun RowScope.NavItem(
    screen: WanAndroidScreen,
    selectedItem: String,
    onTabSelected: (WanAndroidScreen) -> Unit
) {

    val color by animateColorAsState(
        targetValue = if (selectedItem == screen.name) MaterialTheme.colors.primary else Color.Gray
    )
    Button(
        onClick = { onTabSelected(screen) },
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight(),
        shape = RectangleShape,
        colors = ButtonDefaults.outlinedButtonColors(),
        elevation = ButtonDefaults.elevation(0.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(id = screen.icon),
                contentDescription = screen.desc,
                modifier = Modifier
                    .size(24.dp),
                tint = color
            )
            Text(text = screen.desc, color = color)
        }
    }
}

