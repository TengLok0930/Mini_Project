package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.salesReporting

import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.random.Random
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

private const val MILLIS_PER_DAY = 86_400_000L
private val UTC: TimeZone = TimeZone.getTimeZone("UTC")

data class AdminReportStoreOption(
    val id: Int,
    val name: String,
    val canteenName: String
) {
    val displayName: String
        get() = "$name · $canteenName"
}

data class AdminReportMenuItem(
    val id: Int,
    val storeId: Int,
    val name: String,
    val category: String,
    val isPublished: Boolean = true
)

data class AdminSalesEntry(
    val storeId: Int,
    val itemId: Int,
    val dateMillis: Long,
    val quantity: Long,
    val amountInSen: Long
)

data class AdminReportSoldItem(
    val id: Int,
    val storeId: Int,
    val name: String,
    val category: String,
    val soldCount: Long
)

/** A report title plus the date range it opens in Report Details. */
data class AdminAvailableReport(
    val label: String,
    val startDateMillis: Long,
    val endDateMillis: Long
)

data class AdminReportUiState(
    val stores: List<AdminReportStoreOption> = emptyList(),
    val menuItems: List<AdminReportMenuItem> = emptyList(),
    val salesEntries: List<AdminSalesEntry> = emptyList(),
    val availableReports: List<AdminAvailableReport> = emptyList(),
    val selectedStoreId: Int? = null,
    val startDateMillis: Long? = null,
    val endDateMillis: Long? = null
) {
    val selectedStore: AdminReportStoreOption?
        get() = stores.firstOrNull {
            it.id == selectedStoreId
        }

    val dateRangeError: String?
        get() {
            val start = startDateMillis
            val end = endDateMillis

            return if (start != null && end != null && end < start) {
                "End date must be on or after the start date."
            } else {
                null
            }
        }

    /** True once a store and a valid start/end range are chosen. */
    val isReportVisible: Boolean
        get() = selectedStore != null &&
                startDateMillis != null &&
                endDateMillis != null &&
                dateRangeError == null

    private val entriesInRange: List<AdminSalesEntry>
        get() {
            val storeId = selectedStoreId
            val start = startDateMillis
            val end = endDateMillis

            if (
                !isReportVisible ||
                storeId == null ||
                start == null ||
                end == null
            ) {
                return emptyList()
            }

            val endExclusive = end + MILLIS_PER_DAY

            return salesEntries.filter { entry ->
                entry.storeId == storeId &&
                        entry.dateMillis >= start &&
                        entry.dateMillis < endExclusive
            }
        }

    val salesInSen: Long
        get() = entriesInRange.sumOf { it.amountInSen }

    val publishedSoldItems: List<AdminReportSoldItem>
        get() {
            val storeId = selectedStoreId ?: return emptyList()

            val counts = entriesInRange
                .groupBy { it.itemId }
                .mapValues { (_, entries) ->
                    entries.sumOf { it.quantity }
                }

            return menuItems
                .filter { it.storeId == storeId && it.isPublished }
                .map { item ->
                    AdminReportSoldItem(
                        id = item.id,
                        storeId = item.storeId,
                        name = item.name,
                        category = item.category,
                        soldCount = counts[item.id] ?: 0L
                    )
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

            // Highest total units sold first, then alphabetical.
            val sortedCategories = grouped.entries.sortedWith(
                compareByDescending<
                        Map.Entry<String, List<AdminReportSoldItem>>
                        > { entry ->
                    entry.value.sumOf { it.soldCount }
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

                result[heading] = entry.value.sortedWith(
                    compareByDescending<AdminReportSoldItem> {
                        it.soldCount
                    }.thenBy {
                        it.name.lowercase(Locale.ROOT)
                    }.thenBy {
                        it.id
                    }
                )
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
                    if (state.stores.any { it.id == action.storeId }) {
                        state.copy(selectedStoreId = action.storeId)
                    } else {
                        state
                    }
                }
            }

            is AdminReportAction.StartDateSelected -> {
                _uiState.update {
                    it.copy(startDateMillis = action.dateMillis)
                }
            }

            is AdminReportAction.EndDateSelected -> {
                _uiState.update {
                    it.copy(endDateMillis = action.dateMillis)
                }
            }

            // Navigation is handled by AdminReportRoute.
            AdminReportAction.BackClicked,
            is AdminReportAction.ViewReportClicked,
            is AdminReportAction.SaveReportClicked -> Unit
        }
    }
}

/** Formats UTC-midnight millis (as returned by the date picker). */
internal fun formatReportDate(dateMillis: Long): String {
    return SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        .apply { timeZone = UTC }
        .format(Date(dateMillis))
}

