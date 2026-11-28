package my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.orders.pickupOrders

sealed interface WorkerPickupOrderAction {

    data object PendingOrdersClicked : WorkerPickupOrderAction

    data object ActiveOrdersClicked : WorkerPickupOrderAction

    data object CompletedOrdersClicked : WorkerPickupOrderAction

    data object ItemsClicked : WorkerPickupOrderAction

    data class AcceptingOrdersChanged(
        val accepting: Boolean
    ) : WorkerPickupOrderAction

    data class ConfirmCollectionClicked(
        val orderId: Int
    ) : WorkerPickupOrderAction

    data class OrderClicked(
        val orderId: Int
    ) : WorkerPickupOrderAction

    data object ProfileClicked : WorkerPickupOrderAction
}