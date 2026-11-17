package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.canteen

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CustomerCanteenItem(
    val id: Int,
    val name: String,
    val imageUrl: String? = null,
    @DrawableRes val imageRes: Int? = null,
    val isAvailable: Boolean = true
)

data class CanteenUiState(
    val canteens: List<CustomerCanteenItem> = emptyList()
) {
    val availableCanteens: List<CustomerCanteenItem>
        get() = canteens.filter {
            it.isAvailable
        }
}

class CanteenViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        customerCanteenSampleState()
    )

    val uiState = _uiState.asStateFlow()
}

/**
 * Frontend sample data only.
 * Replace with repository/Supabase data later.
 */
internal fun customerCanteenSampleState(): CanteenUiState {
    return CanteenUiState(
        canteens = listOf(
            CustomerCanteenItem(
                id = 1,
                name = "Yum Yum Cafeteria"
            ),
            CustomerCanteenItem(
                id = 2,
                name = "Red Bricks Cafeteria"
            ),
            CustomerCanteenItem(
                id = 3,
                name = "Heritage Kitchen"
            ),
            CustomerCanteenItem(
                id = 4,
                name = "Casuarina"
            ),
            CustomerCanteenItem(
                id = 5,
                name = "FM Café"
            )
        )
    )
}