private fun utcDate(
    year: Int,
    month: Int,
    day: Int
): Long {
    return Calendar.getInstance(UTC).apply {
        clear()
        set(year, month - 1, day)
    }.timeInMillis
}

private fun monthReport(
    year: Int,
    month: Int
): AdminAvailableReport {
    val calendar = Calendar.getInstance(UTC).apply {
        clear()
        set(year, month - 1, 1)
    }

    val start = calendar.timeInMillis
    calendar.set(
        Calendar.DAY_OF_MONTH,
        calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    )

    val monthName = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
        .apply { timeZone = UTC }
        .format(Date(start))

    return AdminAvailableReport(
        label = "Sales for $monthName",
        startDateMillis = start,
        endDateMillis = calendar.timeInMillis
    )
}

private data class SampleItemSpec(
    val item: AdminReportMenuItem,
    val priceInSen: Long,
    val minDaily: Int,
    val maxDaily: Int
)

private fun sampleItemSpecs(): List<SampleItemSpec> {
    return listOf(
        // Chicken Rice · Yum Yum Cafeteria
        SampleItemSpec(
            AdminReportMenuItem(1, 1, "Chicken Rice", "Rice"), 650, 0, 4
        ),
        SampleItemSpec(
            AdminReportMenuItem(2, 1, "Chicken Chop Rice", "Rice"), 750, 0, 3
        ),
        SampleItemSpec(
            AdminReportMenuItem(3, 1, "Ipoh Hor Fun", "Noodle"), 700, 0, 3
        ),
        SampleItemSpec(
            AdminReportMenuItem(4, 1, "Curry Mee", "Noodle"), 650, 0, 3
        ),
        SampleItemSpec(
            AdminReportMenuItem(7, 1, "Teh", "Drinks"), 320, 0, 5
        ),

        // Noodles · Yum Yum Cafeteria
        SampleItemSpec(
            AdminReportMenuItem(5, 2, "Wantan Mee", "Noodle"), 600, 0, 4
        ),
        SampleItemSpec(
            AdminReportMenuItem(6, 2, "Pan Mee", "Noodle"), 650, 0, 3
        ),

        // Western Food · Red Bricks Cafeteria
        SampleItemSpec(
            AdminReportMenuItem(9, 6, "Chicken Chop", "Western"), 1_100, 0, 3
        ),
        SampleItemSpec(
            AdminReportMenuItem(10, 6, "Fish and Chips", "Western"), 1_200, 0, 2
        ),
        SampleItemSpec(
            AdminReportMenuItem(11, 6, "Spaghetti Bolognese", "Pasta"), 1_000, 0, 2
        )

        // Claypot Rice · Heritage Kitchen intentionally has no items.
    )
}

/**
 * Random-looking but repeatable: a fixed seed gives the same numbers
 * on every launch. One entry per item per day, 1 Aug - 26 Nov 2026.
 */
private fun generateSampleSales(
    specs: List<SampleItemSpec>
): List<AdminSalesEntry> {
    val random = Random(2026)
    val lastDay = utcDate(2026, 11, 26)
    val entries = mutableListOf<AdminSalesEntry>()

    var day = utcDate(2026, 8, 1)

    while (day <= lastDay) {
        specs.forEach { spec ->
            val quantity = random.nextInt(
                spec.minDaily,
                spec.maxDaily + 1
            )

            if (quantity > 0) {
                entries += AdminSalesEntry(
                    storeId = spec.item.storeId,
                    itemId = spec.item.id,
                    dateMillis = day,
                    quantity = quantity.toLong(),
                    amountInSen = quantity * spec.priceInSen
                )
            }
        }

        day += MILLIS_PER_DAY
    }

    return entries
}

/**
 * Frontend sample data only.
 * Replace with shared store and order repository data later.
 */
internal fun adminReportSampleState(): AdminReportUiState {
    val specs = sampleItemSpecs()

    return AdminReportUiState(
        stores = listOf(
            AdminReportStoreOption(1, "Chicken Rice", "Yum Yum Cafeteria"),
            AdminReportStoreOption(2, "Noodles", "Yum Yum Cafeteria"),
            AdminReportStoreOption(6, "Western Food", "Red Bricks Cafeteria"),
            AdminReportStoreOption(8, "Claypot Rice", "Heritage Kitchen")
        ),
        menuItems = specs.map { it.item },
        salesEntries = generateSampleSales(specs),
        availableReports = listOf(
            monthReport(2026, 9),
            monthReport(2026, 10),
            monthReport(2026, 11)
        )
    )
}