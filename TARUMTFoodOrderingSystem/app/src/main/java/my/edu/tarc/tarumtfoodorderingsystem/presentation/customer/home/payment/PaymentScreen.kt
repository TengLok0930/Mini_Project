package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.payment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.cart.CustomerCartItem
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppActionButton
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppTopBar

@Composable
fun PaymentScreen(
    uiState: PaymentUiState,
    onAction: (PaymentAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .imePadding(),
        containerColor = colors.background,
        topBar = {
            AppTopBar(
                title = "Payment",
                subtitle = uiState.cart.storeSubtitle,
                onBackClick = {
                    onAction(PaymentAction.BackClicked)
                }
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
                        title = "Pay " + formatPaymentAmount(
                            uiState.totalToPayInSen
                        ),
                        enabled = uiState.canPay,
                        onClick = {
                            onAction(PaymentAction.PayClicked)
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
                    vertical = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                item(key = "summary_heading") {
                    PaymentSectionHeading(
                        title = "Order summary"
                    )
                }

                item(key = "order_summary") {
                    PaymentOrderSummary(
                        uiState = uiState
                    )
                }

                item(key = "voucher") {
                    Card(
                        onClick = {
                            onAction(
                                PaymentAction.OpenVoucherClicked
                            )
                        },
                        enabled = uiState.cart.items.isNotEmpty(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = colors.surfaceVariant,
                            contentColor = colors.onSurface
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 58.dp)
                                .padding(
                                    horizontal = 16.dp,
                                    vertical = 12.dp
                                ),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = when {
                                    uiState.appliedVoucher == null -> {
                                        "Add a voucher"
                                    }

                                    uiState.voucherIssue != null -> {
                                        "${uiState.appliedVoucher.code} — check voucher"
                                    }

                                    else -> {
                                        "${uiState.appliedVoucher.code} applied"
                                    }
                                },
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.weight(1f)
                            )

                            Text(
                                text = "›",
                                fontSize = 26.sp,
                                color = colors.secondary
                            )
                        }
                    }
                }

                val checkoutMessage =
                    uiState.voucherIssue ?: uiState.checkoutError

                if (checkoutMessage != null) {
                    item(key = "checkout_error") {
                        Text(
                            text = checkoutMessage,
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.error
                        )
                    }
                }

                item(key = "method_heading") {
                    PaymentSectionHeading(
                        title = "Payment method",
                        modifier = Modifier.padding(
                            top = 6.dp
                        )
                    )
                }

                item(key = "payment_methods") {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectableGroup(),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        PaymentMethod.values().forEach { method ->
                            PaymentMethodRow(
                                method = method,
                                selected = uiState.selectedMethod == method,
                                onClick = {
                                    onAction(
                                        PaymentAction.PaymentMethodSelected(
                                            method = method
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    if (uiState.showVoucherDialog) {
        PaymentVoucherDialog(
            uiState = uiState,
            onAction = onAction
        )
    }
}

@Composable
private fun PaymentSectionHeading(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.secondary,
        modifier = modifier
    )
}

@Composable
private fun PaymentOrderSummary(
    uiState: PaymentUiState
) {
    val colors = MaterialTheme.colorScheme

    val discountColor = if (colors.background.luminance() < 0.5f) {
        Color(0xFF9FE3B3)
    } else {
        Color(0xFF1B6A32)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = colors.surfaceVariant
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 20.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (uiState.cart.items.isEmpty()) {
                Text(
                    text = "Your cart is empty.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.onSurfaceVariant
                )
            }

            uiState.cart.items.forEach { item ->
                PaymentOrderLine(
                    item = item
                )
            }

            HorizontalDivider(
                color = colors.outlineVariant
            )

            PaymentAmountRow(
                label = "Subtotal",
                amount = formatPaymentAmount(
                    uiState.cart.subtotalInSen
                ),
                labelColor = colors.onSurfaceVariant,
                amountColor = colors.onSurface
            )

            if (
                uiState.appliedVoucher != null &&
                uiState.voucherIssue == null
            ) {
                PaymentAmountRow(
                    label = "${uiState.appliedVoucher.code} discount",
                    amount = "− " + formatPaymentAmount(
                        uiState.discountInSen
                    ),
                    labelColor = discountColor,
                    amountColor = discountColor
                )
            }

            PaymentAmountRow(
                label = "Total to pay",
                amount = formatPaymentAmount(
                    uiState.totalToPayInSen
                ),
                labelColor = colors.onSurface,
                amountColor = discountColor,
                emphasized = true
            )
        }
    }
}

@Composable
private fun PaymentOrderLine(
    item: CustomerCartItem
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "${item.quantity} × ${item.name}",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onSurface
            )

            item.drinkSummary?.let { summary ->
                Text(
                    text = "Drink: $summary",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.onSurfaceVariant
                )
            }

            if (item.remarks.isNotBlank()) {
                Text(
                    text = "Remarks: ${item.remarks}",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.onSurfaceVariant
                )
            }
        }

        Text(
            text = formatPaymentAmount(
                item.lineTotalInSen
            ),
            style = MaterialTheme.typography.bodyMedium,
            color = colors.onSurface
        )
    }
}

@Composable
private fun PaymentAmountRow(
    label: String,
    amount: String,
    labelColor: Color,
    amountColor: Color,
    emphasized: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = if (emphasized) {
                MaterialTheme.typography.titleMedium
            } else {
                MaterialTheme.typography.bodyMedium
            },
            fontWeight = if (emphasized) {
                FontWeight.Bold
            } else {
                FontWeight.Normal
            },
            color = labelColor,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = amount,
            style = if (emphasized) {
                MaterialTheme.typography.titleMedium
            } else {
                MaterialTheme.typography.bodyMedium
            },
            fontWeight = if (emphasized) {
                FontWeight.Bold
            } else {
                FontWeight.Normal
            },
            color = amountColor
        )
    }
}

@Composable
private fun PaymentMethodRow(
    method: PaymentMethod,
    selected: Boolean,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton
            ),
        shape = RoundedCornerShape(14.dp),
        color = colors.surfaceVariant,
        border = if (selected) {
            BorderStroke(
                width = 1.dp,
                color = colors.secondary
            )
        } else {
            null
        }
    ) {
        Row(
            modifier = Modifier
                .heightIn(min = 56.dp)
                .padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                ),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selected,
                onClick = null,
                modifier = Modifier.size(20.dp)
            )

            Text(
                text = method.displayName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (selected) {
                    FontWeight.Bold
                } else {
                    FontWeight.Normal
                },
                color = colors.onSurface,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun PaymentVoucherDialog(
    uiState: PaymentUiState,
    onAction: (PaymentAction) -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            onAction(
                PaymentAction.DismissVoucherClicked
            )
        },
        title = {
            Text("Add a voucher")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = uiState.voucherInput,
                    onValueChange = {
                        onAction(
                            PaymentAction.VoucherCodeChanged(
                                code = it
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Voucher code")
                    },
                    singleLine = true,
                    isError = uiState.voucherInputError != null,
                    supportingText = {
                        Text(
                            text = uiState.voucherInputError
                                ?: "Enter a code from My vouchers."
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onAction(
                                PaymentAction.ApplyVoucherClicked
                            )
                        }
                    )
                )

                if (uiState.appliedVoucher != null) {
                    TextButton(
                        onClick = {
                            onAction(
                                PaymentAction.RemoveVoucherClicked
                            )
                        }
                    ) {
                        Text(
                            text = "Remove current voucher",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                enabled = uiState.voucherInput.isNotBlank(),
                onClick = {
                    onAction(
                        PaymentAction.ApplyVoucherClicked
                    )
                }
            ) {
                Text("Apply")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onAction(
                        PaymentAction.DismissVoucherClicked
                    )
                }
            ) {
                Text("Cancel")
            }
        }
    )
}