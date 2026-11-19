package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.cart

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.menu.CustomerItemType
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.orderItem.DrinkOption
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.orderItem.OrderItemDraft
import kotlin.plus

val drinkOption: DrinkOption? = null
data class CustomerCartItem(
    val id: Int,
    val menuItemId: Int,
    val storeId: Int,
    val name: String,
    val unitPriceInSen: Long,
    val quantity: Int,
    val maximumQuantity: Int,
    val remarks: String = "",
    val imageUrl: String? = null,
    val imageRes: Int? = null,
    val drinkOption: DrinkOption? = null
) {
    val lineTotalInSen: Long
        get() = unitPriceInSen * quantity
}

data class CartUiState(
    val storeId: Int,
    val storeName: String,
    val canteenName: String,
    val items: List<CustomerCartItem> = emptyList(),
    val editingItemId: Int? = null,
    val editedQuantity: String = "",
    val editedRemarks: String = "",
    val quantityError: String? = null
) {
    val storeSubtitle: String
        get() = listOf(
            storeName,
            canteenName
        )
            .filter { it.isNotBlank() }
            .joinToString(" · ")

    val subtotalInSen: Long
        get() = items.sumOf {
            it.lineTotalInSen
        }

    val editingItem: CustomerCartItem?
        get() = items.firstOrNull {
            it.id == editingItemId
        }

    val canProceedToPayment: Boolean
        get() = items.isNotEmpty() &&
                items.all {
                    it.storeId == storeId &&
                            it.quantity in 1..9
                } &&
                items.groupBy {
                    it.menuItemId
                }.values.all { lines ->
                    lines.sumOf {
                        it.quantity
                    } <= lines.minOf {
                        it.maximumQuantity
                    }
                }
}

class CartViewModel(
    storeId: Int,
    storeName: String,
    canteenName: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        CartUiState(
            storeId = storeId,
            storeName = storeName,
            canteenName = canteenName,
            items = emptyList()
        )
    )

    val uiState = _uiState.asStateFlow()

    fun onAction(action: CartAction) {
        when (action) {
            is CartAction.ModifyItemClicked -> {
                startEditing(
                    cartItemId = action.cartItemId
                )
            }

            is CartAction.QuantityChanged -> {
                _uiState.update {
                    it.copy(
                        editedQuantity = action.quantity,
                        quantityError = null
                    )
                }
            }

            is CartAction.RemarksChanged -> {
                _uiState.update {
                    it.copy(
                        editedRemarks = action.remarks.take(240)
                    )
                }
            }

            CartAction.SaveChangesClicked -> {
                saveChanges()
            }

            CartAction.DismissEditorClicked -> {
                _uiState.update {
                    it.closeEditor()
                }
            }

            CartAction.RemoveEditingItemClicked -> {
                removeEditingItem()
            }

            CartAction.BackClicked,
            CartAction.ProceedToPaymentClicked -> {
                // Navigation is handled by CartRoute.
            }
        }
    }

    private fun startEditing(
        cartItemId: Int
    ) {
        _uiState.update { state ->
            val item = state.items.firstOrNull {
                it.id == cartItemId
            } ?: return@update state

            state.copy(
                editingItemId = item.id,
                editedQuantity = item.quantity.toString(),
                editedRemarks = item.remarks,
                quantityError = null
            )
        }
    }

    private fun saveChanges() {
        val state = _uiState.value
        val item = state.editingItem ?: return

        val quantity = state.editedQuantity
            .trim()
            .toIntOrNull()

        if (quantity == null || quantity < 1) {
            _uiState.update {
                it.copy(
                    quantityError = "Enter a quantity of at least 1."
                )
            }

            return
        }

        val quantityInOtherLines = state.items
            .filter {
                it.menuItemId == item.menuItemId &&
                        it.id != item.id
            }
            .sumOf {
                it.quantity
            }

        val maximumAllowed = minOf(
            9,
            (item.maximumQuantity - quantityInOtherLines)
                .coerceAtLeast(0)
        )

        if (quantity > maximumAllowed) {
            _uiState.update {
                it.copy(
                    quantityError =
                        "Maximum available quantity: $maximumAllowed."
                )
            }

            return
        }

        _uiState.update { currentState ->
            currentState.copy(
                items = currentState.items.map { cartItem ->
                    if (cartItem.id == item.id) {
                        cartItem.copy(
                            quantity = quantity,
                            remarks = currentState.editedRemarks.trim()
                        )
                    } else {
                        cartItem
                    }
                }
            ).closeEditor()
        }
    }

    private fun removeEditingItem() {
        _uiState.update { state ->
            val editingItemId = state.editingItemId
                ?: return@update state

            state.copy(
                items = state.items.filterNot {
                    it.id == editingItemId
                }
            ).closeEditor()
        }
    }

    // Front-end functions
    fun addItem(
        draft: OrderItemDraft
    ): Boolean {
        val state = _uiState.value
        val item = draft.item

        if (item.storeId != state.storeId) {
            return false
        }

        if (draft.quantity !in 1..9) {
            return false
        }

        if (draft.remarks.length > 240) {
            return false
        }

        if (
            item.itemType == CustomerItemType.BEVERAGE &&
            draft.drinkOption == null
        ) {
            return false
        }

        val quantityAlreadyInCart = state.items
            .filter {
                it.menuItemId == item.id
            }
            .sumOf {
                it.quantity
            }

        if (
            quantityAlreadyInCart + draft.quantity >
            item.quantityRemaining
        ) {
            return false
        }

        val nextId = (state.items.maxOfOrNull { it.id } ?: 0) + 1

        val cartItem = CustomerCartItem(
            id = nextId,
            menuItemId = item.id,
            storeId = item.storeId,
            name = item.name,
            unitPriceInSen = item.priceInSen,
            quantity = draft.quantity,
            maximumQuantity = item.quantityRemaining,
            remarks = draft.remarks.trim(),
            imageUrl = item.imageUrl,
            imageRes = item.imageRes,
            drinkOption = if (
                item.itemType == CustomerItemType.BEVERAGE
            ) {
                draft.drinkOption
            } else {
                null
            }
        )

        _uiState.update {
            it.copy(
                items = it.items + cartItem
            )
        }

        return true
    }
}

private fun CartUiState.closeEditor(): CartUiState {
    return copy(
        editingItemId = null,
        editedQuantity = "",
        editedRemarks = "",
        quantityError = null
    )
}

/**
 * Frontend fixtures only.
 *
 * These items are not yet populated by Menu or Item Details.
 * maximumQuantity is a sample stock limit, not live inventory.
 */
private fun createSampleCartItems(
    storeId: Int
): List<CustomerCartItem> {
    return listOf(
        CustomerCartItem(
            id = 1,
            menuItemId = 1,
            storeId = storeId,
            name = "Chicken Rice",
            unitPriceInSen = 650L,
            quantity = 1,
            maximumQuantity = 125,
            remarks = "Chicken Breast"
        ),
        CustomerCartItem(
            id = 2,
            menuItemId = 2,
            storeId = storeId,
            name = "Chicken Chop Rice",
            unitPriceInSen = 750L,
            quantity = 2,
            maximumQuantity = 110,
            remarks = "Thai sauce"
        )
    )
}