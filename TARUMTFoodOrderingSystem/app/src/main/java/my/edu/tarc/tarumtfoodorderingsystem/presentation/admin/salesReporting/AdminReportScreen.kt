package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.salesReporting

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.math.BigDecimal
import java.util.Locale
import my.edu.tarc.tarumtfoodorderingsystem.R
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppTopBar
import my.edu.tarc.tarumtfoodorderingsystem.ui.theme.LocalPriceColor

private enum class ReportDateTarget {
    START,
    END
}

@Composable
fun AdminReportScreen(
    uiState: AdminReportUiState,
    onAction: (AdminReportAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val listState = rememberLazyListState()
    var datePickerTarget by remember {
        mutableStateOf<ReportDateTarget?>(null)
    }

    val selectedStore = uiState.selectedStore
    val startMillis = uiState.startDateMillis
    val endMillis = uiState.endDateMillis
    val groupedItems = uiState.groupedSoldItems

    val rangeStart = startMillis?.let(::formatReportDate).orEmpty()
    val rangeEnd = endMillis?.let(::formatReportDate).orEmpty()

    val heading = when {
        selectedStore == null -> "Choose a store to view Reports"
        uiState.isReportVisible -> "Sales Report"
        else -> "Choose date or reports"
    }

    LaunchedEffect(uiState.selectedStoreId, uiState.isReportVisible) {
        listState.scrollToItem(0)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colors.background,
        topBar = {
            AppTopBar(
                title = "Sales Reporting",
                subtitle = selectedStore?.displayName,
                onBackClick = {
                    onAction(AdminReportAction.BackClicked)
                }
            )
        },
        floatingActionButton = {
            if (
                uiState.isReportVisible &&
                selectedStore != null &&
                startMillis != null &&
                endMillis != null
            ) {
                FloatingActionButton(
                    onClick = {
                        onAction(
                            AdminReportAction.SaveReportClicked(
                                storeId = selectedStore.id,
                                startDateMillis = startMillis,
                                endDateMillis = endMillis
                            )
                        )
                    },
                    containerColor = colors.primary,
                    contentColor = colors.onPrimary
                ) {
                    Icon(
                        painter = painterResource(R.drawable.save_48px),
                        contentDescription = "Save report",
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .widthIn(max = 560.dp)
                    .fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 22.dp,
                    end = 22.dp,
                    top = 24.dp,
                    // Keeps the last card clear of the save button.
                    bottom = if (uiState.isReportVisible) 104.dp else 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item(key = "heading", contentType = "heading") {
                    ReportSectionHeading(text = heading)
                }

                item(key = "store_selector", contentType = "selector") {
                    AdminReportStoreDropdown(
                        stores = uiState.stores,
                        selectedStore = selectedStore,
                        onStoreSelected = { storeId ->
                            onAction(
                                AdminReportAction.StoreSelected(
                                    storeId = storeId
                                )
                            )
                        }
                    )
                }

                if (selectedStore == null) {
                    if (uiState.stores.isEmpty()) {
                        item(key = "no_stores", contentType = "message") {
                            ReportMessage(
                                text = "No stores available to report on."
                            )
                        }
                    }
                } else {
                    item(key = "date_range", contentType = "dates") {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            ReportDateField(
                                label = "Start Date",
                                placeholder = "Select start date",
                                dateMillis = startMillis,
                                onClick = {
                                    datePickerTarget = ReportDateTarget.START
                                },
                                modifier = Modifier.weight(1f)
                            )

                            ReportDateField(
                                label = "End Date",
                                placeholder = "Select end date",
                                dateMillis = endMillis,
                                onClick = {
                                    datePickerTarget = ReportDateTarget.END
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    uiState.dateRangeError?.let { error ->
                        item(key = "date_error", contentType = "message") {
                            Text(
                                text = error,
                                style = MaterialTheme.typography.bodySmall,
                                color = colors.error
                            )
                        }
                    }

                    if (uiState.isReportVisible) {
                        item(key = "sales_heading", contentType = "heading") {
                            ReportSectionHeading(
                                text = "Sales",
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }

                        item(key = "sales_card", contentType = "sales") {
                            AdminSalesCard(
                                rangeLabel = "$rangeStart - $rangeEnd",
                                amountInSen = uiState.salesInSen
                            )
                        }

                        item(key = "items_heading", contentType = "heading") {
                            Column(modifier = Modifier.padding(top = 8.dp)) {
                                ReportSectionHeading(
                                    text = "Published Item Sold Count"
                                )

                                Spacer(Modifier.height(4.dp))

                                Text(
                                    text = "Units sold count for " +
                                            "$rangeStart to $rangeEnd",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colors.onSurfaceVariant
                                )
                            }
                        }

                        if (groupedItems.isEmpty()) {
                            item(
                                key = "no_published_items",
                                contentType = "message"
                            ) {
                                ReportMessage(
                                    text = "No published items for this store."
                                )
                            }
                        } else {
                            groupedItems.forEach { (category, categoryItems) ->
                                item(
                                    key = "category:$category",
                                    contentType = "category"
                                ) {
                                    ReportSectionHeading(
                                        text = "$category " +
                                                "(${categoryItems.size})",
                                        style = MaterialTheme.typography
                                            .titleLarge,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }

                                items(
                                    items = categoryItems,
                                    key = { item -> "sold_item:${item.id}" },
                                    contentType = { "sold_item" }
                                ) { item ->
                                    AdminSoldItemRow(item = item)
                                }
                            }
                        }
                    } else if (uiState.availableReports.isNotEmpty()) {
                        item(
                            key = "reports_heading",
                            contentType = "heading"
                        ) {
                            ReportSectionHeading(
                                text = "Available Reports",
                                modifier = Modifier.padding(top = 10.dp)
                            )
                        }

                        items(
                            items = uiState.availableReports,
                            key = { report ->
                                "report:${report.startDateMillis}"
                            },
                            contentType = { "report" }
                        ) { report ->
                            AdminReportTitleRow(
                                report = report,
                                onClick = {
                                    onAction(
                                        AdminReportAction.ViewReportClicked(
                                            storeId = selectedStore.id,
                                            startDateMillis =
                                                report.startDateMillis,
                                            endDateMillis =
                                                report.endDateMillis
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    datePickerTarget?.let { target ->
        ReportDatePickerDialog(
            initialDateMillis = when (target) {
                ReportDateTarget.START -> startMillis
                ReportDateTarget.END -> endMillis
            },
            onDateSelected = { dateMillis ->
                onAction(
                    when (target) {
                        ReportDateTarget.START ->
                            AdminReportAction.StartDateSelected(dateMillis)

                        ReportDateTarget.END ->
                            AdminReportAction.EndDateSelected(dateMillis)
                    }
                )
            },
            onDismiss = {
                datePickerTarget = null
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdminReportStoreDropdown(
    stores: List<AdminReportStoreOption>,
    selectedStore: AdminReportStoreOption?,
    onStoreSelected: (Int) -> Unit
) {
    val colors = MaterialTheme.colorScheme
    var expanded by remember { mutableStateOf(false) }
    val enabled = stores.isNotEmpty()

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = it && enabled
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            value = selectedStore?.displayName.orEmpty(),
            onValueChange = {},
            readOnly = true,
            enabled = enabled,
            singleLine = true,
            label = { Text("Store / Canteen") },
            placeholder = { Text("Select store") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            shape = RoundedCornerShape(14.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colors.surfaceVariant,
                unfocusedContainerColor = colors.surfaceVariant,
                disabledContainerColor = colors.surfaceVariant,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(
                    type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                    enabled = enabled
                )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.heightIn(max = 360.dp)
        ) {
            stores.forEach { store ->
                DropdownMenuItem(
                    text = { Text(store.displayName) },
                    onClick = {
                        expanded = false
                        onStoreSelected(store.id)
                    }
                )
            }
        }
    }
}

@Composable
private fun ReportDateField(
    label: String,
    placeholder: String,
    dateMillis: Long?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 12.dp
            )
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = colors.secondary
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = dateMillis?.let(::formatReportDate) ?: placeholder,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = if (dateMillis == null) {
                    colors.onSurfaceVariant
                } else {
                    colors.onSurface
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReportDatePickerDialog(
    initialDateMillis: Long?,
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val pickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDateMillis
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                enabled = pickerState.selectedDateMillis != null,
                onClick = {
                    pickerState.selectedDateMillis?.let(onDateSelected)
                    onDismiss()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = pickerState)
    }
}

@Composable
private fun AdminSalesCard(
    rangeLabel: String,
    amountInSen: Long
) {
    val colors = MaterialTheme.colorScheme

    val amount = remember(amountInSen) {
        String.format(
            Locale.US,
            "RM %,.2f",
            BigDecimal.valueOf(amountInSen, 2)
        )
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = colors.surfaceVariant
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "Sales ($rangeLabel)",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = colors.onSurface
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = amount,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = LocalPriceColor.current
            )
        }
    }
}

@Composable
private fun AdminSoldItemRow(
    item: AdminReportSoldItem
) {
    val colors = MaterialTheme.colorScheme

    val count = remember(item.soldCount) {
        String.format(Locale.US, "%,d", item.soldCount)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = colors.surfaceVariant
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = colors.onSurface
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Sold Count: $count",
                style = MaterialTheme.typography.bodySmall,
                color = LocalPriceColor.current
            )
        }
    }
}

@Composable
private fun AdminReportTitleRow(
    report: AdminAvailableReport,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = report.label,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = colors.onSurface,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "View >",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = colors.secondary
            )
        }
    }
}

@Composable
private fun ReportSectionHeading(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.titleLarge
) {
    Text(
        text = text,
        style = style,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.secondary,
        modifier = modifier
    )
}

@Composable
private fun ReportMessage(
    text: String
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}