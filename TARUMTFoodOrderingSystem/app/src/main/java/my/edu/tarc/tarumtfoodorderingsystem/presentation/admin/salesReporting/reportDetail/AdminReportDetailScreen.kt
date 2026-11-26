package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.salesReporting.reportDetails

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.math.BigDecimal
import java.util.Locale
import my.edu.tarc.tarumtfoodorderingsystem.R
import my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.salesReporting.AdminReportSoldItem
import my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.salesReporting.formatReportDate
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppTopBar
import my.edu.tarc.tarumtfoodorderingsystem.ui.theme.LocalPriceColor

@Composable
fun AdminReportDetailScreen(
    uiState: AdminReportDetailUiState,
    onAction: (AdminReportDetailAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    val rangeStart = formatReportDate(uiState.startDateMillis)
    val rangeEnd = formatReportDate(uiState.endDateMillis)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colors.background,
        topBar = {
            AppTopBar(
                title = "Sales Reporting",
                subtitle = uiState.storeDisplayName,
                onBackClick = {
                    onAction(AdminReportDetailAction.BackClicked)
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onAction(
                        AdminReportDetailAction.EditReportClicked(
                            storeId = uiState.storeId,
                            startDateMillis = uiState.startDateMillis,
                            endDateMillis = uiState.endDateMillis
                        )
                    )
                },
                containerColor = colors.primary,
                contentColor = colors.onPrimary
            ) {
                Icon(
                    painter = painterResource(R.drawable.edit_48px),
                    contentDescription = "Edit report",
                    modifier = Modifier.size(26.dp)
                )
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
                modifier = Modifier
                    .widthIn(max = 560.dp)
                    .fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    top = 24.dp,
                    // Keeps the last card clear of the button.
                    bottom = 104.dp
                ),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item(key = "title", contentType = "heading") {
                    DetailHeading(text = uiState.reportTitle)
                }

                item(key = "sales_heading", contentType = "heading") {
                    DetailHeading(text = "Sales")
                }

                item(key = "sales_card", contentType = "sales") {
                    DetailSalesCard(
                        rangeLabel = "$rangeStart - $rangeEnd",
                        amountInSen = uiState.salesInSen
                    )
                }

                item(key = "items_heading", contentType = "heading") {
                    Column(modifier = Modifier.padding(top = 8.dp)) {
                        DetailHeading(text = "Published Item Sold Count")

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = "Units sold count for " +
                                    "$rangeStart to $rangeEnd",
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.onSurfaceVariant
                        )
                    }
                }

                if (uiState.soldItemGroups.isEmpty()) {
                    item(key = "no_items", contentType = "message") {
                        Text(
                            text = "No published items for this store.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.onSurfaceVariant
                        )
                    }
                } else {
                    uiState.soldItemGroups.forEach { (category, categoryItems) ->
                        item(
                            key = "category:$category",
                            contentType = "category"
                        ) {
                            DetailHeading(
                                text = "$category (${categoryItems.size})",
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        items(
                            items = categoryItems,
                            key = { item -> "sold_item:${item.id}" },
                            contentType = { "sold_item" }
                        ) { item ->
                            DetailSoldItemRow(item = item)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailSalesCard(
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
private fun DetailSoldItemRow(
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
private fun DetailHeading(
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