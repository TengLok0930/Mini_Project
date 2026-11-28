package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.orders.purchaseHistory

sealed interface PurchaseHistoryAction {

    data object OrdersClicked : PurchaseHistoryAction

    data object PurchaseHistoryClicked : PurchaseHistoryAction

    data object HomeClicked : PurchaseHistoryAction

    data object ProfileClicked : PurchaseHistoryAction
}