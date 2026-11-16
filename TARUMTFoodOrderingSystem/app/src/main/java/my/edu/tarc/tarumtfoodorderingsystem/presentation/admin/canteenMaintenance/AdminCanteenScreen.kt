package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.canteenMaintenance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppActionButton
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppTopBar

@Composable
fun AdminCanteenScreen(
    uiState: AdminCanteenUiState,
    onAction: (AdminCanteenAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colors.background,
        topBar = {
            AppTopBar(
                title = "Canteen Maintenance",
                onBackClick = {
                    onAction(AdminCanteenAction.BackClicked)
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
                    .fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(
                        horizontal = 22.dp,
                        vertical = 24.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item(key = "canteen_heading") {
                        Text(
                            text = "Canteens available " +
                                    "(${uiState.canteens.size})",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = colors.secondary,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }

                    if (uiState.canteens.isEmpty()) {
                        item(key = "empty_state") {
                            Text(
                                text = "No canteens available. " +
                                        "Create a new canteen to get started.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = colors.onSurfaceVariant,
                                modifier = Modifier.padding(vertical = 16.dp)
                            )
                        }
                    }

                    items(
                        items = uiState.canteens,
                        key = { canteen -> canteen.id }
                    ) { canteen ->
                        AdminCanteenRow(
                            canteen = canteen,
                            onRemoveClick = {
                                onAction(
                                    AdminCanteenAction.RemoveCanteenClicked(
                                        canteenId = canteen.id
                                    )
                                )
                            }
                        )
                    }
                }

                AppActionButton(
                    title = "+ Create New Canteen",
                    onClick = {
                        onAction(
                            AdminCanteenAction.CreateCanteenClicked
                        )
                    },
                    modifier = Modifier.padding(
                        horizontal = 22.dp,
                        vertical = 20.dp
                    )
                )
            }
        }
    }

    uiState.pendingRemoval?.let { canteen ->
        AlertDialog(
            onDismissRequest = {
                onAction(AdminCanteenAction.DismissRemoveClicked)
            },
            title = {
                Text("Remove canteen?")
            },
            text = {
                Text(
                    text = "Remove \"${canteen.name}\" from the canteen list?"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onAction(
                            AdminCanteenAction.ConfirmRemoveClicked
                        )
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = colors.error
                    )
                ) {
                    Text("Remove")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onAction(
                            AdminCanteenAction.DismissRemoveClicked
                        )
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun AdminCanteenRow(
    canteen: AdminCanteenItem,
    onRemoveClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = colors.surfaceVariant,
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = canteen.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.onSurface
                )

                Spacer(Modifier.height(5.dp))

                Text(
                    text = if (canteen.storeCount == 1) {
                        "1 Store available"
                    } else {
                        "${canteen.storeCount} Stores available"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.onSurfaceVariant
                )
            }

            Button(
                onClick = onRemoveClick,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF970F0F),
                    contentColor = Color(0xFFDCB2FB)
                ),
                contentPadding = PaddingValues(
                    horizontal = 12.dp,
                    vertical = 8.dp
                ),
                modifier = Modifier.heightIn(min = 48.dp)
            ) {
                Text(
                    text = "Remove",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}