package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.profile.voucher

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppTopBar
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.CustomerBottomBar
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.CustomerTab
import my.edu.tarc.tarumtfoodorderingsystem.ui.theme.TARUMTFoodOrderingSystemTheme

@Composable
fun VoucherScreen(
    uiState: VoucherUiState,
    onAction: (VoucherAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val vouchers = uiState.availableVouchers

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colors.background,
        topBar = {
            AppTopBar(
                title = "Vouchers",
                onBackClick = {
                    onAction(VoucherAction.BackClicked)
                }
            )
        },
        bottomBar = {
            CustomerBottomBar(
                selectedTab = CustomerTab.PROFILE,
                onTabSelected = { tab ->
                    when (tab) {
                        CustomerTab.ORDERS -> {
                            onAction(VoucherAction.OrdersClicked)
                        }

                        CustomerTab.HOME -> {
                            onAction(VoucherAction.HomeClicked)
                        }

                        CustomerTab.PROFILE -> {
                            onAction(VoucherAction.ProfileClicked)
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
                    vertical = 28.dp
                ),
                verticalArrangement = Arrangement.spacedBy(22.dp)
            ) {
                item(key = "voucher_heading") {
                    Column {
                        Text(
                            text = "Available vouchers",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = colors.secondary
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = "${vouchers.size} ready to use",
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.onSurfaceVariant
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = "Enter a voucher code at checkout to apply it.",
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.onSurfaceVariant
                        )
                    }
                }

                if (vouchers.isEmpty()) {
                    item(key = "vouchers_empty") {
                        Text(
                            text = "You have no available vouchers.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.onSurfaceVariant
                        )
                    }
                }

                items(
                    items = vouchers,
                    key = { voucher ->
                        voucher.id
                    }
                ) { voucher ->
                    CustomerVoucherCard(
                        voucher = voucher
                    )
                }
            }
        }
    }
}

@Composable
private fun CustomerVoucherCard(
    voucher: CustomerVoucher
) {
    val colors = MaterialTheme.colorScheme

    val minimumSpend = remember(voucher.minimumSpendInSen) {
        String.format(
            Locale.US,
            "RM %,.2f",
            BigDecimal.valueOf(voucher.minimumSpendInSen, 2)
        )
    }

    val validUntil = remember(voucher.expiresAtMillis) {
        // The expiry cutoff is exclusive.
        // One millisecond before it gives the last valid date.
        SimpleDateFormat(
            "d MMM yyyy",
            Locale.ENGLISH
        ).format(
            Date(voucher.expiresAtMillis - 1L)
        )
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = colors.surfaceVariant
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = voucher.type.displayName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = colors.secondary
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Min. spend $minimumSpend",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onSurfaceVariant
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = "Valid until $validUntil",
                style = MaterialTheme.typography.bodySmall,
                color = colors.onSurfaceVariant
            )

            Spacer(Modifier.height(20.dp))

            HorizontalDivider(
                color = colors.outlineVariant
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text = voucher.code,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colors.onSurface
            )

            Spacer(Modifier.height(12.dp))
        }
    }
}