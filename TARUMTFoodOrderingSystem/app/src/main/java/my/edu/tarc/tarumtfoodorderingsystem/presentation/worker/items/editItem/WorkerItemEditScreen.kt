package my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.items.editItem

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppActionButton
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppTopBar

@Composable
fun WorkerItemEditScreen(
    uiState: WorkerItemEditUiState,
    onAction: (WorkerItemEditAction) -> Unit,
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
                title = "Edit item",
                subtitle = uiState.storeSubtitle,
                onBackClick = {
                    onAction(
                        WorkerItemEditAction.BackClicked
                    )
                }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(
                        WindowInsets.navigationBars.only(
                            WindowInsetsSides.Bottom
                        )
                    )
                    .padding(
                        horizontal = 20.dp,
                        vertical = 20.dp
                    ),
                contentAlignment = Alignment.Center
            ) {
                AppActionButton(
                    title = "Save changes",
                    onClick = {
                        onAction(
                            WorkerItemEditAction.SaveChangesClicked
                        )
                    },
                    enabled = uiState.itemFound &&
                            !uiState.isSaving,
                    isLoading = uiState.isSaving,
                    modifier = Modifier
                        .widthIn(max = 560.dp)
                        .fillMaxWidth()
                )
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
            Column(
                modifier = Modifier
                    .widthIn(max = 560.dp)
                    .fillMaxSize()
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(
                        horizontal = 20.dp,
                        vertical = 28.dp
                    )
            ) {
                if (!uiState.itemFound) {
                    Text(
                        text = "Item not found",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurface
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "Return to Items and select an item again.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.onSurfaceVariant
                    )
                } else {
                    Text(
                        text = uiState.itemName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = colors.secondary
                    )

                    Spacer(Modifier.height(30.dp))

                    Text(
                        text = "Price (RM)",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.onSurfaceVariant
                    )

                    Spacer(Modifier.height(10.dp))

                    WorkerItemPriceInput(
                        uiState = uiState,
                        onAction = onAction
                    )

                    Spacer(Modifier.height(8.dp))

                    WorkerItemFieldMessage(
                        message = uiState.priceError
                            ?: "Enter a price or adjust in RM 0.50 steps.",
                        isError = uiState.priceError != null
                    )

                    Spacer(Modifier.height(28.dp))

                    Text(
                        text = "Stock available",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.onSurfaceVariant
                    )

                    Spacer(Modifier.height(10.dp))

                    OutlinedTextField(
                        value = uiState.stockInput,
                        onValueChange = { value ->
                            onAction(
                                WorkerItemEditAction.StockChanged(
                                    value = value
                                )
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !uiState.isSaving,
                        singleLine = true,
                        isError = uiState.stockError != null,
                        shape = RoundedCornerShape(16.dp),
                        textStyle = MaterialTheme.typography
                            .titleLarge.copy(
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold
                            ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = colors.surfaceVariant,
                            unfocusedContainerColor = colors.surfaceVariant,
                            disabledContainerColor = colors.surfaceVariant,
                            errorContainerColor = colors.surfaceVariant,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            disabledBorderColor = Color.Transparent
                        )
                    )

                    Spacer(Modifier.height(8.dp))

                    WorkerItemFieldMessage(
                        message = uiState.stockError
                            ?: "Enter the new stock quantity.",
                        isError = uiState.stockError != null
                    )

                    if (uiState.isLowStock) {
                        Spacer(Modifier.height(56.dp))

                        WorkerItemLowStockWarning(
                            itemName = uiState.itemName,
                            stockQuantity = uiState.stockQuantity ?: 0
                        )
                    }

                    uiState.saveError?.let { message ->
                        Spacer(Modifier.height(24.dp))

                        Text(
                            text = message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.error
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WorkerItemPriceInput(
    uiState: WorkerItemEditUiState,
    onAction: (WorkerItemEditAction) -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = colors.surfaceVariant
    ) {
        Row(
            modifier = Modifier.padding(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            WorkerPriceStepButton(
                text = "−",
                enabled = uiState.canDecreasePrice,
                onClick = {
                    onAction(
                        WorkerItemEditAction.DecreasePriceClicked
                    )
                }
            )

            TextField(
                value = uiState.priceInput,
                onValueChange = { value ->
                    onAction(
                        WorkerItemEditAction.PriceChanged(
                            value = value
                        )
                    )
                },
                modifier = Modifier.weight(1f),
                enabled = !uiState.isSaving,
                singleLine = true,
                isError = uiState.priceError != null,
                textStyle = MaterialTheme.typography
                    .titleLarge.copy(
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                )
            )

            WorkerPriceStepButton(
                text = "+",
                enabled = uiState.canIncreasePrice,
                onClick = {
                    onAction(
                        WorkerItemEditAction.IncreasePriceClicked
                    )
                }
            )
        }
    }
}

@Composable
private fun WorkerPriceStepButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    FilledTonalButton(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.size(48.dp),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = colors.onSurface.copy(
                alpha = 0.10f
            ),
            contentColor = colors.onSurface
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun WorkerItemFieldMessage(
    message: String,
    isError: Boolean
) {
    val colors = MaterialTheme.colorScheme

    Text(
        text = message,
        style = MaterialTheme.typography.bodySmall,
        color = if (isError) {
            colors.error
        } else {
            colors.onSurfaceVariant
        }
    )
}

@Composable
private fun WorkerItemLowStockWarning(
    itemName: String,
    stockQuantity: Int
) {
    val colors = MaterialTheme.colorScheme
    val isDarkBackground = colors.background.luminance() < 0.5f

    val containerColor = if (isDarkBackground) {
        Color(0xFF843638)
    } else {
        Color(0xFFFFDAD6)
    }

    val headingColor = if (isDarkBackground) {
        Color(0xFFFF9093)
    } else {
        Color(0xFF8C1D24)
    }

    val bodyColor = if (isDarkBackground) {
        Color(0xFFFFEEEE)
    } else {
        Color(0xFF5E171C)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = containerColor
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 20.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "LOW ON STOCK WARNING!",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = headingColor
            )

            Text(
                text = "Please restock $itemName!\n" +
                        "$stockQuantity units remaining.",
                style = MaterialTheme.typography.bodyMedium,
                color = bodyColor
            )
        }
    }
}