package my.edu.tarc.tarumtfoodorderingsystem.presentation.auth.otp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun OTPLoginRoute(
    viewModel: OTPLoginViewModel,
    onNavigateBack: () -> Unit,
    onChangeEmail: () -> Unit,
    onVerificationSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val currentOnVerificationSuccess by rememberUpdatedState(
        newValue = onVerificationSuccess
    )

    LaunchedEffect(uiState.isVerificationSuccessful) {
        if (uiState.isVerificationSuccessful) {
            viewModel.onVerificationSuccessHandled()
            currentOnVerificationSuccess()
        }
    }

    OTPLoginScreen(
        uiState = uiState,
        modifier = modifier,
        onAction = { action ->
            when (action) {
                OTPLoginAction.BackClicked -> {
                    if (!uiState.isVerificationSuccessful) {
                        onNavigateBack()
                    }
                }

                OTPLoginAction.ChangeEmailClicked -> {
                    if (!uiState.isVerificationSuccessful) {
                        onChangeEmail()
                    }
                }

                else -> viewModel.onAction(action)
            }
        }
    )
}