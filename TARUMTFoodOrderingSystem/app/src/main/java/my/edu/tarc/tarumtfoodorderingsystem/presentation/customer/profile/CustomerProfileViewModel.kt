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
    val showLogoutConfirmation: Boolean = false
)

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