package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.orderItem

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.menu.CustomerItemType
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.menu.CustomerMenuItem

const val TAKEAWAY_SURCHARGE_IN_SEN = 70L

enum class DrinkOption(
    val displayName: String,
    val surchargeInSen: Long
) {
    HOT("Hot", 0L),
    COLD("Cold", 30L)
}

data class OrderItemDraft(
    val item: CustomerMenuItem,
    val quantity: Int,
    val remarks: String,
    val drinkOption: DrinkOption?,
    val takeaway: Boolean,
    val unitPriceInSen: Long
) {
    val drinkSummary: String?
        get() = buildList {
            drinkOption?.let { add(it.displayName) }
            if (takeaway) add ("Takeaway")
        }.takeIf { it.isNotEmpty() }
            ?.joinToString(" · ")

    val lineTotalInSen: Long
        get() = unitPriceInSen * quantity
}

data class OrderItemUiState(
    val item: CustomerMenuItem,
    val availableQuantity: Int,
    val quantity: Int = 1,
    val selectedDrinkOption: DrinkOption? = null,
    val takeaway: Boolean = false,
    val remarks: String = ""
) {
    val isBeverage: Boolean
        get() = item.itemType == CustomerItemType.BEVERAGE

    val maximumQuantity: Int
        get() = minOf(
            9,
            availableQuantity.coerceAtLeast(0)
        )

    val unitPriceInSen: Long
        get() = item.priceInSen +
                (selectedDrinkOption?.surchargeInSen ?: 0L) +
                if (isBeverage && takeaway) {
                    TAKEAWAY_SURCHARGE_IN_SEN
                } else {
                    0L
                }

    val totalInSen: Long
        get() = unitPriceInSen * quantity

    val canOrder: Boolean
        get() = quantity in 1..maximumQuantity &&
                (!isBeverage || selectedDrinkOption != null)
}

class OrderItemViewModel(
    item: CustomerMenuItem
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        OrderItemUiState(
            item = item,
            availableQuantity = item.quantityRemaining,
            quantity = if (item.isAvailable) 1 else 0
        )
    )

    val uiState = _uiState.asStateFlow()

    fun updateAvailableQuantity(
        availableQuantity: Int
    ) {
        _uiState.update { state ->
            val available = availableQuantity.coerceAtLeast(0)
            val maximum = minOf(9, available)

            state.copy(
                availableQuantity = available,
                quantity = if (maximum > 0) {
                    state.quantity.coerceIn(1, maximum)
                } else {
                    0
                }
            )
        }
    }

    fun onAction(action: OrderItemAction) {
        when (action) {
            OrderItemAction.IncreaseQuantityClicked -> {
                _uiState.update { state ->
                    if (state.quantity < state.maximumQuantity) {
                        state.copy(
                            quantity = state.quantity + 1
                        )
                    } else {
                        state
                    }
                }
            }

            OrderItemAction.DecreaseQuantityClicked -> {
                _uiState.update { state ->
                    if (state.quantity > 1) {
                        state.copy(
                            quantity = state.quantity - 1
                        )
                    } else {
                        state
                    }
                }
            }

            is OrderItemAction.DrinkOptionSelected -> {
                _uiState.update { state ->
                    if (state.isBeverage) {
                        state.copy(
                            selectedDrinkOption = action.option
                        )
                    } else {
                        state
                    }
                }
            }

            is OrderItemAction.TakeawayChanged -> {
                _uiState.update { state ->
                    if (state.isBeverage) {
                        state.copy(
                            takeaway = action.takeaway
                        )
                    } else {
                        state
                    }
                }
            }

            is OrderItemAction.RemarksChanged -> {
                _uiState.update {
                    it.copy(
                        remarks = action.remarks.take(240)
                    )
                }
            }

            OrderItemAction.BackClicked,
            OrderItemAction.OrderNowClicked -> {
                // Handled by OrderItemRoute.
            }
        }
    }

    fun createDraft(): OrderItemDraft? {
        val state = _uiState.value

        if (!state.canOrder) {
            return null
        }

        return OrderItemDraft(
            item = state.item,
            quantity = state.quantity,
            remarks = state.remarks.trim(),
            drinkOption = if (state.isBeverage) {
                state.selectedDrinkOption
            } else {
                null
            },
            takeaway = state.isBeverage && state.takeaway,
            unitPriceInSen = state.unitPriceInSen
        )
    }
}