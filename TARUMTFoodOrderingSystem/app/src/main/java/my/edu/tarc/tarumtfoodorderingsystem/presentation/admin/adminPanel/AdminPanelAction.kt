package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.adminPanel

sealed interface AdminPanelAction {

    data object CanteenMaintenanceClicked : AdminPanelAction

    data object StoreMaintenanceClicked : AdminPanelAction

    data object MenuMaintenanceClicked : AdminPanelAction

    data object OrderReportingClicked : AdminPanelAction

    data object LogoutClicked : AdminPanelAction

    data object LogoutConfirmed : AdminPanelAction

    data object LogoutDismissed : AdminPanelAction
}