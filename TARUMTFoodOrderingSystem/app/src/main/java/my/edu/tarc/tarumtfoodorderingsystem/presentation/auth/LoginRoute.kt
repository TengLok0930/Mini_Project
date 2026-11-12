package my.edu.tarc.tarumtfoodorderingsystem.presentation.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun LoginRoute(
    viewModel: LoginViewModel,
    isDarkTheme: Boolean,
    onDarkThemeChanged: (Boolean) -> Unit,
    onNavigateToOTPLogin: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val currentOnLoginSuccess by rememberUpdatedState (newValue = onLoginSuccess)

    LaunchedEffect(uiState.isLoginSuccessful) {
        if (uiState.isLoginSuccessful) {
            viewModel.onLoginSuccessHandled()
            currentOnLoginSuccess()
        }
    }

    LoginScreen(
        uiState = uiState,
        isDarkTheme = isDarkTheme,
        onDarkThemeChanged = onDarkThemeChanged,
        modifier = modifier,
        onAction = { action ->
            when (action) {
                LoginAction.EmailCodeClicked -> {
                    if (
                        !uiState.isLoading &&
                        !uiState.isLoginSuccessful
                    ) {
                        onNavigateToOTPLogin()
                    }
                }

                LoginAction.RegisterClicked -> {
                    if (
                        !uiState.isLoading &&
                        !uiState.isLoginSuccessful
                    ) {
                        onNavigateToRegister()
                    }
                }

                else -> viewModel.onAction(action)
            }
        }
    )
}