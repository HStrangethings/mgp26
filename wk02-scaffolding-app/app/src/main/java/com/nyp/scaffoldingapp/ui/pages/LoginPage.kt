package com.nyp.scaffoldingapp.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nyp.scaffoldingapp.ui.login.LoginState
import com.nyp.scaffoldingapp.ui.login.LoginViewModel
// ─────────────────────────────────────────────────────────────
// LOGIN PAGE: the first screen the user sees (AppRoot in MainActivity decides that).
// It draws and reports events. The checking itself happens in the ViewModel.
// ─────────────────────────────────────────────────────────────
@Composable
fun LoginPage(
    viewModel: LoginViewModel,
    modifier: Modifier = Modifier
) {
    // Read-only state from the ViewModel; this function recomposes when it changes.
    val state by viewModel.state.collectAsState()

    // What the user typed. rememberSaveable so rotating the phone does not wipe it (Week 3).
    var input by rememberSaveable { mutableStateOf("") }

    // Derived state, recalculated on every keystroke: null = fine, text = error to show under the field.
    val inputError: String? = when {
        input.isEmpty() -> null                              // untouched, don't nag yet
        !input.all { it.isDigit() } -> "Numbers only"        // validated as the user types
        else -> null
    }

    val isChecking = state == LoginState.Checking

    // Column + Center = the input and button sit in the vertical middle of the screen.
    Column(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding()                            // keep clear of status bar and keyboard
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,            // centre vertically
        horizontalAlignment = Alignment.CenterHorizontally   // centre horizontally
    ) {
        Text("Login", style = MaterialTheme.typography.headlineLarge)

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = input,
            onValueChange = { input = it },                  // keep whatever was typed
            label = { Text("User ID") },
            singleLine = true,
            isError = inputError != null,                    // red outline on error
            supportingText = {                               // message directly under the field
                if (inputError != null) Text(inputError)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), // number keypad
            enabled = !isChecking,                           // lock the field while checking
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { viewModel.login(input) },            // event goes UP to the ViewModel
            // Disabled when empty, invalid, or already checking.
            enabled = input.isNotEmpty() && inputError == null && !isChecking,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isChecking) {
                // Visible loading state inside the button.
                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
            } else {
                Text("Login")
            }
        }
    }

    // POPUP: shown only while the state is Error.
    // Local val so the compiler can smart-cast (a 'by' delegate can't be smart-cast).
    val current = state
    if (current is LoginState.Error) {
        AlertDialog(
            onDismissRequest = viewModel::dismissError,      // tapping outside or pressing back closes it
            title = { Text("Login failed") },
            text = { Text(current.message) },                // "User not registered!" or a network message
            confirmButton = {
                TextButton(onClick = viewModel::dismissError) { Text("OK") }
            }
        )
    }
}
