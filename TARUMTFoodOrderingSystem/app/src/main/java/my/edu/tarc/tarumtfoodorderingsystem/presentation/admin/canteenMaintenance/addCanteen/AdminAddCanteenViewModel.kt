package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.canteenMaintenance.addCanteen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class NewCanteenDraft(
    val name: String,
    val imageUri: String?
)

data class AdminAddCanteenUiState(
    val name: String = "",
    val imageUri: String? = null,
    val nameError: String? = null,
    val imageError: String? = null,
    val isReadingImage: Boolean = false,
    val pendingCanteen: NewCanteenDraft? = null
)

class AdminAddCanteenViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        AdminAddCanteenUiState()
    )

    val uiState = _uiState.asStateFlow()

    fun onAction(action: AdminAddCanteenAction) {
        if (_uiState.value.pendingCanteen != null) {
            return
        }

        when (action) {
            is AdminAddCanteenAction.NameChanged -> {
                _uiState.update {
                    it.copy(
                        name = action.value,
                        nameError = null
                    )
                }
            }

            AdminAddCanteenAction.ImageReadingStarted -> {
                _uiState.update {
                    it.copy(
                        isReadingImage = true,
                        imageError = null
                    )
                }
            }

            is AdminAddCanteenAction.ImageSelected -> {
                validateImage(action)
            }

            is AdminAddCanteenAction.ImageReadingFailed -> {
                _uiState.update {
                    it.copy(
                        isReadingImage = false,
                        imageError = action.message
                    )
                }
            }

            AdminAddCanteenAction.RemoveImageClicked -> {
                if (!_uiState.value.isReadingImage) {
                    _uiState.update {
                        it.copy(
                            imageUri = null,
                            imageError = null
                        )
                    }
                }
            }

            AdminAddCanteenAction.CreateClicked -> {
                validateForm()
            }

            AdminAddCanteenAction.PickImageClicked,
            AdminAddCanteenAction.BackClicked -> Unit
        }
    }

    private fun validateImage(
        action: AdminAddCanteenAction.ImageSelected
    ) {
        val error = when {
            action.width <= 0 || action.height <= 0 ->
                "Unable to read this image."

            action.width > MAX_IMAGE_DIMENSION ||
                    action.height > MAX_IMAGE_DIMENSION ->
                "Image must be no larger than 100 × 100 pixels."

            else -> null
        }

        _uiState.update {
            it.copy(
                // Keep the previous valid image if a replacement is rejected.
                imageUri = if (error == null) action.uri else it.imageUri,
                imageError = error,
                isReadingImage = false
            )
        }
    }

    private fun validateForm() {
        val state = _uiState.value

        if (state.isReadingImage) {
            return
        }

        val name = state.name.trim()

        val nameError = when {
            name.isBlank() ->
                "Please enter a canteen name."

            name.length > MAX_NAME_LENGTH ->
                "Canteen name must not exceed 100 characters."

            else -> null
        }

        val imageError = when {
            state.imageUri.isNullOrBlank() ->
                "Please attach a canteen image."

            state.imageError != null ->
                state.imageError

            else -> null
        }

        if (nameError != null || imageError != null) {
            _uiState.update {
                it.copy(
                    name = name,
                    nameError = nameError,
                    imageError = imageError,
                    pendingCanteen = null
                )
            }

            return
        }

        _uiState.update {
            it.copy(
                name = name,
                nameError = null,
                imageError = null,
                pendingCanteen = NewCanteenDraft(
                    name = name,
                    imageUri = state.imageUri
                )
            )
        }
    }

    fun onCreateRequestHandled() {
        _uiState.update {
            it.copy(pendingCanteen = null)
        }
    }

    private companion object {
        const val MAX_IMAGE_DIMENSION = 100
        const val MAX_NAME_LENGTH = 100
    }
}