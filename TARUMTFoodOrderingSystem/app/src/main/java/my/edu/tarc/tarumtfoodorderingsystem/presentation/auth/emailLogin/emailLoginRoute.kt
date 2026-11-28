package my.edu.tarc.tarumtfoodorderingsystem.presentation.auth.emailLogin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun EmailLoginRoute(
    viewModel: EmailLoginViewModel,
    isDarkTheme: Boolean,
    onDarkThemeChanged: (Boolean) -> Unit,
    onBackToPasswordLogin: () -> Unit,
    onContinueToOTP: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val currentOnContinueToOTP by rememberUpdatedState(
        newValue = onContinueToOTP
    )

    LaunchedEffect(uiState.pendingEmail) {
        val email = uiState.pendingEmail

        if (email != null) {
            viewModel.onEmailNavigationHandled()
            currentOnContinueToOTP(email)
        }
    }

    EmailLoginScreen(
        uiState = uiState,
        isDarkTheme = isDarkTheme,
        onDarkThemeChanged = onDarkThemeChanged,
        modifier = modifier,
        onAction = { action ->
            when (action) {
                EmailLoginAction.BackToPasswordClicked -> {
                    if (uiState.pendingEmail == null) {
                        onBackToPasswordLogin()
                    }
                }

                else -> viewModel.onAction(action)
            }
        }
    )
}