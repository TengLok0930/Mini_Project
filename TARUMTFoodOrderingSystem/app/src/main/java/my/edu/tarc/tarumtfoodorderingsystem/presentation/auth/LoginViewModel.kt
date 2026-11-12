package my.edu.tarc.tarumtfoodorderingsystem.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

// Temporary frontend role login
enum class TestLoginRole {
    CUSTOMER,
    WORKER,
    ADMIN
}

data class LoginUiState(
    val userId: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val userIdError: String? = null,
    val passwordError: String? = null,
    val loginError: String? = null,
    val isLoginSuccessful: Boolean = false,
    val loggedInRole: TestLoginRole? = null // For login
)

// Login Authenticator is to be deleted soon (delete for backend)
fun interface LoginAuthenticator {
    suspend fun login(
        userId: Int,
        password: String
    ): TestLoginRole
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

        val userIdError = when {
            userIdText.isBlank() ->
                "Please enter your TARUMT ID."

            !USER_ID_REGEX.matches(userIdText) ->
                "User ID must contain exactly 7 digits."

            else -> null
        }

        val passwordError = when {
            currentState.password.isBlank() ->
                "Please enter your password."

            else -> null
        }

        if (userIdError != null || passwordError != null) {
            _uiState.update {
                it.copy(
                    userIdError = userIdError,
                    passwordError = passwordError,
                    loginError = null,
                    loggedInRole = null
                )
            }
            return
        }

        val validUserId = userIdText.toIntOrNull() ?: return

        _uiState.update {
            it.copy(
                isLoading = true,
                userIdError = null,
                passwordError = null,
                loginError = null,
                loggedInRole = null
            )
        }

        viewModelScope.launch {
            try {
                val role = authenticator.login(
                    userId = validUserId,
                    password = currentState.password
                )

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        password = "",
                        isPasswordVisible = false,
                        loggedInRole = role,
                        isLoginSuccessful = true
                    )
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        loggedInRole = null,
                        loginError = "Unable to sign in. Check your " +
                                "credentials and connection, then try again."
                    )
                }
            }
        }
    }

    fun onLoginSuccessHandled() {
        _uiState.update {
            it.copy(
                isLoginSuccessful = false,
                loggedInRole = null
            )
        }
    }

    private companion object {
        val USER_ID_REGEX = Regex("""[0-9]{7}""")
    }
}