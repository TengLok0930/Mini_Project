package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.salesReporting.editReport

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import my.edu.tarc.tarumtfoodorderingsystem.R
import my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.salesReporting.saveReport.AdminReportSaveType
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppActionButton
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppTopBar

@Composable
fun AdminEditReportScreen(
    uiState: AdminEditReportUiState,
    onAction: (AdminEditReportAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colors.background,
        topBar = {
            AppTopBar(
                title = "Sales Reporting",
                subtitle = uiState.storeDisplayName,
                onBackClick = {
                    onAction(AdminEditReportAction.BackClicked)
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
                    .imePadding()
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Report Editing",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = colors.secondary,
                            modifier = Modifier.weight(1f)
                        )

                        IconButton(
                            onClick = {
                                onAction(AdminEditReportAction.DeleteClicked)
                            }
                        ) {
                            Icon(
                                painter = painterResource(
                                    R.drawable.delete_40px
                                ),
                                contentDescription = "Delete report",
                                tint = colors.error,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }

                    TextField(
                        value = uiState.reportName,
                        onValueChange = { name ->
                            onAction(
                                AdminEditReportAction.ReportNameChanged(
                                    name = name
                                )
                            )
                        },
                        singleLine = true,
                        label = { Text("Report Name") },
                        placeholder = { Text("Enter Report Name") },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                            }
                        ),
                        shape = RoundedCornerShape(14.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = colors.surfaceVariant,
                            unfocusedContainerColor = colors.surfaceVariant,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = "Type to save",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = colors.secondary,
                        modifier = Modifier.padding(top = 10.dp)
                    )

                    AdminReportSaveType.entries.forEach { type ->
                        EditTypeRow(
                            label = type.displayName,
                            checked = type in uiState.selectedTypes,
                            onCheckedChange = { selected ->
                                onAction(
                                    AdminEditReportAction.TypeToggled(
                                        type = type,
                                        selected = selected
                                    )
                                )
                            }
                        )
                    }

                    if (uiState.selectedTypes.isEmpty()) {
                        Text(
                            text = "Choose at least one type to save.",
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.onSurfaceVariant
                        )
                    }
                }

                AppActionButton(
                    title = "Save Report",
                    onClick = {
                        onAction(AdminEditReportAction.SaveReportClicked)
                    },
                    enabled = uiState.canSave
                )
            }
        }
    }

    if (uiState.showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                onAction(AdminEditReportAction.DismissDeleteClicked)
            },
            title = {
                Text("Delete report?")
            },
            text = {
                Text(
                    text = "\"${uiState.reportName.trim()}\" will be " +
                            "permanently deleted. This cannot be undone."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onAction(AdminEditReportAction.ConfirmDeleteClicked)
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = colors.error
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onAction(AdminEditReportAction.DismissDeleteClicked)
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun EditTypeRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .toggleable(
                value = checked,
                role = Role.Checkbox,
                onValueChange = onCheckedChange
            )
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = null
        )

        Text(
            text = label,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}