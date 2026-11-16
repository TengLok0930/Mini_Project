package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.orderReporting

import androidx.lifecycle.ViewModel
import java.util.Locale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AdminReportStoreOption(
    val id: Int,
    val name: String,
    val canteenName: String
) {
    val displayName: String
        get() = "$name · $canteenName"
}

data class AdminFinancialSummary(
    val previousMonthSalesInSen: Long = 0L,
    val thisMonthSalesInSen: Long = 0L,
    val overallSalesInSen: Long = 0L
)

data class AdminReportSoldItem(
    val id: Int,
    val storeId: Int,
    val name: String,
    val category: String,
    val soldCount: Long,
    val isPublished: Boolean = true
)

data class AdminReportUiState(
    val stores: List<AdminReportStoreOption> = emptyList(),
    val financialReports: Map<Int, AdminFinancialSummary> = emptyMap(),
    val soldItems: List<AdminReportSoldItem> = emptyList(),
    val selectedStoreId: Int? = null
) {
    val selectedStore: AdminReportStoreOption?
        get() = stores.firstOrNull {
            it.id == selectedStoreId
        }

    val selectedFinancialReport: AdminFinancialSummary?
        get() = selectedStore?.let {
            financialReports[it.id]
        }

    val publishedSoldItems: List<AdminReportSoldItem>
        get() = if (selectedStore == null) {
            emptyList()
        } else {
            soldItems.filter {
                it.storeId == selectedStoreId && it.isPublished
            }
        }

    val groupedSoldItems: Map<String, List<AdminReportSoldItem>>
        get() {
            fun cleanCategory(value: String): String {
                return value
                    .trim()
                    .replace(Regex("""\s+"""), " ")
                    .ifBlank { "Uncategorized" }
            }

            val grouped = publishedSoldItems.groupBy { item ->
                cleanCategory(item.category).lowercase(Locale.ROOT)
            }

            // Highest total units sold first.
            // Categories with equal totals are alphabetical.
            val sortedCategories = grouped.entries.sortedWith(
                compareByDescending<
                        Map.Entry<String, List<AdminReportSoldItem>>
                        > { entry ->
                    entry.value.sumOf { item ->
                        item.soldCount
                    }
                }.thenBy { entry ->
                    entry.key
                }
            )

            val result = linkedMapOf<String, List<AdminReportSoldItem>>()

            sortedCategories.forEach { entry ->
                val heading = cleanCategory(
                    entry.value.first().category
                ).replaceFirstChar {
                    it.titlecase(Locale.ROOT)
                }

                // Best-selling items first within this category.
                val sortedItems = entry.value.sortedWith(
                    compareByDescending<AdminReportSoldItem> {
                        it.soldCount
                    }.thenBy {
                        it.name.lowercase(Locale.ROOT)
                    }.thenBy {
                        it.id
                    }
                )

                result[heading] = sortedItems
            }

            return result
        }
    }


class AdminReportViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        adminReportSampleState()
    )

    val uiState = _uiState.asStateFlow()

    fun onAction(action: AdminReportAction) {
        when (action) {
            is AdminReportAction.StoreSelected -> {
                _uiState.update { state ->
                    if (
                        state.stores.any {
                            it.id == action.storeId
                        }
                    ) {
                        state.copy(
                            selectedStoreId = action.storeId
                        )
                    } else {
                        state
                    }
                }
            }

            // Handled by AdminReportRoute.
            AdminReportAction.BackClicked -> Unit
        }
    }
}

/**
 * Frontend sample data only.
 * Replace with shared store and order repository data later.
 */
internal fun adminReportSampleState(): AdminReportUiState {
    return AdminReportUiState(
        stores = listOf(
            AdminReportStoreOption(
                id = 1,
                name = "Chicken Rice",
                canteenName = "Yum Yum Cafeteria"
            ),
            AdminReportStoreOption(
                id = 2,
                name = "Noodles",
                canteenName = "Yum Yum Cafeteria"
            ),
            AdminReportStoreOption(
                id = 6,
                name = "Western Food",
                canteenName = "Red Bricks Cafeteria"
            ),
            AdminReportStoreOption(
                id = 8,
                name = "Claypot Rice",
                canteenName = "Heritage Kitchen"
            )
        ),
        financialReports = mapOf(
            1 to AdminFinancialSummary(
                previousMonthSalesInSen = 650_000L,
                thisMonthSalesInSen = 340_000L,
                overallSalesInSen = 2_000_000L
            ),
            2 to AdminFinancialSummary(
                previousMonthSalesInSen = 420_000L,
                thisMonthSalesInSen = 215_000L,
                overallSalesInSen = 1_450_000L
            ),
            6 to AdminFinancialSummary(
                previousMonthSalesInSen = 780_000L,
                thisMonthSalesInSen = 395_000L,
                overallSalesInSen = 2_850_000L
            ),
            8 to AdminFinancialSummary()
        ),
        soldItems = listOf(
            AdminReportSoldItem(
                id = 1,
                storeId = 1,
                name = "Chicken Rice",
                category = "Rice",
                soldCount = 342L
            ),
            AdminReportSoldItem(
                id = 2,
                storeId = 1,
                name = "Chicken Chop Rice",
                category = "Rice",
                soldCount = 232L
            ),
            AdminReportSoldItem(
                id = 3,
                storeId = 1,
                name = "Ipoh Hor Fun",
                category = "Noodle",
                soldCount = 74L
            ),
            AdminReportSoldItem(
                id = 4,
                storeId = 1,
                name = "Curry Mee",
                category = "Noodle",
                soldCount = 118L
            ),
            AdminReportSoldItem(
                id = 5,
                storeId = 2,
                name = "Wantan Mee",
                category = "Noodle",
                soldCount = 215L
            ),
            AdminReportSoldItem(
                id = 6,
                storeId = 2,
                name = "Pan Mee",
                category = "Noodle",
                soldCount = 168L
            ),
            AdminReportSoldItem(
                id = 9,
                storeId = 6,
                name = "Chicken Chop",
                category = "Western",
                soldCount = 184L
            ),
            AdminReportSoldItem(
                id = 10,
                storeId = 6,
                name = "Fish and Chips",
                category = "Western",
                soldCount = 96L
            )

            // Claypot Rice intentionally has no published items.
        )
    )
}