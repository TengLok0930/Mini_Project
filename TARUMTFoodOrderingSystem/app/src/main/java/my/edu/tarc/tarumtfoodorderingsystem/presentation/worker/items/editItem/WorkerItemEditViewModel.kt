package my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.items.editItem

import androidx.lifecycle.ViewModel
import java.math.BigDecimal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.items.WorkerMenuItem

data class WorkerItemEditValues(
    val itemId: Int,
    val priceInSen: Long,
    val availableQuantity: Int
)

data class WorkerItemEditUiState(
    val itemId: Int,
    val storeName: String,
    val canteenName: String,
    val itemName: String,
    val priceInput: String,
    val stockInput: String,
    val itemFound: Boolean,
    val showValidationErrors: Boolean = false,
    val isSaving: Boolean = false,
    val saveError: String? = null
) {
    val storeSubtitle: String
        get() = listOf(
            storeName,
            canteenName
        )
            .filter { it.isNotBlank() }
            .joinToString(" · ")

    val priceInSen: Long?
        get() = parsePriceInSen(priceInput)

    val stockQuantity: Int?
        get() = parseStockQuantity(stockInput)

    val isLowStock: Boolean
        get() = stockQuantity?.let {
            it < 70
        } ?: false

    val canDecreasePrice: Boolean
        get() = !isSaving && (
                priceInSen?.let {
                    it > 50L
                } ?: false
                )

    val canIncreasePrice: Boolean
        get() = !isSaving && (
                priceInSen?.let {
                    it <= Long.MAX_VALUE - 50L
                } ?: false
                )

    val priceError: String?
        get() = if (
            showValidationErrors &&
            priceInSen == null
        ) {
            "Enter a price greater than RM0.00 " +
                    "with up to 2 decimal places."
        } else {
            null
        }

    val stockError: String?
        get() = if (
            showValidationErrors &&
            stockQuantity == null
        ) {
            "Enter a valid whole-number stock quantity."
        } else {
            null
        }
}

class WorkerItemEditViewModel(
    itemId: Int,
    initialItem: WorkerMenuItem?,
    storeName: String,
    canteenName: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        WorkerItemEditUiState(
            itemId = itemId,
            storeName = storeName,
            canteenName = canteenName,
            itemName = initialItem?.name.orEmpty(),
            priceInput = initialItem?.let {
                formatEditablePrice(it.priceInSen)
            }.orEmpty(),
            stockInput = initialItem
                ?.availableQuantity
                ?.toString()
                .orEmpty(),
            itemFound = initialItem != null
        )
    )

    val uiState: StateFlow<WorkerItemEditUiState> =
        _uiState.asStateFlow()

    fun onAction(
        action: WorkerItemEditAction
    ) {
        if (_uiState.value.isSaving) {
            return
        }

        when (action) {
            is WorkerItemEditAction.PriceChanged -> {
                val validInput =
                    action.value.length <= 20 &&
                            PRICE_INPUT_PATTERN.matches(action.value)

                if (validInput) {
                    _uiState.update {
                        it.copy(
                            priceInput = action.value,
                            saveError = null
                        )
                    }
                }
            }

            is WorkerItemEditAction.StockChanged -> {
                val validInput =
                    action.value.length <= 10 &&
                            action.value.all { character ->
                                character in '0'..'9'
                            }

                if (validInput) {
                    _uiState.update {
                        it.copy(
                            stockInput = action.value,
                            saveError = null
                        )
                    }
                }
            }

            WorkerItemEditAction.DecreasePriceClicked -> {
                val state = _uiState.value
                val currentPrice = state.priceInSen ?: return

                if (state.canDecreasePrice) {
                    _uiState.update {
                        it.copy(
                            priceInput = formatEditablePrice(
                                currentPrice - 50L
                            ),
                            saveError = null
                        )
                    }
                }
            }

            WorkerItemEditAction.IncreasePriceClicked -> {
                val state = _uiState.value
                val currentPrice = state.priceInSen ?: return

                if (state.canIncreasePrice) {
                    _uiState.update {
                        it.copy(
                            priceInput = formatEditablePrice(
                                currentPrice + 50L
                            ),
                            saveError = null
                        )
                    }
                }
            }

            WorkerItemEditAction.BackClicked,
            WorkerItemEditAction.SaveChangesClicked -> {
                // Handled by the Route.
            }
        }
    }

    fun validateForSave(): WorkerItemEditValues? {
        val state = _uiState.value

        if (!state.itemFound || state.isSaving) {
            return null
        }

        val priceInSen = state.priceInSen
        val stockQuantity = state.stockQuantity

        _uiState.update {
            it.copy(
                showValidationErrors = true,
                saveError = null
            )
        }

        if (priceInSen == null || stockQuantity == null) {
            return null
        }

        _uiState.update {
            it.copy(
                isSaving = true
            )
        }

        return WorkerItemEditValues(
            itemId = state.itemId,
            priceInSen = priceInSen,
            availableQuantity = stockQuantity
        )
    }

    fun onSaveFailed() {
        _uiState.update {
            it.copy(
                isSaving = false,
                saveError = "The item could not be saved. " +
                        "Return to Items and try again."
            )
        }
    }
}

private val PRICE_INPUT_PATTERN =
    Regex("[0-9]*(\\.[0-9]{0,2})?")

private val COMPLETE_PRICE_PATTERN =
    Regex("[0-9]+(\\.[0-9]{1,2})?")

private fun parsePriceInSen(
    value: String
): Long? {
    if (!COMPLETE_PRICE_PATTERN.matches(value)) {
        return null
    }

    return runCatching {
        BigDecimal(value)
            .movePointRight(2)
            .longValueExact()
    }.getOrNull()?.takeIf {
        it > 0L
    }
}

private fun parseStockQuantity(
    value: String
): Int? {
    if (
        value.isEmpty() ||
        value.any { it !in '0'..'9' }
    ) {
        return null
    }

    return value.toIntOrNull()?.takeIf {
        it >= 0
    }
}

private fun formatEditablePrice(
    priceInSen: Long
): String {
    return BigDecimal.valueOf(
        priceInSen,
        2
    ).toPlainString()
}