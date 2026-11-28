package my.edu.tarc.tarumtfoodorderingsystem.presentation.auth

sealed interface LoginAction {
    data class UserIdChanged(val value: String) : LoginAction
    data class PasswordChanged(val value: String) : LoginAction

    data object TogglePasswordVisibility : LoginAction
    data object LoginClicked : LoginAction
    data object EmailCodeClicked : LoginAction
    data object RegisterClicked : LoginAction
}