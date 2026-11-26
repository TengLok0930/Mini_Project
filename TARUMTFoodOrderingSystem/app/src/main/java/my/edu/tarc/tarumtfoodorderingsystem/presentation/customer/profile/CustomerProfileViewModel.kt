package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.profile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CustomerProfileUiState(
    val accountName: String = "",
    val accountRole: String = "",
    val totalSpentInSen: Long = 0L,
    val ordersThisMonth: Int = 0,
    val availableVoucherCount: Int = 0,
    val showLogoutConfirmation: Boolean = false,
    val showChangePasswordDialog: Boolean = false,
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmNewPassword: String = "",
    val passwordError: String? = null,
    val showPasswordChangedMessage: Boolean = false,
    val showDeleteConfirmation: Boolean = false
)

private const val MIN_PASSWORD_LENGTH = 8

class CustomerProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        customerProfileSampleState()
    )

    val uiState = _uiState.asStateFlow()

    fun showLogoutConfirmation() {
        _uiState.update {
            it.copy(showLogoutConfirmation = true)
        }
    }

    fun dismissLogoutConfirmation() {
        _uiState.update {
            it.copy(showLogoutConfirmation = false)
        }
    }

    fun showChangePassword() {
        _uiState.update {
            it.copy(showChangePasswordDialog = true)
        }
    }

    fun dismissChangePassword() {
        _uiState.update {
            it.copy(
                showChangePasswordDialog = false,
                currentPassword = "",
                newPassword = "",
                confirmNewPassword = "",
                passwordError = null
            )
        }
    }

    fun onCurrentPasswordChanged(value: String) {
        _uiState.update {
            it.copy(currentPassword = value, passwordError = null)
        }
    }

    fun onNewPasswordChanged(value: String) {
        _uiState.update {
            it.copy(newPassword = value, passwordError = null)
        }
    }

    fun onConfirmNewPasswordChanged(value: String) {
        _uiState.update {
            it.copy(confirmNewPassword = value, passwordError = null)
        }
    }

    fun submitPasswordChange() {
        val state = _uiState.value

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
            _uiState.update { it.copy(passwordError = error) }
            return
        }

        // TODO: Verify state.currentPassword against the real account
        //  (re-authenticate), then update to state.newPassword.
        //  If verification fails, set passwordError =
        //  "Current password is incorrect." and return.

        _uiState.update {
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

    fun passwordChangedMessageShown() {
        _uiState.update {
            it.copy(showPasswordChangedMessage = false)
        }
    }

    fun showDeleteConfirmation() {
        _uiState.update {
            it.copy(showDeleteConfirmation = true)
        }
    }

    fun dismissDeleteConfirmation() {
        _uiState.update {
            it.copy(showDeleteConfirmation = false)
        }
    }
}

/**
 * Frontend sample data only.
 * Replace with account, order, and voucher repository data later.
 */
internal fun customerProfileSampleState(): CustomerProfileUiState {
    return CustomerProfileUiState(
        accountName = "University Account Name",
        accountRole = "Student",
        totalSpentInSen = 12_850L,
        ordersThisMonth = 18,
        availableVoucherCount = 2
    )
}