package com.nyp.scaffoldingapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyp.scaffoldingapp.data.LoginApi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONException
import java.net.SocketTimeoutException

// the login screen is in EXACTLY one of these states.
sealed interface LoginState {
    data object Idle : LoginState                        // waiting for the user to type and press the button
    data object Checking : LoginState                    // request in progress
    data object Success : LoginState                     // registered: show the main menu
    data class Error(val message: String) : LoginState   // shown in the popup
}

// A ViewModel survives rotation, so rotating mid-check does not restart or lose the result.
class LoginViewModel : ViewModel() {

    // Private and mutable: only this class changes the state.
    private val _state = MutableStateFlow<LoginState>(LoginState.Idle)

    // Read-only for the screen: state goes down, events go up (Week 3).
    val state: StateFlow<LoginState> = _state.asStateFlow()

    // Event from the screen: the button was pressed.
    fun login(input: String) {
        if (_state.value == LoginState.Checking) return // ignore double taps while a request is running

        _state.value = LoginState.Checking // show the loading state immediately

        // viewModelScope: the request is cancelled automatically if the ViewModel goes away.
        viewModelScope.launch {
            try {
                val registered = LoginApi.isRegistered(input.trim())
                _state.value = if (registered) {
                    LoginState.Success
                } else {
                    LoginState.Error("User not registered!") // the popup text
                }
            } catch (e: SocketTimeoutException) {
                // Most specific first, otherwise the general catch below would swallow it.
                _state.value = LoginState.Error("The server took too long to answer. Please try again.")
            } catch (e: CancellationException) {
                // Not a failure: rethrow so cancellation works properly.
                throw e
            } catch (e: JSONException) {
                _state.value = LoginState.Error("We received data we could not read. Please try again later.")
            } catch (e: Exception) {
                // Anything else, such as no internet. Never show e.toString() to the user.
                _state.value = LoginState.Error("Could not reach the server. Check your connection and try again.")
            }
        }
    }

    // Event from the screen: the popup's OK button was pressed.
    fun dismissError() {
        _state.value = LoginState.Idle
    }
}



