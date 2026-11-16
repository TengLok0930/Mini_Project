package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.canteenMaintenance

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AdminCanteenItem(
    val id: Int,
    val name: String,
    val storeCount: Int
)

data class AdminCanteenUiState(
    val canteens: List<AdminCanteenItem> = emptyList(),
    val pendingRemoval: AdminCanteenItem? = null
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

    fun onAction(action: AdminCanteenAction) {
        when (action) {
            is AdminCanteenAction.RemoveCanteenClicked -> {
                _uiState.update { state ->
                    state.copy(
                        pendingRemoval = state.canteens.firstOrNull {
                            it.id == action.canteenId
                        }
                    )
                }
            }

            AdminCanteenAction.ConfirmRemoveClicked -> {
                removeSelectedCanteen()
            }

            AdminCanteenAction.DismissRemoveClicked -> {
                _uiState.update {
                    it.copy(pendingRemoval = null)
                }
            }

            // Navigation is handled by AdminCanteenRoute.
            AdminCanteenAction.BackClicked,
            AdminCanteenAction.CreateCanteenClicked -> Unit
        }
    }

    private fun removeSelectedCanteen() {
        _uiState.update { state ->
            val selected = state.pendingRemoval

            if (selected == null) {
                state
            } else {
                state.copy(
                    canteens = state.canteens.filterNot {
                        it.id == selected.id
                    },
                    pendingRemoval = null
                )
            }
        }
    }
}