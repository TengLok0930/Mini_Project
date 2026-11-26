package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.salesReporting.reportDetails

import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.salesReporting.AdminReportSoldItem
import my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.salesReporting.adminReportSampleState
import my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.salesReporting.formatReportDate

private val UTC: TimeZone = TimeZone.getTimeZone("UTC")

data class AdminReportDetailUiState(
    val storeId: Int,
    val storeDisplayName: String,
    val reportTitle: String,
    val startDateMillis: Long,
    val endDateMillis: Long,
    val salesInSen: Long,
    val soldItemGroups: Map<String, List<AdminReportSoldItem>>
)

class AdminReportDetailViewModel(
    storeId: Int,
    startDateMillis: Long,
    endDateMillis: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        buildUiState(
            storeId = storeId,
            startDateMillis = startDateMillis,
            endDateMillis = endDateMillis
        )
    )

    val uiState = _uiState.asStateFlow()
}

/**
 * Frontend sample data only. Reuses the Sales Reporting sample figures
 * so both screens agree. Replace with the report repository later.
 */
private fun buildUiState(
    storeId: Int,
    startDateMillis: Long,
    endDateMillis: Long
): AdminReportDetailUiState {
    val source = adminReportSampleState().copy(
        selectedStoreId = storeId,
        startDateMillis = startDateMillis,
        endDateMillis = endDateMillis
    )

    return AdminReportDetailUiState(
        storeId = storeId,
        storeDisplayName = source.selectedStore?.displayName.orEmpty(),
        reportTitle = reportTitle(
            startDateMillis = startDateMillis,
            endDateMillis = endDateMillis
        ),
        startDateMillis = startDateMillis,
        endDateMillis = endDateMillis,
        salesInSen = source.salesInSen,
        soldItemGroups = source.groupedSoldItems
    )
}

/** "Sales for September 2026" for a whole month, else the date range. */
internal fun reportTitle(
    startDateMillis: Long,
    endDateMillis: Long
): String {
    val start = Calendar.getInstance(UTC).apply {
        timeInMillis = startDateMillis
    }

    val end = Calendar.getInstance(UTC).apply {
        timeInMillis = endDateMillis
    }

    val isWholeMonth =
        start.get(Calendar.DAY_OF_MONTH) == 1 &&
                start.get(Calendar.YEAR) == end.get(Calendar.YEAR) &&
                start.get(Calendar.MONTH) == end.get(Calendar.MONTH) &&
                end.get(Calendar.DAY_OF_MONTH) ==
                end.getActualMaximum(Calendar.DAY_OF_MONTH)

    if (isWholeMonth) {
        val monthName = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
            .apply { timeZone = UTC }
            .format(Date(startDateMillis))

        return "Sales for $monthName"
    }

    return "Sales for ${formatReportDate(startDateMillis)} - " +
            formatReportDate(endDateMillis)
}