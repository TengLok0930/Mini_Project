package my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.items

sealed interface WorkerItemAction {

    data object OrdersClicked : WorkerItemAction

    data object CompletedOrdersClicked : WorkerItemAction

    data class EditItemClicked(
        val itemId: Int
    ) : WorkerItemAction

    data object ProfileClicked : WorkerItemAction
}