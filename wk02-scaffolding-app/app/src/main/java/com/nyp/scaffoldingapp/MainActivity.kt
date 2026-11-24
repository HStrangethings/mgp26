package com.nyp.scaffoldingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.annotation.DrawableRes
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.nyp.scaffoldingapp.ui.pages.*
import com.nyp.scaffoldingapp.ui.theme.ScaffoldingTheme
import practice.wk03.Settingspage
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nyp.scaffoldingapp.ui.login.LoginState
import com.nyp.scaffoldingapp.ui.login.LoginViewModel


//bitch
/**
 * The four bottom tabs. An enum keeps the list in one place — add a tab here
 * and the navigation bar updates itself.
 */
/**
 * The four bottom tabs. An enum keeps the list in one place: add a tab here
 * and the navigation bar updates itself.
 */
enum class Tab(val label: String, @DrawableRes val icon: Int) {
    PAGE1("Home", R.drawable.ic_home),
    PAGE2("Transfer", R.drawable.ic_email),
    PAGE3("History", R.drawable.ic_list),
    PAGE4("Game", R.drawable.ic_star)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScaffoldingTheme {
                AppRoot() // the ONLY place AppRoot is called from
            }
        }
    }
}

/**
 * First thing the app shows. Until login succeeds it shows LoginPage,
 * afterward it shows the main menu (AppScaffold).
 */
@Composable
fun AppRoot() {
    // viewModel() gives an Activity-scoped LoginViewModel, so it survives rotation.
    val loginViewModel: LoginViewModel = viewModel()
    val loginState by loginViewModel.state.collectAsState()

    if (loginState is LoginState.Success) {
        AppScaffold()                 // the main menu with the bottom bar (defined below)
    } else {
        LoginPage(loginViewModel)
    }
}

/**
 * The main menu: bottom navigation bar plus the page for the selected tab.
 * NOTE: this function must NOT call AppRoot().
 */
@Composable
fun AppScaffold() {
    // rememberSaveable, not remember: the selected tab must survive a rotation.
    var selectedTab by rememberSaveable { mutableStateOf(Tab.PAGE1) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                Tab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        icon = {
                            // Icon() tints the image with the theme colour,
                            // giving selected / unselected states for free.
                            Icon(
                                painter = painterResource(tab.icon),
                                contentDescription = tab.label
                            )
                        },
                        label = { Text(tab.label) }
                    )
                }
            }
        }
    ) { padding ->
        // Use the padding Scaffold gives you, or your content hides behind the navigation bar.
        Box(Modifier.padding(padding).fillMaxSize()) {
            when (selectedTab) {
                Tab.PAGE1 -> Page1()
                Tab.PAGE2 -> Page2()
                Tab.PAGE3 -> Page3()
                Tab.PAGE4 -> Page4()
            }
        }
    }
}
