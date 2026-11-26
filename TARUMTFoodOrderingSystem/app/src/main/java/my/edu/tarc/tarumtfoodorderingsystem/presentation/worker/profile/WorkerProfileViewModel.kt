package my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.orders.pendingOrders.WorkerPendingOrderUiState

private const val MIN_PASSWORD_LENGTH = 8

data class WorkerProfileUiState(
    val storeName: String = "",
    val canteenName: String = "",
    val accountRole: String = "Worker",
    val showLogoutDialog: Boolean = false,
    val showChangePasswordDialog: Boolean = false,
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmNewPassword: String = "",
    val passwordError: String? = null,
    val showPasswordChangedMessage: Boolean = false
) {
    val accountTitle: String
        get() = listOf(
            storeName,
            canteenName
        )
            .filter { it.isNotBlank() }
            .joinToString(" · ")
}

private data class WorkerProfileLocalState(
    val showLogoutDialog: Boolean = false,
    val showChangePasswordDialog: Boolean = false,
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmNewPassword: String = "",
    val passwordError: String? = null,
    val showPasswordChangedMessage: Boolean = false
)

class WorkerProfileViewModel(
    private val sharedState: StateFlow<WorkerPendingOrderUiState>
) : ViewModel() {

    private val localState = MutableStateFlow(
        WorkerProfileLocalState()
    )

    val uiState: StateFlow<WorkerProfileUiState> = combine(
        sharedState,
        localState
    ) { shared, local ->
        buildUiState(
            shared = shared,
            local = local
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = buildUiState(
            shared = sharedState.value,
            local = localState.value
        )
    )

    fun onAction(
        action: WorkerProfileAction
    ) {
        when (action) {
            WorkerProfileAction.LogoutClicked -> {
                localState.update {
                    it.copy(showLogoutDialog = true)
                }
            }

            WorkerProfileAction.DismissLogoutClicked -> {
                localState.update {
                    it.copy(showLogoutDialog = false)
                }
            }

            WorkerProfileAction.ChangePasswordClicked -> {
                localState.update {
                    it.copy(showChangePasswordDialog = true)
                }
            }

            WorkerProfileAction.DismissChangePasswordClicked -> {
                localState.update {
                    it.copy(
                        showChangePasswordDialog = false,
                        currentPassword = "",
                        newPassword = "",
                        confirmNewPassword = "",
                        passwordError = null
                    )
                }
            }

            is WorkerProfileAction.CurrentPasswordChanged -> {
                localState.update {
                    it.copy(
                        currentPassword = action.value,
                        passwordError = null
                    )
                }
            }

            is WorkerProfileAction.NewPasswordChanged -> {
                localState.update {
                    it.copy(
                        newPassword = action.value,
                        passwordError = null
                    )
                }
            }

            is WorkerProfileAction.ConfirmNewPasswordChanged -> {
                localState.update {
                    it.copy(
                        confirmNewPassword = action.value,
                        passwordError = null
                    )
                }
            }

            WorkerProfileAction.SubmitPasswordChangeClicked -> {
                submitPasswordChange()
            }

            WorkerProfileAction.PasswordChangedMessageShown -> {
                localState.update {
                    it.copy(showPasswordChangedMessage = false)
                }
            }

            WorkerProfileAction.BackClicked,
            WorkerProfileAction.ConfirmLogoutClicked,
            is WorkerProfileAction.ThemeSelected -> {
                // Navigation and theme changes are handled by the Route.
            }
        }
    }

    private fun submitPasswordChange() {
        val state = localState.value

        val error = when {
            state.currentPassword.isBlank() ->
                "Enter your current password."

            state.newPassword.length < MIN_PASSWORD_LENGTH ->
                "New password must be at least " +
                        "$MIN_PASSWORD_LENGTH characters."

            state.newPassword == state.currentPassword ->
                "New password must be different from the current one."

            state.newPassword != state.confirmNewPassword ->
                "New passwords do not match."

            else -> null
        }

        if (error != null) {
            localState.update { it.copy(passwordError = error) }
            return
        }

        // TODO: Verify state.currentPassword against the real account
        //  (re-authenticate), then update to state.newPassword.
        //  If verification fails, set passwordError =
        //  "Current password is incorrect." and return.

        localState.update {
            it.copy(
                showChangePasswordDialog = false,
                currentPassword = "",
                newPassword = "",
                confirmNewPassword = "",
                passwordError = null,
                showPasswordChangedMessage = true
            )
        }
    }

    private fun buildUiState(
        shared: WorkerPendingOrderUiState,
        local: WorkerProfileLocalState
    ): WorkerProfileUiState {
        return WorkerProfileUiState(
            storeName = shared.storeName,
            canteenName = shared.canteenName,
            showLogoutDialog = local.showLogoutDialog,
            showChangePasswordDialog = local.showChangePasswordDialog,
            currentPassword = local.currentPassword,
            newPassword = local.newPassword,
            confirmNewPassword = local.confirmNewPassword,
            passwordError = local.passwordError,
            showPasswordChangedMessage = local.showPasswordChangedMessage
        )
    }
}