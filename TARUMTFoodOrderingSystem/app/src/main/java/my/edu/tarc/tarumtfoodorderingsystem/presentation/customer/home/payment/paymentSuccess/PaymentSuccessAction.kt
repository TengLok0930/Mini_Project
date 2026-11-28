package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.payment.paymentSuccess

sealed interface PaymentSuccessAction {

    data object ViewOrdersClicked : PaymentSuccessAction
}