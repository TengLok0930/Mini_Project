package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.canteen

sealed interface CanteenAction {

    data class CanteenClicked(
        val canteenId: Int
    ) : CanteenAction

    data object OrdersClicked : CanteenAction

    data object HomeClicked : CanteenAction

    data object ProfileClicked : CanteenAction

    data object InformationClicked : CanteenAction
}