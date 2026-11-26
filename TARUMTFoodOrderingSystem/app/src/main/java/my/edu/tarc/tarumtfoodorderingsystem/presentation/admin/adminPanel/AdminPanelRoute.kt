package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.adminPanel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AdminPanelRoute(
    viewModel: AdminPanelViewModel,
    onNavigateToCanteenMaintenance: () -> Unit,
    onNavigateToStoreMaintenance: () -> Unit,
    onNavigateToMenuMaintenance: () -> Unit,
    onNavigateToSalesReporting: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AdminPanelScreen(
        uiState = uiState,
        modifier = modifier,
        onAction = { action ->
            when (action) {
                AdminPanelAction.CanteenMaintenanceClicked -> {
                    onNavigateToCanteenMaintenance()
                }

                AdminPanelAction.StoreMaintenanceClicked -> {
                    onNavigateToStoreMaintenance()
                }

                AdminPanelAction.MenuMaintenanceClicked -> {
                    onNavigateToMenuMaintenance()
                }

                AdminPanelAction.SalesReportingClicked -> {
                    onNavigateToSalesReporting()
                }

                AdminPanelAction.LogoutConfirmed -> {
                    if (uiState.showLogoutConfirmation) {
                        viewModel.onAction(action)
                        onLogout()
                    }
                }

                else -> viewModel.onAction(action)
            }
        }
    )
}