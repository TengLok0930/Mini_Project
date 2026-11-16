package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.menuMaintenance.addMenuItem

import android.content.ContentResolver
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.math.BigDecimal
import java.math.RoundingMode
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.menuMaintenance.AdminMenuStoreOption
import my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.menuMaintenance.adminMenuSampleState

enum class MenuItemType(
    val displayName: String
) {
    FOOD("Food"),
    BEVERAGE("Beverage")
}

data class NewMenuItemDraft(
    val name: String,
    val storeId: Int,
    val category: String,
    val itemType: MenuItemType,
    val priceInSen: Int,
    val imageUri: String,
    val isPublished: Boolean = true
)

data class AdminAddItemUiState(
    val name: String = "",
    val stores: List<AdminMenuStoreOption> = emptyList(),
    val selectedStoreId: Int? = null,
    val category: String = "",
    val itemType: MenuItemType? = null,
    val price: String = "",
    val imageUri: String? = null,

    val nameError: String? = null,
    val storeError: String? = null,
    val categoryError: String? = null,
    val itemTypeError: String? = null,
    val priceError: String? = null,
    val imageError: String? = null,

    val isReadingImage: Boolean = false,
    val pendingItem: NewMenuItemDraft? = null
) {
    val selectedStore: AdminMenuStoreOption?
        get() = stores.firstOrNull {
            it.id == selectedStoreId
        }
}

class AdminAddItemViewModel(
    initialStoreId: Int? = null
) : ViewModel() {

    private val storeOptions = adminMenuSampleState().stores

    private val _uiState = MutableStateFlow(
        AdminAddItemUiState(
            stores = storeOptions,
            selectedStoreId = initialStoreId?.takeIf { id ->
                storeOptions.any { it.id == id }
            }
        )
    )

    val uiState = _uiState.asStateFlow()

    fun onAction(action: AdminAddItemAction) {
        if (_uiState.value.pendingItem != null) return

        when (action) {
            is AdminAddItemAction.NameChanged -> {
                _uiState.update {
                    it.copy(
                        name = action.value,
                        nameError = null
                    )
                }
            }

            is AdminAddItemAction.StoreSelected -> {
                _uiState.update { state ->
                    if (
                        state.stores.any {
                            it.id == action.storeId
                        }
                    ) {
                        state.copy(
                            selectedStoreId = action.storeId,
                            storeError = null
                        )
                    } else {
                        state
                    }
                }
            }

            is AdminAddItemAction.CategoryChanged -> {
                _uiState.update {
                    it.copy(
                        category = action.value,
                        categoryError = null
                    )
                }
            }

            is AdminAddItemAction.ItemTypeSelected -> {
                _uiState.update {
                    it.copy(
                        itemType = action.itemType,
                        itemTypeError = null
                    )
                }
            }

            is AdminAddItemAction.PriceChanged -> {
                _uiState.update {
                    it.copy(
                        price = action.value,
                        priceError = null
                    )
                }
            }

            AdminAddItemAction.RemoveImageClicked -> {
                if (!_uiState.value.isReadingImage) {
                    _uiState.update {
                        it.copy(
                            imageUri = null,
                            imageError = null
                        )
                    }
                }
            }

            is AdminAddItemAction.ImageFailed -> {
                _uiState.update {
                    it.copy(imageError = action.message)
                }
            }

            AdminAddItemAction.PublishClicked -> {
                validateForm()
            }

            // Handled by AdminAddItemRoute.
            AdminAddItemAction.PickImageClicked,
            AdminAddItemAction.BackClicked -> Unit
        }
    }

    fun readCroppedImage(
        uri: Uri,
        contentResolver: ContentResolver
    ) {
        if (
            _uiState.value.isReadingImage ||
            _uiState.value.pendingItem != null
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

        if (state.isReadingImage) return

        val name = state.name.trim()
        val category = state.category.trim()
        val price = state.price.trim()

        val store = state.selectedStore
        val itemType = state.itemType
        val imageUri = state.imageUri

        val priceInSen = parsePriceInSen(price)

        val nameError = when {
            name.isBlank() ->
                "Please enter an item name."

            name.length > 100 ->
                "Item name must not exceed 100 characters."

            else -> null
        }

        val storeError = if (store == null) {
            "Please select a linked store."
        } else {
            null
        }

        val categoryError = when {
            category.isBlank() ->
                "Please enter a category."

            category.length > 50 ->
                "Category must not exceed 50 characters."

            else -> null
        }

        val itemTypeError = if (itemType == null) {
            "Please select an item type."
        } else {
            null
        }

        val priceError = when {
            price.isBlank() ->
                "Please enter a price."

            priceInSen == null ->
                "Enter a price from RM 0.01 to RM 999999.99 " +
                        "with up to 2 decimal places."

            else -> null
        }

        val imageError = when {
            imageUri.isNullOrBlank() ->
                "Please attach an item image."

            state.imageError != null ->
                state.imageError

            else -> null
        }

        _uiState.update {
            it.copy(
                name = name,
                category = category,
                price = price,
                nameError = nameError,
                storeError = storeError,
                categoryError = categoryError,
                itemTypeError = itemTypeError,
                priceError = priceError,
                imageError = imageError
            )
        }

        if (
            listOf(
                nameError,
                storeError,
                categoryError,
                itemTypeError,
                priceError,
                imageError
            ).any { it != null }
        ) {
            return
        }

        val validStore = store ?: return
        val validType = itemType ?: return
        val validPrice = priceInSen ?: return
        val validImage = imageUri?.takeIf { it.isNotBlank() } ?: return

        _uiState.update {
            it.copy(
                pendingItem = NewMenuItemDraft(
                    name = name,
                    storeId = validStore.id,
                    category = category,
                    itemType = validType,
                    priceInSen = validPrice,
                    imageUri = validImage
                )
            )
        }
    }

    private fun parsePriceInSen(value: String): Int? {
        if (!PRICE_PATTERN.matches(value)) return null

        return try {
            val amount = BigDecimal(value)

            if (amount <= BigDecimal.ZERO) {
                null
            } else {
                amount
                    .movePointRight(2)
                    .setScale(0, RoundingMode.UNNECESSARY)
                    .intValueExact()
            }
        } catch (exception: ArithmeticException) {
            null
        } catch (exception: NumberFormatException) {
            null
        }
    }

    fun onPublishRequestHandled() {
        _uiState.update {
            it.copy(pendingItem = null)
        }
    }

    private companion object {
        val PRICE_PATTERN = Regex(
            """\d{1,6}(\.\d{1,2})?"""
        )
    }
}