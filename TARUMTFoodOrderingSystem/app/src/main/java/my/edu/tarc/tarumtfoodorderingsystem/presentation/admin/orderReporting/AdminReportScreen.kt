package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.orderReporting

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.math.BigDecimal
import java.util.Locale
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppTopBar
import my.edu.tarc.tarumtfoodorderingsystem.ui.theme.LocalPriceColor
import my.edu.tarc.tarumtfoodorderingsystem.ui.theme.LocalReportColors
import my.edu.tarc.tarumtfoodorderingsystem.ui.theme.TARUMTFoodOrderingSystemTheme

@Composable
fun AdminReportScreen(
    uiState: AdminReportUiState,
    onAction: (AdminReportAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val listState = rememberLazyListState()

    val selectedStore = uiState.selectedStore
    val financialReport = uiState.selectedFinancialReport
    val groupedItems = uiState.groupedSoldItems

    LaunchedEffect(uiState.selectedStoreId) {
        listState.scrollToItem(0)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colors.background,
        topBar = {
            AppTopBar(
                title = "Order Reporting",
                subtitle = selectedStore?.displayName,
                onBackClick = {
                    onAction(AdminReportAction.BackClicked)
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
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .widthIn(max = 560.dp)
                    .fillMaxSize(),
                contentPadding = PaddingValues(
                    horizontal = 22.dp,
                    vertical = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item(
                    key = "store_selector",
                    contentType = "selector"
                ) {
                    Column {
                        ReportSectionHeading(
                            text = "Choose a store to view sales"
                        )

                        Spacer(Modifier.height(16.dp))

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
                }

                if (selectedStore == null) {
                    if (uiState.stores.isEmpty()) {
                        item(
                            key = "no_stores",
                            contentType = "message"
                        ) {
                            ReportMessage(
                                text = "No stores available to report on."
                            )
                        }
                    }
                } else {
                    item(
                        key = "financial_report",
                        contentType = "financial_report"
                    ) {
                        Column(
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            ReportSectionHeading(
                                text = "Financial Report"
                            )

                            Spacer(Modifier.height(12.dp))

                            if (financialReport == null) {
                                ReportMessage(
                                    text = "Financial report unavailable."
                                )
                            } else {
                                AdminFinancialReport(
                                    report = financialReport
                                )
                            }
                        }
                    }

                    item(
                        key = "published_items_heading",
                        contentType = "heading"
                    ) {
                        Column(
                            modifier = Modifier.padding(top = 14.dp)
                        ) {
                            ReportSectionHeading(
                                text = "Published Item Sold Count"
                            )

                            Spacer(Modifier.height(4.dp))

                            Text(
                                text = "All-time units sold",
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
                        groupedItems.forEach { (category, items) ->
                            item(
                                key = "category:$category",
                                contentType = "category"
                            ) {
                                ReportSectionHeading(
                                    text = "$category (${items.size})",
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }

                            items(
                                items = items,
                                key = { item ->
                                    "sold_item:${item.id}"
                                },
                                contentType = {
                                    "sold_item"
                                }
                            ) { item ->
                                AdminSoldItemRow(
                                    item = item
                                )
                            }
                        }
                    }
                }
            }
        }
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
            label = {
                Text("Store / Canteen")
            },
            placeholder = {
                Text("Select store")
            },
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
            onDismissRequest = {
                expanded = false
            },
            modifier = Modifier.heightIn(max = 360.dp)
        ) {
            stores.forEach { store ->
                DropdownMenuItem(
                    text = {
                        Text(store.displayName)
                    },
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
private fun AdminFinancialReport(
    report: AdminFinancialSummary
) {
    val reportColors = LocalReportColors.current

    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        FinancialReportRow(
            label = "Previous Month’s Sales",
            amountInSen = report.previousMonthSalesInSen,
            color = reportColors.previousMonth
        )

        FinancialReportRow(
            label = "This Month’s Sales",
            amountInSen = report.thisMonthSalesInSen,
            color = reportColors.thisMonth
        )

        FinancialReportRow(
            label = "Sales Overall",
            amountInSen = report.overallSalesInSen,
            color = reportColors.overall
        )
    }
}

@Composable
private fun FinancialReportRow(
    label: String,
    amountInSen: Long,
    color: Color
) {
    val amount = remember(amountInSen) {
        String.format(
            Locale.US,
            "RM %,.2f",
            BigDecimal.valueOf(amountInSen, 2)
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = color,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = amount,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = color
        )
    }
}

@Composable
private fun AdminSoldItemRow(
    item: AdminReportSoldItem
) {
    val colors = MaterialTheme.colorScheme

    val count = remember(item.soldCount) {
        String.format(
            Locale.US,
            "%,d",
            item.soldCount
        )
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = colors.surfaceVariant
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
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
private fun ReportSectionHeading(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
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