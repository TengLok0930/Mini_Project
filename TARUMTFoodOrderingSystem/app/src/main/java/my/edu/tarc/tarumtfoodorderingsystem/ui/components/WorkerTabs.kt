package my.edu.tarc.tarumtfoodorderingsystem.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

enum class WorkerNavigationTabItem(
    val title: String
) {
    ORDERS("Orders"),
    COMPLETED("Completed"),
    ITEMS("Items")
}

enum class WorkerOrdersTabItem(
    val title: String
) {
    PENDING("Pending"),
    ACTIVE("Active"),
    PICKUPS("Pickups")
}

@Composable
fun WorkerNavigationTab(
    selectedTab: WorkerNavigationTabItem,
    onTabSelected: (WorkerNavigationTabItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val tabs = WorkerNavigationTabItem.values()
    val selectedIndex = tabs.indexOf(selectedTab)

    TabRow(
        selectedTabIndex = selectedIndex,
        modifier = modifier,
        containerColor = colors.surfaceVariant,
        contentColor = colors.secondary,
        indicator = { positions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(
                    positions[selectedIndex]
                ),
                color = colors.secondary
            )
        }
    ) {
        tabs.forEach { tab ->
            Tab(
                selected = selectedTab == tab,
                onClick = {
                    onTabSelected(tab)
                },
                selectedContentColor = colors.secondary,
                unselectedContentColor = colors.onSurfaceVariant,
                text = {
                    Text(
                        text = tab.title,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            )
        }
    }
}

@Composable
fun WorkerOrdersTab(
    selectedTab: WorkerOrdersTabItem,
    onTabSelected: (WorkerOrdersTabItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val tabs = WorkerOrdersTabItem.values()

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = colors.surfaceVariant
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup()
                .padding(5.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            tabs.forEach { tab ->
                val selected = selectedTab == tab
                val tabShape = RoundedCornerShape(8.dp)

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(tabShape)
                        .selectable(
                            selected = selected,
                            onClick = {
                                onTabSelected(tab)
                            },
                            role = Role.Tab
                        ),
                    shape = tabShape,
                    color = if (selected) {
                        colors.primary
                    } else {
                        Color.Transparent
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .heightIn(min = 48.dp)
                            .padding(
                                horizontal = 8.dp,
                                vertical = 8.dp
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tab.title,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = if (selected) {
                                colors.onPrimary
                            } else {
                                colors.onSurfaceVariant
                            }
                        )
                    }
                }
            }
        }
    }
}