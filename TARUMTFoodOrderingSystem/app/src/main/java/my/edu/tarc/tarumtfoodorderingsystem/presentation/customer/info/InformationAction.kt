package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.info

sealed interface InformationAction {

    data object CloseClicked : InformationAction

    data object CampusMapClicked : InformationAction

    data object DismissMapErrorClicked : InformationAction
}