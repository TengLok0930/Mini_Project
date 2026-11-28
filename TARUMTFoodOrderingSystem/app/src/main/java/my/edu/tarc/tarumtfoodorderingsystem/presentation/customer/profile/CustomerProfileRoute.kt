package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CustomerProfileRoute(
    viewModel: CustomerProfileViewModel,
    isDarkTheme: Boolean,
    onDarkThemeChanged: (Boolean) -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToVouchers: () -> Unit,
    onLogoutRequested: () -> Unit,
    onDeleteAccountRequested: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CustomerProfileScreen(
        uiState = uiState,
        isDarkTheme = isDarkTheme,
        modifier = modifier,
        onAction = { action ->
            when (action) {
                CustomerProfileAction.OrdersClicked -> {
                    onNavigateToOrders()
                }

                CustomerProfileAction.HomeClicked -> {
                    onNavigateToHome()
                }

                CustomerProfileAction.ProfileClicked -> {
                    // Already on Profile.
                }

                CustomerProfileAction.ViewVouchersClicked -> {
                    onNavigateToVouchers()
                }

                is CustomerProfileAction.ThemeSelected -> {
                    onDarkThemeChanged(action.isDarkTheme)
                }

                CustomerProfileAction.LogoutClicked -> {
                    viewModel.showLogoutConfirmation()
                }

                CustomerProfileAction.DismissLogoutClicked -> {
                    viewModel.dismissLogoutConfirmation()
                }

                CustomerProfileAction.ConfirmLogoutClicked -> {
                    viewModel.dismissLogoutConfirmation()
                    onLogoutRequested()
                }

                CustomerProfileAction.ChangePasswordClicked -> {
                    viewModel.showChangePassword()
                }

                CustomerProfileAction.DismissChangePasswordClicked -> {
                    viewModel.dismissChangePassword()
                }

                is CustomerProfileAction.CurrentPasswordChanged -> {
                    viewModel.onCurrentPasswordChanged(action.value)
                }

                is CustomerProfileAction.NewPasswordChanged -> {
                    viewModel.onNewPasswordChanged(action.value)
                }

                is CustomerProfileAction.ConfirmNewPasswordChanged -> {
                    viewModel.onConfirmNewPasswordChanged(action.value)
                }

                CustomerProfileAction.SubmitPasswordChangeClicked -> {
                    viewModel.submitPasswordChange()
                }

                CustomerProfileAction.PasswordChangedMessageShown -> {
                    viewModel.passwordChangedMessageShown()
                }

                CustomerProfileAction.DeleteAccountClicked -> {
                    viewModel.showDeleteConfirmation()
                }

                CustomerProfileAction.DismissDeleteAccountClicked -> {
                    viewModel.dismissDeleteConfirmation()
                }

                CustomerProfileAction.ConfirmDeleteAccountClicked -> {
                    // TODO: Delete the account and its data first.
                    viewModel.dismissDeleteConfirmation()
                    onDeleteAccountRequested()
                }
            }
        }
    )
}