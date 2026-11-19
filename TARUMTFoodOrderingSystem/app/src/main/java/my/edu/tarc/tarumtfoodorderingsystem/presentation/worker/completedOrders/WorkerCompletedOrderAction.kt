package my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.completedOrders

sealed interface WorkerCompletedOrderAction {

    data class SearchQueryChanged(
        val query: String
    ) : WorkerCompletedOrderAction

    data object OrdersClicked : WorkerCompletedOrderAction

    data object ItemsClicked : WorkerCompletedOrderAction

    data object LogoutClicked : WorkerCompletedOrderAction

    data object ConfirmLogoutClicked : WorkerCompletedOrderAction

    data object DismissLogoutClicked : WorkerCompletedOrderAction
}