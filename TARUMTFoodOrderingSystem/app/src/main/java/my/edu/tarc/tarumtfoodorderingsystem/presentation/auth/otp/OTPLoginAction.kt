package my.edu.tarc.tarumtfoodorderingsystem.presentation.auth.otp

sealed interface OTPLoginAction {

    data class CodeChanged(
        val value: String
    ) : OTPLoginAction

    data object VerifyClicked : OTPLoginAction

    data object ResendClicked : OTPLoginAction

    data object ChangeEmailClicked : OTPLoginAction

    data object BackClicked : OTPLoginAction
}