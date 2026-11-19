package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.menu

sealed interface MenuAction {

    data object BackClicked : MenuAction

    data object CartClicked : MenuAction

    data class ItemClicked(
        val itemId: Int
    ) : MenuAction

    data object InformationClicked : MenuAction
}