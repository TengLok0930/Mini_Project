package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Locale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.cart.CartUiState
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.cart.CustomerCartItem
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.profile.voucher.CustomerVoucher
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.profile.voucher.customerVoucherSampleData

enum class PaymentMethod(
    val displayName: String
) {
    TOUCH_N_GO_EWALLET("Touch and Go e-Wallet"),
    FPX("Online banking (FPX)")
}

/**
 * Local checkout details prepared for a future payment integration.
 * Creating this object does not charge the customer.
 */
data class PaymentRequest(
    val storeId: Int,
    val items: List<CustomerCartItem>,
    val paymentMethod: PaymentMethod,
    val subtotalInSen: Long,
    val voucherCode: String?,
    val discountInSen: Long,
    val totalInSen: Long
)

data class PaymentUiState(
    val cart: CartUiState,
    val selectedMethod: PaymentMethod =
        PaymentMethod.TOUCH_N_GO_EWALLET,
    val appliedVoucher: CustomerVoucher? = null,
    val discountInSen: Long = 0L,
    val showVoucherDialog: Boolean = false,
    val voucherInput: String = "",
    val voucherInputError: String? = null,
    val voucherIssue: String? = null,
    val checkoutError: String? = null
) {
    val totalToPayInSen: Long
        get() = (
                cart.subtotalInSen - discountInSen
                ).coerceAtLeast(0L)

    val canPay: Boolean
        get() = cart.canProceedToPayment &&
                cart.subtotalInSen > 0L &&
                voucherIssue == null
}

private data class PaymentFormState(
    val selectedMethod: PaymentMethod =
        PaymentMethod.TOUCH_N_GO_EWALLET,
    val appliedVoucher: CustomerVoucher? = null,
    val showVoucherDialog: Boolean = false,
    val voucherInput: String = "",
    val voucherInputError: String? = null,
    val checkoutError: String? = null,
    val lastValidationAtMillis: Long = System.currentTimeMillis()
)

