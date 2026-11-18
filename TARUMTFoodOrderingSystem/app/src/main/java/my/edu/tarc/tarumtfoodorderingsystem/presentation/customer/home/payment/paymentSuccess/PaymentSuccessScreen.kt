package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.payment.paymentSuccess

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.math.BigDecimal
import java.util.Locale
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppActionButton
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppTopBar

@Composable
fun PaymentSuccessScreen(
    uiState: PaymentSuccessUiState,
    onAction: (PaymentSuccessAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    val isDarkBackground = colors.background.luminance() < 0.5f

    val successContainerColor = if (isDarkBackground) {
        Color(0xFF214536)
    } else {
        Color(0xFFD9F2E2)
    }

    val successHeadingColor = if (isDarkBackground) {
        Color(0xFF9FE3B3)
    } else {
        Color(0xFF1B6A32)
    }

    val successTextColor = if (isDarkBackground) {
        Color(0xFFEBFFF1)
    } else {
        Color(0xFF173D27)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colors.background,
        topBar = {
            AppTopBar(
                title = "Payment complete"
            )
        },
        bottomBar = {
            Surface(
                color = colors.background
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(
                            WindowInsets.navigationBars.only(
                                WindowInsetsSides.Bottom
                            )
                        )
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AppActionButton(
                        title = "View my orders",
                        onClick = {
                            onAction(
                                PaymentSuccessAction.ViewOrdersClicked
                            )
                        },
                        modifier = Modifier.widthIn(
                            max = 560.dp
                        )
                    )
                }
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
                    horizontal = 20.dp,
                    vertical = 28.dp
                ),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item(
                    key = "payment_success_card"
                ) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = successContainerColor
                    ) {
                        Column(
                            modifier = Modifier.padding(
                                horizontal = 20.dp,
                                vertical = 24.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "Payment successful",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = successHeadingColor
                            )

                            Text(
                                text = formatSuccessAmount(
                                    uiState.amountPaidInSen
                                ),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = successTextColor
                            )

                            Text(
                                text = "Your order has been sent to the store.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = successTextColor
                            )
                        }
                    }
                }

                item(
                    key = "pickup_heading"
                ) {
                    Text(
                        text = "Pickup details",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = colors.secondary,
                        modifier = Modifier.padding(
                            top = 8.dp
                        )
                    )
                }

                item(
                    key = "pickup_details"
                ) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = colors.surfaceVariant
                    ) {
                        Column(
                            modifier = Modifier.padding(
                                horizontal = 20.dp,
                                vertical = 24.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = uiState.storeSubtitle,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = colors.onSurface
                            )

                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Pick up number",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colors.onSurfaceVariant
                                )

                                Text(
                                    text = uiState.pickupNumber,
                                    style = MaterialTheme.typography.displaySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.onSurface
                                )
                            }

                            Text(
                                text = "Status: ${uiState.status.displayName}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = colors.secondary
                            )
                        }
                    }
                }

                item(
                    key = "pickup_message"
                ) {
                    Text(
                        text = "Track the status in Orders. Collect when ready.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.onSurfaceVariant
                    )
                }
            }
        }
    }
}

private fun formatSuccessAmount(
    amountInSen: Long
): String {
    return String.format(
        Locale.US,
        "RM %,.2f",
        BigDecimal.valueOf(amountInSen, 2)
    )
}