package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.storeMaintenance.addStore

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.storeMaintenance.StoreCanteenOption

data class NewStoreDraft(
    val name: String,
    val canteenId: Int,
    val imageUri: String
)

data class AdminAddStoreUiState(
    val name: String = "",
    val canteens: List<StoreCanteenOption> = emptyList(),
    val selectedCanteenId: Int? = null,
    val imageUri: String? = null,
    val nameError: String? = null,
    val canteenError: String? = null,
    val imageError: String? = null,
    val isReadingImage: Boolean = false,
    val pendingStore: NewStoreDraft? = null
) {
    val selectedCanteen: StoreCanteenOption?
        get() = canteens.firstOrNull {
            it.id == selectedCanteenId
        }
}

class AdminAddStoreViewModel(
    initialCanteenId: Int? = null
) : ViewModel() {

    private val sampleCanteens = listOf(
        StoreCanteenOption(1, "Yum Yum Cafeteria"),
        StoreCanteenOption(2, "Red Bricks Cafeteria"),
        StoreCanteenOption(3, "Heritage Kitchen"),
        StoreCanteenOption(4, "Casuarina"),
        StoreCanteenOption(5, "FM Café")
    )

    private val _uiState = MutableStateFlow(
        AdminAddStoreUiState(
            canteens = sampleCanteens,
            selectedCanteenId = initialCanteenId?.takeIf { id ->
                sampleCanteens.any { it.id == id }
            }
        )
    )

    val uiState = _uiState.asStateFlow()

    fun onAction(action: AdminAddStoreAction) {
        if (_uiState.value.pendingStore != null) {
            return
        }

        when (action) {
            is AdminAddStoreAction.NameChanged -> {
                _uiState.update {
                    it.copy(
                        name = action.value,
                        nameError = null
                    )
                }
            }

            is AdminAddStoreAction.CanteenSelected -> {
                _uiState.update { state ->
                    if (
                        state.canteens.any {
                            it.id == action.canteenId
                        }
                    ) {
                        state.copy(
                            selectedCanteenId = action.canteenId,
                            canteenError = null
                        )
                    } else {
                        state
                    }
                }
            }

            AdminAddStoreAction.ImageReadingStarted -> {
                _uiState.update {
                    it.copy(
                        isReadingImage = true,
                        imageError = null
                    )
                }
            }

            is AdminAddStoreAction.ImageSelected -> {
                validateImage(action)
            }

            is AdminAddStoreAction.ImageReadingFailed -> {
                _uiState.update {
                    it.copy(
                        isReadingImage = false,
                        imageError = action.message
                    )
                }
            }

            AdminAddStoreAction.RemoveImageClicked -> {
                if (!_uiState.value.isReadingImage) {
                    _uiState.update {
                        it.copy(
                            imageUri = null,
                            imageError = null
                        )
                    }
                }
            }

            AdminAddStoreAction.CreateClicked -> {
                validateForm()
            }

            // Handled by the route.
            AdminAddStoreAction.PickImageClicked,
            AdminAddStoreAction.BackClicked -> Unit
        }
    }

    private fun validateImage(
        action: AdminAddStoreAction.ImageSelected
    ) {
        val error = when {
            action.width <= 0 || action.height <= 0 ->
                "Unable to read this image."

            action.width > MAX_IMAGE_DIMENSION ||
                    action.height > MAX_IMAGE_DIMENSION ->
                "The cropped image must not exceed 100 × 100 pixels."

            else -> null
        }

        _uiState.update {
            it.copy(
                imageUri = if (error == null) action.uri else it.imageUri,
                imageError = error,
                isReadingImage = false
            )
        }
    }

    private fun validateForm() {
        val state = _uiState.value

        if (state.isReadingImage) return

        val name = state.name.trim()
        val canteen = state.selectedCanteen
        val imageUri = state.imageUri

        val nameError = when {
            name.isBlank() ->
                "Please enter a store name."

            name.length > MAX_NAME_LENGTH ->
                "Store name must not exceed 100 characters."

            else -> null
        }

        val canteenError = if (canteen == null) {
            "Please select a linked canteen."
        } else {
            null
        }

        val imageError = when {
            imageUri.isNullOrBlank() ->
                "Please attach a store image."

            state.imageError != null ->
                state.imageError

            else -> null
        }

        _uiState.update {
            it.copy(
                name = name,
                nameError = nameError,
                canteenError = canteenError,
                imageError = imageError
            )
        }

        if (
            nameError != null ||
            canteenError != null ||
            imageError != null
        ) {
            return
        }

        val validCanteen = canteen ?: return
        val validImageUri = imageUri?.takeIf { it.isNotBlank() } ?: return

        _uiState.update {
            it.copy(
                pendingStore = NewStoreDraft(
                    name = name,
                    canteenId = validCanteen.id,
                    imageUri = validImageUri
                )
            )
        }
    }

    fun onCreateRequestHandled() {
        _uiState.update {
            it.copy(pendingStore = null)
        }
    }

    private companion object {
        const val MAX_NAME_LENGTH = 100
        const val MAX_IMAGE_DIMENSION = 100
    }
}