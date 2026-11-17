package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.math.BigDecimal
import java.util.Locale
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.CustomerBottomBar
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.CustomerTab
import my.edu.tarc.tarumtfoodorderingsystem.R

@Composable
fun CustomerProfileScreen(
    uiState: CustomerProfileUiState,
    isDarkTheme: Boolean,
    onAction: (CustomerProfileAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    val formattedSpent = remember(uiState.totalSpentInSen) {
        String.format(
            Locale.US,
            "RM %,.2f",
            BigDecimal.valueOf(uiState.totalSpentInSen, 2)
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colors.background,
        topBar = {
            CustomerProfileTopBar(
                onLogoutClick = {
                    onAction(CustomerProfileAction.LogoutClicked)
                }
            )
        },
        bottomBar = {
            CustomerBottomBar(
                selectedTab = CustomerTab.PROFILE,
                onTabSelected = { tab ->
                    when (tab) {
                        CustomerTab.ORDERS -> {
                            onAction(CustomerProfileAction.OrdersClicked)
                        }

                        CustomerTab.HOME -> {
                            onAction(CustomerProfileAction.HomeClicked)
                        }

                        CustomerTab.PROFILE -> {
                            onAction(CustomerProfileAction.ProfileClicked)
                        }
                    }
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
                modifier = Modifier
                    .widthIn(max = 560.dp)
                    .fillMaxSize(),
                contentPadding = PaddingValues(
                    horizontal = 20.dp,
                    vertical = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                item(key = "account_heading") {
                    Text(
                        text = "My Account",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = colors.secondary
                    )
                }

                item(key = "account_card") {
                    ProfileInformationCard {
                        Text(
                            text = uiState.accountName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = colors.onSurface
                        )

                        Spacer(Modifier.height(10.dp))

                        Text(
                            text = uiState.accountRole,
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.onSurfaceVariant
                        )

                        Spacer(Modifier.height(12.dp))
                    }
                }

                item(key = "spending_card") {
                    ProfileInformationCard {
                        Text(
                            text = "Total amount spent",
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.onSurfaceVariant
                        )

                        Spacer(Modifier.height(12.dp))

                        Text(
                            text = formattedSpent,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = colors.onSurface
                        )

                        Spacer(Modifier.height(8.dp))

                        val orderLabel = if (uiState.ordersThisMonth == 1) {
                            "order"
                        } else {
                            "orders"
                        }

                        Text(
                            text = "This month: " +
                                    "${uiState.ordersThisMonth} $orderLabel",
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.secondary
                        )
                    }
                }

                item(key = "vouchers_card") {
                    Card(
                        onClick = {
                            onAction(
                                CustomerProfileAction.ViewVouchersClicked
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = colors.surfaceVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "My vouchers",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.onSurface
                                )

                                Spacer(Modifier.height(4.dp))

                                Text(
                                    text = "${uiState.availableVoucherCount} " +
                                            "available",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colors.onSurfaceVariant
                                )
                            }

                            Text(
                                text = "View ›",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = colors.secondary
                            )
                        }
                    }
                }

                item(key = "appearance_card") {
                    ProfileInformationCard {
                        Text(
                            text = "Appearance",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = colors.onSurface
                        )

                        Spacer(Modifier.height(14.dp))

                        ProfileAppearanceSelector(
                            isDarkTheme = isDarkTheme,
                            onDarkThemeChanged = { darkTheme ->
                                onAction(
                                    CustomerProfileAction.ThemeSelected(
                                        isDarkTheme = darkTheme
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    if (uiState.showLogoutConfirmation) {
        AlertDialog(
            onDismissRequest = {
                onAction(CustomerProfileAction.DismissLogoutClicked)
            },
            title = {
                Text("Log out?")
            },
            text = {
                Text("Are you sure you want to log out of your account?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onAction(CustomerProfileAction.ConfirmLogoutClicked)
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = colors.error
                    )
                ) {
                    Text("Log out")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onAction(CustomerProfileAction.DismissLogoutClicked)
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun CustomerProfileTopBar(
    onLogoutClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Surface(
        color = colors.primary,
        contentColor = colors.onPrimary
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .heightIn(min = 80.dp)
                .padding(
                    start = 20.dp,
                    end = 12.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Profile",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = onLogoutClick
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.logout_48px),
                    contentDescription = "Log out",
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
private fun ProfileInformationCard(
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

@Composable
private fun ProfileAppearanceSelector(
    isDarkTheme: Boolean,
    onDarkThemeChanged: (Boolean) -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(colors.background)
            .selectableGroup()
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        ProfileAppearanceOption(
            label = "Dark",
            selected = isDarkTheme,
            onClick = {
                onDarkThemeChanged(true)
            },
            modifier = Modifier.weight(1f)
        )

        ProfileAppearanceOption(
            label = "Light",
            selected = !isDarkTheme,
            onClick = {
                onDarkThemeChanged(false)
            },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ProfileAppearanceOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(9.dp))
            .background(
                if (selected) colors.primary else colors.background
            )
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick
            )
            .heightIn(min = 44.dp)
            .padding(
                horizontal = 12.dp,
                vertical = 10.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
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