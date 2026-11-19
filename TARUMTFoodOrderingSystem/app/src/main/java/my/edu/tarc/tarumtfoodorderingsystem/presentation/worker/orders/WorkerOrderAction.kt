package my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.orders

sealed interface WorkerOrderAction {

    data object PendingOrdersClicked : WorkerOrderAction

    data object PickupsClicked : WorkerOrderAction

    data object CompletedOrdersClicked : WorkerOrderAction

    data object ItemsClicked : WorkerOrderAction

    data class AcceptingOrdersChanged(
        val accepting: Boolean
    ) : WorkerOrderAction

    data class OrderClicked(
        val orderId: Int
    ) : WorkerOrderAction

    data class MarkReadyForPickupClicked(
        val orderId: Int
    ) : WorkerOrderAction

    data object LogoutClicked : WorkerOrderAction

    data object ConfirmLogoutClicked : WorkerOrderAction

    data object DismissLogoutClicked : WorkerOrderAction
}