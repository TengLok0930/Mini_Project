package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.profile.voucher

import androidx.lifecycle.ViewModel
import java.util.Calendar
import java.util.Locale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class VoucherType(
    val displayName: String,
    val percentageOff: Int = 0,
    val fixedDiscountInSen: Long = 0L
) {
    DISCOUNT5(
        displayName = "5% off",
        percentageOff = 5
    ),

    DISCOUNT10(
        displayName = "10% off",
        percentageOff = 10
    ),

    OFF2(
        displayName = "RM 2 off",
        fixedDiscountInSen = 200L
    ),

    OFF5(
        displayName = "RM 5 off",
        fixedDiscountInSen = 500L
    )
}

data class CustomerVoucher(
    val id: Int,
    val code: String,
    val type: VoucherType,
    val expiresAtMillis: Long,
    val minimumSpendInSen: Long = 1_000L
)

data class VoucherUiState(
    val vouchers: List<CustomerVoucher> = emptyList(),
    val currentTimeMillis: Long = System.currentTimeMillis()
) {
    val availableVouchers: List<CustomerVoucher>
        get() = vouchers
            .filter {
                currentTimeMillis < it.expiresAtMillis
            }
            .sortedWith(
                compareBy<CustomerVoucher> {
                    it.expiresAtMillis
                }.thenBy {
                    it.id
                }
            )
}

class VoucherViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        VoucherUiState(
            vouchers = customerVoucherSampleData()
        )
    )

    val uiState = _uiState.asStateFlow()

    fun refreshAvailability() {
        _uiState.update {
            it.copy(
                currentTimeMillis = System.currentTimeMillis()
            )
        }
    }

    /**
     * Lookup helper for the future checkout screen.
     *
     * Returns null when:
     * - The code does not exist.
     * - The voucher has expired.
     * - The minimum spend has not been reached.
     *
     * This does not redeem or consume the voucher.
     */
    fun findApplicableVoucher(
        enteredCode: String,
        subtotalInSen: Long,
        nowMillis: Long = System.currentTimeMillis()
    ): CustomerVoucher? {
        val normalizedCode = enteredCode
            .trim()
            .uppercase(Locale.ROOT)

        return _uiState.value.vouchers.firstOrNull { voucher ->
            voucher.code.uppercase(Locale.ROOT) == normalizedCode &&
                    nowMillis < voucher.expiresAtMillis &&
                    subtotalInSen >= voucher.minimumSpendInSen
        }
    }
}

/**
 * Fixed frontend campaign dates.
 * Replace with repository/backend voucher data later.
 */
internal fun customerVoucherSampleData(): List<CustomerVoucher> {
    val expiryExclusive = Calendar.getInstance().apply {
        clear()

        // Midnight after 1 December 2026.
        set(
            2026,
            Calendar.DECEMBER,
            2,
            0,
            0,
            0
        )
    }.timeInMillis

    return listOf(
        CustomerVoucher(
            id = 1,
            code = "CAMPUS2",
            type = VoucherType.OFF2,
            expiresAtMillis = expiryExclusive
        ),
        CustomerVoucher(
            id = 2,
            code = "LUNCH10",
            type = VoucherType.DISCOUNT10,
            expiresAtMillis = expiryExclusive
        )
    )
}