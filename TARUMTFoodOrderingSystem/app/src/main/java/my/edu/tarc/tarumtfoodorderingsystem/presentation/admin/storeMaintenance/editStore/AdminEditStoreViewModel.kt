package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.storeMaintenance.editStore

import android.content.ContentResolver
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.storeMaintenance.AdminStoreItem
import my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.storeMaintenance.StoreCanteenOption

data class UpdatedStoreDraft(
    val storeId: Int,
    val name: String,
    val imageUri: String
)

data class AdminEditStoreUiState(
    val storeId: Int? = null,
    val originalName: String = "",
    val canteenName: String = "",
    val name: String = "",
    val imageUri: String? = null,
    val nameError: String? = null,
    val imageError: String? = null,
    val isReadingImage: Boolean = false,
    val pendingUpdate: UpdatedStoreDraft? = null,
    val showDeleteDialog: Boolean = false,
    val pendingDelete: Int? = null
)

class AdminEditStoreViewModel(
    store: AdminStoreItem?,
    canteen: StoreCanteenOption?
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        AdminEditStoreUiState(
            storeId = store?.id?.takeIf {
                canteen != null && store.canteenId == canteen.id
            },
            originalName = store?.name.orEmpty(),
            canteenName = canteen?.name.orEmpty(),
            name = store?.name.orEmpty(),
            imageUri = store?.imageUri
        )
    )

    val uiState = _uiState.asStateFlow()

    fun onAction(action: AdminEditStoreAction) {
        if (_uiState.value.pendingUpdate !=  null || _uiState.value.pendingDelete != null) return

        when (action) {
            AdminEditStoreAction.DeleteClicked -> {
                val state = _uiState.value

                if (state.storeId != null && !state.isReadingImage) {
                    _uiState.update {
                        it.copy(showDeleteDialog = true)
                    }
                }
            }

            AdminEditStoreAction.DismissDeleteClicked -> {
                _uiState.update {
                    it.copy(showDeleteDialog = false)
                }
            }

            AdminEditStoreAction.ConfirmDeleteClicked -> {
                val storeId = _uiState.value.storeId ?: return

                _uiState.update {
                    it.copy(
                        showDeleteDialog = false,
                        pendingDelete = storeId
                    )
                }
            }

            is AdminEditStoreAction.NameChanged -> {
                _uiState.update {
                    it.copy(
                        name = action.value,
                        nameError = null
                    )
                }
            }

            AdminEditStoreAction.RemoveImageClicked -> {
                if (!_uiState.value.isReadingImage) {
                    _uiState.update {
                        it.copy(
                            imageUri = null,
                            imageError = null
                        )
                    }
                }
            }

            is AdminEditStoreAction.ImageFailed -> {
                _uiState.update {
                    it.copy(imageError = action.message)
                }
            }

            AdminEditStoreAction.UpdateClicked -> {
                validateForm()
            }

            // Handled by AdminEditStoreRoute.
            AdminEditStoreAction.BackClicked,
            AdminEditStoreAction.PickImageClicked -> Unit
        }
    }

    fun readCroppedImage(
        uri: Uri,
        contentResolver: ContentResolver
    ) {
        val state = _uiState.value

        if (
            state.storeId == null ||
            state.isReadingImage ||
            state.pendingUpdate != null ||
            state.pendingDelete != null
        ) {
            return
        }

        _uiState.update {
            it.copy(
                isReadingImage = true,
                imageError = null
            )
        }

        viewModelScope.launch {
            try {
                val dimensions = withContext(Dispatchers.IO) {
                    val options = BitmapFactory.Options().apply {
                        inJustDecodeBounds = true
                    }

                    val stream = contentResolver.openInputStream(uri)
                        ?: error("Cannot open image.")

                    stream.use {
                        BitmapFactory.decodeStream(
                            it,
                            null,
                            options
                        )
                    }

                    options.outWidth to options.outHeight
                }

                val width = dimensions.first
                val height = dimensions.second

                val error = when {
                    width <= 0 || height <= 0 ->
                        "Unable to read this image."

                    width != height ->
                        "Please crop the image to a square."

                    width > 100 || height > 100 ->
                        "The image must not exceed 100 × 100 pixels."

                    else -> null
                }

                _uiState.update {
                    it.copy(
                        imageUri = if (error == null) {
                            uri.toString()
                        } else {
                            it.imageUri
                        },
                        imageError = error
                    )
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        imageError = "Unable to read the cropped image. " +
                                "Please choose another image."
                    )
                }
            } finally {
                _uiState.update {
                    it.copy(isReadingImage = false)
                }
            }
        }
    }

    private fun validateForm() {
        val state = _uiState.value
        val storeId = state.storeId ?: return

        if (state.isReadingImage) return

        val name = state.name.trim()
        val imageUri = state.imageUri

        val nameError = when {
            name.isBlank() ->
                "Please enter a store name."

            name.length > 100 ->
                "Store name must not exceed 100 characters."

            else -> null
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
                imageError = imageError
            )
        }

        if (nameError != null || imageError != null) return

        val validImage = imageUri?.takeIf { it.isNotBlank() } ?: return

        _uiState.update {
            it.copy(
                pendingUpdate = UpdatedStoreDraft(
                    storeId = storeId,
                    name = name,
                    imageUri = validImage
                )
            )
        }
    }

    fun onUpdateHandled() {
        _uiState.update {
            it.copy(pendingUpdate = null)
        }
    }

    fun onDeleteHandled() {
        _uiState.update {
            it.copy(pendingDelete = null)
        }
    }
}