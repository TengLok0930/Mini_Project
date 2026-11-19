package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.info

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class InformationUiState(
    val orderingSteps: List<String> = listOf(
        "Choose a canteen from the Home screen.",
        "Select a store and browse its available food and drinks.",
        "Choose an item, set the quantity, and add any available " +
                "drink options or remarks.",
        "Add your items to the cart and review your order in the cart.",
        "Continue to checkout and complete payment.",
        "Open Orders to check your order details and pickup number. " +
                "Collect your order at the selected store when it is ready."
    ),
    val operatingHours: String = "7:00 am – 6:00 pm",
    val operatingHoursNote: String =
        "These are the general canteen operating hours. " +
                "Individual stores may have different opening times " +
                "or close earlier when items sell out.",
    val campusMapUrl: String =
        "https://www.tarc.edu.my/kl/KLCampusMap.pdf",
    val showMapError: Boolean = false
)

class InformationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        InformationUiState()
    )

    val uiState = _uiState.asStateFlow()

    fun onAction(
        action: InformationAction
    ) {
        when (action) {
            InformationAction.DismissMapErrorClicked -> {
                _uiState.update {
                    it.copy(
                        showMapError = false
                    )
                }
            }

            InformationAction.CloseClicked,
            InformationAction.CampusMapClicked -> {
                // Navigation and external links are handled
                // by InformationRoute.
            }
        }
    }

    fun onMapOpenFailed() {
        _uiState.update {
            it.copy(
                showMapError = true
            )
        }
    }
}