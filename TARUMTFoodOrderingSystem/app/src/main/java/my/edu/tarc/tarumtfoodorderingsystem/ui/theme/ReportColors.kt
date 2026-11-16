package my.edu.tarc.tarumtfoodorderingsystem.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class ReportColors(
    val previousMonth: Color,
    val thisMonth: Color,
    val overall: Color
)

val LightReportColors = ReportColors(
    previousMonth = ReportPreviousMonthLight,
    thisMonth = ReportThisMonthLight,
    overall = ReportOverallLight
)

val DarkReportColors = ReportColors(
    previousMonth = ReportPreviousMonthDark,
    thisMonth = ReportThisMonthDark,
    overall = ReportOverallDark
)

val LocalReportColors = staticCompositionLocalOf {
    LightReportColors
}