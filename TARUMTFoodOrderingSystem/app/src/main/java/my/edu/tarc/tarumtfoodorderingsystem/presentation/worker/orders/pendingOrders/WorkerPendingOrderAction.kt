package my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.orders.pendingOrders

sealed interface WorkerPendingOrderAction {

    data object CompletedOrdersClicked : WorkerPendingOrderAction

    data object ItemsClicked : WorkerPendingOrderAction

    data object ActiveOrdersClicked : WorkerPendingOrderAction

    data object PickupsClicked : WorkerPendingOrderAction

    data class AcceptingOrdersChanged(
        val accepting: Boolean
    ) : WorkerPendingOrderAction

    data class OrderClicked(
        val orderId: Int
    ) : WorkerPendingOrderAction

    data class ManageOrderClicked(
        val orderId: Int
    ) : WorkerPendingOrderAction

    data class AcceptOrderClicked(
        val orderId: Int
    ) : WorkerPendingOrderAction

    data class CancelOrderClicked(
        val orderId: Int
    ) : WorkerPendingOrderAction

    data object DismissOrderDialogClicked : WorkerPendingOrderAction

    data object ProfileClicked : WorkerPendingOrderAction
}