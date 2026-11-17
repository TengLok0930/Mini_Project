package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.cart

sealed interface CartAction {

    data object BackClicked : CartAction

    data object ProceedToPaymentClicked : CartAction

    data class ModifyItemClicked(
        val cartItemId: Int
    ) : CartAction

    data class QuantityChanged(
        val quantity: String
    ) : CartAction

    data class RemarksChanged(
        val remarks: String
    ) : CartAction

    data object SaveChangesClicked : CartAction

    data object DismissEditorClicked : CartAction

    data object RemoveEditingItemClicked : CartAction
}