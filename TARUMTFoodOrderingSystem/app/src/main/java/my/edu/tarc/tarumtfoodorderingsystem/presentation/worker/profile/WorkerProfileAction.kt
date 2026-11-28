package my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.profile

sealed interface WorkerProfileAction {

    data object BackClicked : WorkerProfileAction

    data class ThemeSelected(
        val isDarkTheme: Boolean
    ) : WorkerProfileAction

    data object LogoutClicked : WorkerProfileAction

    data object ConfirmLogoutClicked : WorkerProfileAction

    data object DismissLogoutClicked : WorkerProfileAction

    data object ChangePasswordClicked : WorkerProfileAction

    data object DismissChangePasswordClicked : WorkerProfileAction

    data class CurrentPasswordChanged(
        val value: String
    ) : WorkerProfileAction

    data class NewPasswordChanged(
        val value: String
    ) : WorkerProfileAction

    data class ConfirmNewPasswordChanged(
        val value: String
    ) : WorkerProfileAction

    data object SubmitPasswordChangeClicked : WorkerProfileAction

    data object PasswordChangedMessageShown : WorkerProfileAction
}