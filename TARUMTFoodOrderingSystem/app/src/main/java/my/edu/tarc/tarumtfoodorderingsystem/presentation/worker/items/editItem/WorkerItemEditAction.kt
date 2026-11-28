package my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.items.editItem

sealed interface WorkerItemEditAction {

    data object BackClicked : WorkerItemEditAction

    data class PriceChanged(
        val value: String
    ) : WorkerItemEditAction

    data class StockChanged(
        val value: String
    ) : WorkerItemEditAction

    data object DecreasePriceClicked : WorkerItemEditAction

    data object IncreasePriceClicked : WorkerItemEditAction

    data object SaveChangesClicked : WorkerItemEditAction
}