package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.orders

sealed interface OrdersAction {

    data object PurchaseHistoryClicked : OrdersAction

    data object OrdersClicked : OrdersAction

    data object HomeClicked : OrdersAction

    data object ProfileClicked : OrdersAction
}