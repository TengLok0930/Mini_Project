package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.canteenMaintenance

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AdminCanteenItem(
    val id: Int,
    val name: String,
    val storeCount: Int,
    val imageUri: String? = null
)

data class AdminCanteenUiState(
    val canteens: List<AdminCanteenItem> = emptyList()
)

class AdminCanteenViewModel : ViewModel() {

    // Frontend sample data only.
    private val _uiState = MutableStateFlow(
        AdminCanteenUiState(
            canteens = listOf(
                AdminCanteenItem(
                    id = 1,
                    name = "Yum Yum Cafeteria",
                    storeCount = 7
                ),
                AdminCanteenItem(
                    id = 2,
                    name = "Red Bricks Cafeteria",
                    storeCount = 8
                ),
                AdminCanteenItem(
                    id = 3,
                    name = "Heritage Kitchen",
                    storeCount = 12
                ),
                AdminCanteenItem(
                    id = 4,
                    name = "Casuarina",
                    storeCount = 4
                ),
                AdminCanteenItem(
                    id = 5,
                    name = "FM Café",
                    storeCount = 1
                )
            )
        )
    )

    val uiState = _uiState.asStateFlow()

    fun updateCanteen(
        canteenId: Int,
        name: String,
        imageUri: String
    ) {
        _uiState.update { state ->
            state.copy(
                canteens = state.canteens.map { canteen ->
                    if (canteen.id == canteenId) {
                        canteen.copy(
                            name = name,
                            imageUri = imageUri
                        )
                    } else {
                        canteen
                    }
                }
            )
        }
    }
}