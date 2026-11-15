package my.edu.tarc.tarumtfoodorderingsystem.presentation.auth.otp

import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

data class OTPLoginUiState(
    val email: String = "",
    val code: String = "",
    val remainingSeconds: Int = 180,
    val resendSeconds: Int = 30,
    val errorMessage: String? = null,
    val infoMessage: String? = null,
    val isVerificationSuccessful: Boolean = false
) {
    val isExpired: Boolean
        get() = remainingSeconds <= 0

    val canResend: Boolean
        get() = resendSeconds <= 0
}

class OTPLoginViewModel(
    email: String,
    private val enableDebugTesting: Boolean = false
) : ViewModel() {

    private var expiresAt =
        SystemClock.elapsedRealtime() + CODE_VALIDITY_MILLIS

    private var resendAvailableAt =
        SystemClock.elapsedRealtime() + RESEND_COOLDOWN_MILLIS

    private val _uiState = MutableStateFlow(
        OTPLoginUiState(
            email = email.trim()
        )
    )

    val uiState = _uiState.asStateFlow()

    init {
        updateTimers()

        viewModelScope.launch {
            while (isActive) {
                delay(500)
                updateTimers()
            }
        }
    }

    fun onAction(action: OTPLoginAction) {
        if (_uiState.value.isVerificationSuccessful) {
            return
        }

        when (action) {
            is OTPLoginAction.CodeChanged -> {
                updateCode(action.value)
            }

            OTPLoginAction.VerifyClicked -> {
                verifyCode()
            }

            OTPLoginAction.ResendClicked -> {
                resendCode()
            }

            // These are handled by OTPLoginRoute.
            OTPLoginAction.ChangeEmailClicked,
            OTPLoginAction.BackClicked -> Unit
        }
    }

    private fun updateCode(value: String) {
        updateTimers()

        if (_uiState.value.isExpired) {
            return
        }

        // Allow only up to six ASCII digits.
        if (
            value.length <= CODE_LENGTH &&
            value.all { it in '0'..'9' }
        ) {
            _uiState.update {
                it.copy(
                    code = value,
                    errorMessage = null,
                    infoMessage = null
                )
            }
        }
    }

    private fun verifyCode() {
        updateTimers()

        val state = _uiState.value

        if (state.email.isBlank()) {
            _uiState.update {
                it.copy(
                    errorMessage = "Email is missing. Go back and enter it again."
                )
            }
            return
        }

        if (state.isExpired) {
            _uiState.update {
                it.copy(
                    errorMessage = "This code has expired. Request a new code."
                )
            }
            return
        }

        if (state.code.length != CODE_LENGTH) {
            _uiState.update {
                it.copy(
                    errorMessage = "Please enter all 6 digits."
                )
            }
            return
        }

        if (!enableDebugTesting) {
            _uiState.update {
                it.copy(
                    errorMessage =
                        "OTP verification is not connected to the backend yet."
                )
            }
            return
        }

        // Frontend testing only. This is not real authentication.
        if (state.code != DEBUG_CODE) {
            _uiState.update {
                it.copy(
                    errorMessage =
                        "Incorrect test code. Use 123456 in debug testing."
                )
            }
            return
        }

        _uiState.update {
            it.copy(
                code = "",
                errorMessage = null,
                infoMessage = null,
                isVerificationSuccessful = true
            )
        }
    }

    private fun resendCode() {
        updateTimers()

        if (!_uiState.value.canResend) {
            return
        }

        if (!enableDebugTesting) {
            _uiState.update {
                it.copy(
                    errorMessage =
                        "Sending an OTP is not connected to the backend yet."
                )
            }
            return
        }

        // Simulate a fresh code window for frontend testing.
        // No email is actually sent.
        val now = SystemClock.elapsedRealtime()

        expiresAt = now + CODE_VALIDITY_MILLIS
        resendAvailableAt = now + RESEND_COOLDOWN_MILLIS

        _uiState.update {
            it.copy(
                code = "",
                errorMessage = null,
                infoMessage = "Test code reset. Use 123456. No email was sent."
            )
        }

        updateTimers()
    }

    private fun updateTimers() {
        val now = SystemClock.elapsedRealtime()

        val remainingSeconds = secondsUntil(
            deadline = expiresAt,
            now = now
        )

        val resendSeconds = secondsUntil(
            deadline = resendAvailableAt,
            now = now
        )

        _uiState.update {
            it.copy(
                remainingSeconds = remainingSeconds,
                resendSeconds = resendSeconds,
                code = if (remainingSeconds == 0) "" else it.code
            )
        }
    }

    private fun secondsUntil(
        deadline: Long,
        now: Long
    ): Int {
        val remainingMillis = (deadline - now).coerceAtLeast(0L)

        // Round upward so 1–999 milliseconds displays as one second.
        return ((remainingMillis + 999L) / 1000L).toInt()
    }

    fun onVerificationSuccessHandled() {
        _uiState.update {
            it.copy(isVerificationSuccessful = false)
        }
    }

    private companion object {
        const val CODE_LENGTH = 6
        const val DEBUG_CODE = "123456"

        const val CODE_VALIDITY_MILLIS = 3 * 60 * 1000L
        const val RESEND_COOLDOWN_MILLIS = 30 * 1000L
    }
}