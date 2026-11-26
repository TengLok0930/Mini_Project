package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.adminPanel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppTopBar

@Composable
fun AdminPanelScreen(
    uiState: AdminPanelUiState,
    onAction: (AdminPanelAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(
                title = "Admin Panel",
                onLogoutClick = {
                    onAction(AdminPanelAction.LogoutClicked)
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 560.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        horizontal = 22.dp,
                        vertical = 28.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                AdminPanelMenuButton(
                    text = "Canteen Maintenance",
                    onClick = {
                        onAction(
                            AdminPanelAction.CanteenMaintenanceClicked
                        )
                    }
                )

                AdminPanelMenuButton(
                    text = "Store Maintenance",
                    onClick = {
                        onAction(
                            AdminPanelAction.StoreMaintenanceClicked
                        )
                    }
                )

                AdminPanelMenuButton(
                    text = "Menu Maintenance",
                    onClick = {
                        onAction(
                            AdminPanelAction.MenuMaintenanceClicked
                        )
                    }
                )

                AdminPanelMenuButton(
                    text = "Sales Reporting",
                    onClick = {
                        onAction(
                            AdminPanelAction.SalesReportingClicked
                        )
                    }
                )
            }
        }
    }

    if (uiState.showLogoutConfirmation) {
        AlertDialog(
            onDismissRequest = {
                onAction(AdminPanelAction.LogoutDismissed)
            },
            title = {
                Text("Log out?")
            },
            text = {
                Text("Are you sure you want to log out?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onAction(AdminPanelAction.LogoutConfirmed)
                    }
                ) {
                    Text("Log out")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onAction(AdminPanelAction.LogoutDismissed)
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun AdminPanelMenuButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 96.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 12.dp)
        )
    }
}
