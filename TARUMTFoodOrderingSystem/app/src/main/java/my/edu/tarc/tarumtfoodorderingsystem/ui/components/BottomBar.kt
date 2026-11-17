package my.edu.tarc.tarumtfoodorderingsystem.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import my.edu.tarc.tarumtfoodorderingsystem.R

enum class CustomerTab(
    val label: String,
    @DrawableRes val iconRes: Int
) {
    ORDERS(
        label = "Orders",
        iconRes = R.drawable.orders_24px
    ),
    HOME(
        label = "Home",
        iconRes = R.drawable.home_24px
    ),
    PROFILE(
        label = "Profile",
        iconRes = R.drawable.profile_24px
    )
}

@Composable
fun CustomerBottomBar(
    selectedTab: CustomerTab,
    onTabSelected: (CustomerTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    val itemColors = NavigationBarItemDefaults.colors(
        selectedIconColor = colors.primary,
        selectedTextColor = colors.primary,
        indicatorColor = colors.primaryContainer,
        unselectedIconColor = colors.onSurfaceVariant,
        unselectedTextColor = colors.onSurfaceVariant
    )

    NavigationBar(
        modifier = modifier,
        containerColor = colors.surface,
        tonalElevation = 0.dp
    ) {
        CustomerTab.values().forEach { tab ->
            NavigationBarItem(
                selected = selectedTab == tab,
                onClick = {
                        onTabSelected(tab)
                },
                icon = {
                    Icon(
                        painter = painterResource(tab.iconRes),
                        contentDescription = null,
                        modifier = Modifier.size(26.dp)
                    )
                },
                label = {
                    Text(tab.label)
                },
                colors = itemColors
            )
        }
    }
}