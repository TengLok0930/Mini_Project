package my.edu.tarc.tarumtfoodorderingsystem.presentation.auth.register

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

data class RegisterUiState(
    val userId: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val userIdError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val registerError: String? = null,
    val isRegistrationSuccessful: Boolean = false
)

// Register Service is to be deleted soon (delete for backend)
fun interface RegistrationService {
    suspend fun register(
        userId: Int,
        email: String,
        password: String
    )
}

class RegisterViewModel(private val registrationService: RegistrationService) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    fun onAction(action: RegisterAction) {
        if (_uiState.value.isLoading || _uiState.value.isRegistrationSuccessful) {
            return
        }

        when (action) {
            is RegisterAction.UserIdChanged -> {
                _uiState.update {
                    it.copy(
                        userId = action.value,
                        userIdError = null,
                        registerError = null
                    )
                }
            }

            is RegisterAction.EmailChanged -> {
                _uiState.update {
                    it.copy(
                        email = action.value,
                        emailError = null,
                        registerError = null
                    )
                }
            }

            is RegisterAction.PasswordChanged -> {
                _uiState.update {
                    it.copy(
                        password = action.value,
                        passwordError = null,
                        confirmPasswordError = null,
                        registerError = null
                    )
                }
            }

            is RegisterAction.ConfirmPasswordChanged -> {
                _uiState.update {
                    it.copy(
                        confirmPassword = action.value,
                        confirmPasswordError = null,
                        registerError = null
                    )
                }
            }

            RegisterAction.TogglePasswordVisibility -> {
                _uiState.update {
                    it.copy(
                        isPasswordVisible = !it.isPasswordVisible
                    )
                }
            }

            RegisterAction.ToggleConfirmPasswordVisibility -> {
                _uiState.update {
                    it.copy(
                        isConfirmPasswordVisible =
                            !it.isConfirmPasswordVisible
                    )
                }
            }

            RegisterAction.RegisterClicked -> register()

            RegisterAction.BackClicked -> Unit
        }
    }

    private fun register() {
        val currentState = _uiState.value
        val userIdText = currentState.userId.trim()
        val email = currentState.email.trim()

        val userIdError = when {
            userIdText.isBlank() ->
                "Please enter your user ID."

            !USER_ID_REGEX.matches(userIdText) ->
                "User ID must contain exactly 7 digits."

            else -> null
        }

        val parsedUserId = if (userIdError == null) {
            userIdText.toIntOrNull()
        } else {
            null
        }

        val emailError = when {
            email.isBlank() ->
                "Please enter your email address."

            !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                "Please enter a valid email address."

            !EMAIL_DOMAIN_REGEX.matches(email) ->
                "Use your student.tarc.edu.my or tarc.edu.my email address."

            else -> null
        }

        val passwordError = when {
            currentState.password.isBlank() ->
                "Please enter a password."

            currentState.password.length < MIN_PASSWORD_LENGTH ->
                "Password must contain at least " +
                        "$MIN_PASSWORD_LENGTH characters."

            else -> null
        }

        val confirmPasswordError = when {
            currentState.confirmPassword.isBlank() ->
                "Please confirm your password."

            currentState.confirmPassword != currentState.password ->
                "Passwords do not match."

            else -> null
        }

        _uiState.update {
            it.copy(
                userIdError = userIdError,
                emailError = emailError,
                passwordError = passwordError,
                confirmPasswordError = confirmPasswordError,
                registerError = null
            )
        }

        if (
            userIdError != null ||
            emailError != null ||
            passwordError != null ||
            confirmPasswordError != null
        ) {
            return
        }

        val validUserId = parsedUserId ?: return

        _uiState.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch {
            try {
                registrationService.register(
                    userId = validUserId,
                    email = email,
                    password = currentState.password
                )

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        password = "",
                        confirmPassword = "",
                        isPasswordVisible = false,
                        isConfirmPasswordVisible = false,
                        isRegistrationSuccessful = true
                    )
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        registerError =
                            "Unable to register. Check your details " +
                                    "and connection, then try again."
                    )
                }
            }
        }
    }

    fun onRegistrationSuccessHandled() {
        _uiState.update {
            it.copy(isRegistrationSuccessful = false)
        }
    }

    private companion object {
        const val MIN_PASSWORD_LENGTH = 8

        val USER_ID_REGEX = Regex("""[0-9]{7}""")

        val EMAIL_DOMAIN_REGEX = Regex(
            pattern = """[^@\s]+@(?:student\.)?tarc\.edu\.my""",
            option = RegexOption.IGNORE_CASE
        )
    }
}