class PaymentViewModel(
    private val cartState: StateFlow<CartUiState>
) : ViewModel() {

    private var paymentSubmitted = false

    fun allowPaymentRetry() {
        paymentSubmitted = false
    }

    private val vouchers = customerVoucherSampleData()

    private val formState = MutableStateFlow(
        PaymentFormState()
    )

    val uiState: StateFlow<PaymentUiState> = combine(
        cartState,
        formState
    ) { cart, form ->
        buildUiState(
            cart = cart,
            form = form,
            nowMillis = System.currentTimeMillis()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = buildUiState(
            cart = cartState.value,
            form = formState.value,
            nowMillis = System.currentTimeMillis()
        )
    )

    fun refreshValidation() {
        formState.update {
            it.copy(
                lastValidationAtMillis = System.currentTimeMillis()
            )
        }
    }

    fun onAction(action: PaymentAction) {
        when (action) {
            is PaymentAction.PaymentMethodSelected -> {
                formState.update {
                    it.copy(
                        selectedMethod = action.method,
                        checkoutError = null
                    )
                }
            }

            PaymentAction.OpenVoucherClicked -> {
                formState.update {
                    it.copy(
                        showVoucherDialog = true,
                        voucherInput = it.appliedVoucher?.code.orEmpty(),
                        voucherInputError = null
                    )
                }
            }

            PaymentAction.DismissVoucherClicked -> {
                formState.update {
                    it.copy(
                        showVoucherDialog = false,
                        voucherInputError = null
                    )
                }
            }

            is PaymentAction.VoucherCodeChanged -> {
                formState.update {
                    it.copy(
                        voucherInput = action.code,
                        voucherInputError = null
                    )
                }
            }

            PaymentAction.ApplyVoucherClicked -> {
                applyVoucher()
            }

            PaymentAction.RemoveVoucherClicked -> {
                formState.update {
                    it.copy(
                        appliedVoucher = null,
                        voucherInput = "",
                        voucherInputError = null,
                        showVoucherDialog = false,
                        checkoutError = null
                    )
                }
            }

            PaymentAction.BackClicked,
            PaymentAction.PayClicked -> {
                // Handled by PaymentRoute.
            }
        }
    }

    private fun applyVoucher() {
        val code = formState.value.voucherInput
            .trim()
            .uppercase(Locale.ROOT)

        val voucher = vouchers.firstOrNull {
            it.code.uppercase(Locale.ROOT) == code
        }

        val error = when {
            code.isBlank() -> {
                "Enter a voucher code."
            }

            voucher == null -> {
                "This voucher code is invalid."
            }

            else -> {
                validateVoucher(
                    voucher = voucher,
                    subtotalInSen = cartState.value.subtotalInSen,
                    nowMillis = System.currentTimeMillis()
                )
            }
        }

        if (error != null) {
            formState.update {
                it.copy(
                    voucherInputError = error
                )
            }

            return
        }

        val validVoucher = voucher ?: return

        formState.update {
            it.copy(
                appliedVoucher = validVoucher,
                voucherInput = validVoucher.code,
                voucherInputError = null,
                showVoucherDialog = false,
                checkoutError = null
            )
        }
    }

    fun createPaymentRequest(): PaymentRequest? {

        if (paymentSubmitted) {
            return null
        }

        val currentState = buildUiState(
            cart = cartState.value,
            form = formState.value,
            nowMillis = System.currentTimeMillis()
        )

        if (!currentState.canPay) {
            formState.update {
                it.copy(
                    checkoutError = currentState.voucherIssue
                        ?: "Your cart is unavailable or contains invalid quantities.",
                    lastValidationAtMillis = System.currentTimeMillis()
                )
            }

            return null
        }

        paymentSubmitted = true

        return PaymentRequest(
            storeId = currentState.cart.storeId,
            items = currentState.cart.items.toList(),
            paymentMethod = currentState.selectedMethod,
            subtotalInSen = currentState.cart.subtotalInSen,
            voucherCode = currentState.appliedVoucher?.code,
            discountInSen = currentState.discountInSen,
            totalInSen = currentState.totalToPayInSen
        )
    }

    private fun buildUiState(
        cart: CartUiState,
        form: PaymentFormState,
        nowMillis: Long
    ): PaymentUiState {
        val voucherIssue = form.appliedVoucher?.let { voucher ->
            validateVoucher(
                voucher = voucher,
                subtotalInSen = cart.subtotalInSen,
                nowMillis = nowMillis
            )
        }

        val discount = if (
            form.appliedVoucher != null &&
            voucherIssue == null
        ) {
            calculateDiscount(
                voucher = form.appliedVoucher,
                subtotalInSen = cart.subtotalInSen
            )
        } else {
            0L
        }

        return PaymentUiState(
            cart = cart,
            selectedMethod = form.selectedMethod,
            appliedVoucher = form.appliedVoucher,
            discountInSen = discount,
            showVoucherDialog = form.showVoucherDialog,
            voucherInput = form.voucherInput,
            voucherInputError = form.voucherInputError,
            voucherIssue = voucherIssue,
            checkoutError = form.checkoutError
        )
    }
}

private fun validateVoucher(
    voucher: CustomerVoucher,
    subtotalInSen: Long,
    nowMillis: Long
): String? {
    return when {
        nowMillis >= voucher.expiresAtMillis -> {
            "This voucher has expired. Remove it or enter another code."
        }

        subtotalInSen < voucher.minimumSpendInSen -> {
            "Spend at least " +
                    formatPaymentAmount(voucher.minimumSpendInSen) +
                    " to use this voucher."
        }

        else -> null
    }
}

private fun calculateDiscount(
    voucher: CustomerVoucher,
    subtotalInSen: Long
): Long {
    val discount = if (voucher.type.percentageOff > 0) {
        BigDecimal.valueOf(subtotalInSen)
            .multiply(
                BigDecimal.valueOf(
                    voucher.type.percentageOff.toLong()
                )
            )
            .divide(
                BigDecimal.valueOf(100L),
                0,
                RoundingMode.DOWN
            )
            .longValueExact()
    } else {
        voucher.type.fixedDiscountInSen
    }

    return discount.coerceIn(
        minimumValue = 0L,
        maximumValue = subtotalInSen.coerceAtLeast(0L)
    )
}

internal fun formatPaymentAmount(
    amountInSen: Long
): String {
    return String.format(
        Locale.US,
        "RM %,.2f",
        BigDecimal.valueOf(amountInSen, 2)
    )
}