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
enum class Tab(val label: String, @DrawableRes val icon: Int) {
    PAGE1("Page 1", R.drawable.ic_home),
    PAGE2("Page 2", R.drawable.ic_email),
    PAGE3("Page 3", R.drawable.ic_list),
    PAGE4("Page 4", R.drawable.ic_star)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScaffoldingTheme {
                AppRoot() // was: AppScaffold()
            }
        }
    }
}

@Composable
fun AppRoot() {
    // viewModel() gives the Activity-scoped LoginViewModel, so it survives rotation.
    val loginViewModel: LoginViewModel = viewModel()
    val loginState by loginViewModel.state.collectAsState()

    if (loginState is LoginState.Success) {
        AppRoot()
    } else {
        LoginPage(loginViewModel)
    }
}
