package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.orderItem

sealed interface OrderItemAction {

    data object BackClicked : OrderItemAction

    data object IncreaseQuantityClicked : OrderItemAction

    data object DecreaseQuantityClicked : OrderItemAction

    data class DrinkOptionSelected(
        val option: DrinkOption
    ) : OrderItemAction

    data class RemarksChanged(
        val remarks: String
    ) : OrderItemAction

    data object OrderNowClicked : OrderItemAction
}