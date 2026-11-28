package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.adminPanel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AdminPanelUiState(
    val showLogoutConfirmation: Boolean = false
)

class AdminPanelViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AdminPanelUiState())
    val uiState = _uiState.asStateFlow()

    fun onAction(action: AdminPanelAction) {
        when (action) {
            AdminPanelAction.LogoutClicked -> {
                _uiState.update {
                    it.copy(showLogoutConfirmation = true)
                }
            }

            AdminPanelAction.LogoutDismissed,
            AdminPanelAction.LogoutConfirmed -> {
                _uiState.update {
                    it.copy(showLogoutConfirmation = false)
                }
            }

            // Navigations
            AdminPanelAction.CanteenMaintenanceClicked,
            AdminPanelAction.StoreMaintenanceClicked,
            AdminPanelAction.MenuMaintenanceClicked,
            AdminPanelAction.SalesReportingClicked -> Unit
        }
    }
}