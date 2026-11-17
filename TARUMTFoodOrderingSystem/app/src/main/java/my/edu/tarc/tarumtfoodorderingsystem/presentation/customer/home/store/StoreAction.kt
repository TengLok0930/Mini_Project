package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.store

sealed interface StoreAction {

    data object BackClicked : StoreAction

    data class StoreClicked(
        val storeId: Int
    ) : StoreAction

    data object OrdersClicked : StoreAction

    data object HomeClicked : StoreAction

    data object ProfileClicked : StoreAction
}