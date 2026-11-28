package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.profile

sealed interface CustomerProfileAction {

    data object OrdersClicked : CustomerProfileAction

    data object HomeClicked : CustomerProfileAction

    data object ProfileClicked : CustomerProfileAction

    data object ChangePasswordClicked : CustomerProfileAction

    data object DismissChangePasswordClicked : CustomerProfileAction

    data class CurrentPasswordChanged(
        val value: String
    ) : CustomerProfileAction

    data class NewPasswordChanged(
        val value: String
    ) : CustomerProfileAction

    data class ConfirmNewPasswordChanged(
        val value: String
    ) : CustomerProfileAction

    data object SubmitPasswordChangeClicked : CustomerProfileAction

    data object PasswordChangedMessageShown : CustomerProfileAction

    data object ViewVouchersClicked : CustomerProfileAction

    data class ThemeSelected(
        val isDarkTheme: Boolean
    ) : CustomerProfileAction

    data object LogoutClicked : CustomerProfileAction

    data object ConfirmLogoutClicked : CustomerProfileAction

    data object DismissLogoutClicked : CustomerProfileAction

    data object DeleteAccountClicked : CustomerProfileAction

    data object ConfirmDeleteAccountClicked : CustomerProfileAction

    data object DismissDeleteAccountClicked : CustomerProfileAction
}