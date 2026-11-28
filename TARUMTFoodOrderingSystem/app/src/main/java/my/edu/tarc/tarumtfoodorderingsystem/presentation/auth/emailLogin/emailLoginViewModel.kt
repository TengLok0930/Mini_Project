package my.edu.tarc.tarumtfoodorderingsystem.presentation.auth.emailLogin

import android.util.Patterns
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class EmailLoginUiState(
    val email: String = "",
    val emailError: String? = null,
    val pendingEmail: String? = null
)

class EmailLoginViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(EmailLoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onAction(action: EmailLoginAction) {
        if (_uiState.value.pendingEmail != null) {
            return
        }

        when (action) {
            is EmailLoginAction.EmailChanged -> {
                _uiState.update {
                    it.copy(
                        email = action.value,
                        emailError = null
                    )
                }
            }

            EmailLoginAction.SendVerificationCodeClicked -> {
                validateEmail()
            }

            EmailLoginAction.BackToPasswordClicked -> Unit
        }
    }

    private fun validateEmail() {
        val email = _uiState.value.email.trim()

        val error = when {
            email.isBlank() -> "Please enter your registered email."

            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Please enter a valid email address."

            !EMAIL_DOMAIN_REGEX.matches(email) -> "Use your student.tarc.edu.my or tarc.edu.my email."

            else -> null
        }

        _uiState.update {
            it.copy(
                email = email,
                emailError = error,
                pendingEmail = if (error == null) email else null
            )
        }
    }

    fun onEmailNavigationHandled() {
        _uiState.update {
            it.copy(pendingEmail = null)
        }
    }

    private companion object {
        val EMAIL_DOMAIN_REGEX = Regex(
            pattern = """[^@\s]+@(?:student\.)?tarc\.edu\.my""",
            option = RegexOption.IGNORE_CASE
        )
    }
}