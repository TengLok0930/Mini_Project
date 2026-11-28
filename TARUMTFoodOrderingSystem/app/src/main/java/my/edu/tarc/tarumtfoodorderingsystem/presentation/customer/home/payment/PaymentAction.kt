package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.payment

sealed interface PaymentAction {

    data object BackClicked : PaymentAction

    data class PaymentMethodSelected(
        val method: PaymentMethod
    ) : PaymentAction

    data object OpenVoucherClicked : PaymentAction

    data object DismissVoucherClicked : PaymentAction

    data class VoucherCodeChanged(
        val code: String
    ) : PaymentAction

    data object ApplyVoucherClicked : PaymentAction

    data object RemoveVoucherClicked : PaymentAction

    data object PayClicked : PaymentAction
}