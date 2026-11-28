package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.salesReporting.saveReport

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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppTopBar

@Composable
fun AdminSaveReportScreen(
    uiState: AdminSaveReportUiState,
    onAction: (AdminSaveReportAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val keyboardController = LocalSoftwareKeyboardController.current

    val fieldColors = TextFieldDefaults.colors(
        focusedContainerColor = colors.surfaceVariant,
        unfocusedContainerColor = colors.surfaceVariant,
        disabledContainerColor = colors.surfaceVariant,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colors.background,
        topBar = {
            AppTopBar(
                title = "Sales Reporting",
                subtitle = uiState.storeDisplayName,
                onBackClick = {
                    onAction(AdminSaveReportAction.BackClicked)
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
                    Text(
                        text = "Save Report",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = colors.secondary
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        TextField(
                            value = uiState.reportName,
                            onValueChange = { name ->
                                onAction(
                                    AdminSaveReportAction.ReportNameChanged(
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
                            colors = fieldColors,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Text(
                            text = "${uiState.reportName.length}/" +
                                    "$MAX_SAVED_REPORT_NAME_LENGTH",
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.onSurfaceVariant,
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 4.dp)
                        )
                    }

                    TextField(
                        value = uiState.storeDisplayName,
                        onValueChange = {},
                        readOnly = true,
                        singleLine = true,
                        label = { Text("Store / Canteen") },
                        shape = RoundedCornerShape(14.dp),
                        colors = fieldColors,
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
                        SaveTypeRow(
                            label = type.displayName,
                            checked = type in uiState.selectedTypes,
                            onCheckedChange = { selected ->
                                onAction(
                                    AdminSaveReportAction.TypeToggled(
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

                Button(
                    onClick = {
                        onAction(AdminSaveReportAction.SaveReportClicked)
                    },
                    enabled = uiState.canSave,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 52.dp)
                ) {
                    Text(
                        text = "Save Report",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun SaveTypeRow(
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