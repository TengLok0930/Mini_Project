package my.edu.tarc.tarumtfoodorderingsystem.presentation.auth.emailLogin

sealed interface EmailLoginAction {

    data class EmailChanged(
        val value: String
    ) : EmailLoginAction

    data object SendVerificationCodeClicked : EmailLoginAction
    data object BackToPasswordClicked : EmailLoginAction
}