package my.edu.tarc.tarumtfoodorderingsystem.presentation.auth.register

sealed interface RegisterAction {

    data class UserIdChanged(
        val value: String
    ) : RegisterAction

    data class EmailChanged(
        val value: String
    ) : RegisterAction

    data class PasswordChanged(
        val value: String
    ) : RegisterAction

    data class ConfirmPasswordChanged(
        val value: String
    ) : RegisterAction

    data object TogglePasswordVisibility : RegisterAction

    data object ToggleConfirmPasswordVisibility : RegisterAction

    data object RegisterClicked : RegisterAction

    data object BackClicked : RegisterAction
}