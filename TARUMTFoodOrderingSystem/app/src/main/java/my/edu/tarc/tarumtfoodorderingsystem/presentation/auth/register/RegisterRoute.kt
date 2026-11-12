package my.edu.tarc.tarumtfoodorderingsystem.presentation.auth.register

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun RegisterRoute(
    viewModel: RegisterViewModel,
    onNavigateBack: () -> Unit,
    onRegistrationSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val currentOnRegistrationSuccess by rememberUpdatedState(newValue = onRegistrationSuccess)

    LaunchedEffect(uiState.isRegistrationSuccessful) {
        if (uiState.isRegistrationSuccessful) {
            viewModel.onRegistrationSuccessHandled()
            currentOnRegistrationSuccess()
        }
    }

    RegisterScreen(
        uiState = uiState,
        modifier = modifier,
        onAction = { action ->
            when (action) {
                RegisterAction.BackClicked -> {
                    if (!uiState.isLoading && !uiState.isRegistrationSuccessful) {
                        onNavigateBack()
                    }
                }

                else -> viewModel.onAction(action)
            }
        }
    )
}