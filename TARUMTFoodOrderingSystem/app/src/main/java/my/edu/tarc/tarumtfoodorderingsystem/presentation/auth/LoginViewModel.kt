package my.edu.tarc.tarumtfoodorderingsystem.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

data class LoginUiState(
    val userId: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val userIdError: String? = null,
    val passwordError: String? = null,
    val loginError: String? = null,
    val isLoginSuccessful: Boolean = false
)

// Login Authenticator is to be deleted soon (delete for backend)
fun interface LoginAuthenticator {
    suspend fun login(
        userId: Int,
        password: String
    )
}

class LoginViewModel(private val authenticator: LoginAuthenticator) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onAction(action: LoginAction) {
        if (
            _uiState.value.isLoading || _uiState.value.isLoginSuccessful
        ) {
            return
        }

        when (action) {
            is LoginAction.UserIdChanged -> {
                _uiState.update {
                    it.copy(
                        userId = action.value,
                        userIdError = null,
                        loginError = null
                    )
                }
            }

            is LoginAction.PasswordChanged -> {
                _uiState.update {
                    it.copy(
                        password = action.value,
                        passwordError = null,
                        loginError = null
                    )
                }
            }

            LoginAction.TogglePasswordVisibility -> {
                _uiState.update {
                    it.copy(
                        isPasswordVisible = !it.isPasswordVisible
                    )
                }
            }

            LoginAction.LoginClicked -> login()

            LoginAction.EmailCodeClicked,
            LoginAction.RegisterClicked -> Unit
        }
    }

    private fun login() {
        val currentState = _uiState.value
        val userIdText = currentState.userId.trim()

        val containsOnlyDigits = userIdText.isNotEmpty() && userIdText.all { it in '0' .. '9'}

        val parsedUserId = if (containsOnlyDigits) {
            userIdText.toIntOrNull()
        } else {
            null
        }

        val userIdError = when {
            userIdText.isBlank() -> "Please enter your TARUMT ID"
            parsedUserId == null -> "Please enter a valid numeric ID"
            else -> null
        }

        val passwordError = when {
            currentState.password.isBlank() -> "Please enter your password."
            else -> null
        }

        if (userIdError != null || passwordError != null) {
            _uiState.update {
                it.copy(
                    userIdError = userIdError,
                    passwordError = passwordError,
                    loginError = null
                )
            }
            return
        }

        val validUserId = parsedUserId ?: return

        _uiState.update {
            it.copy(
                isLoading = true,
                userIdError = null,
                passwordError = null,
                loginError = null
            )
        }

        viewModelScope.launch {
            try {
                authenticator.login(
                    userId = validUserId,
                    password = currentState.password
                )

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        password = "",
                        isPasswordVisible = false,
                        isLoginSuccessful = true
                    )
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        loginError = "Unable to sign in. Check your " +
                                "credentials and connection, then try again."
                    )
                }
            }
        }
    }

    fun onLoginSuccessHandled() {
        _uiState.update {
            it.copy(isLoginSuccessful = false)
        }
    }
}