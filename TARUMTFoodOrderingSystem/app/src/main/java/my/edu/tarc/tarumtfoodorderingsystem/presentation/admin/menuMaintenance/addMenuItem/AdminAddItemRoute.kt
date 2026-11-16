package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.menuMaintenance.addMenuItem

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import kotlinx.coroutines.CancellationException

@Composable
fun AdminAddItemRoute(
    viewModel: AdminAddItemViewModel,
    onNavigateBack: () -> Unit,
    onPublishItem: (NewMenuItemDraft) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val currentOnPublishItem by rememberUpdatedState(
        newValue = onPublishItem
    )

    val cropLauncher = rememberLauncherForActivityResult(
        contract = CropImageContract()
    ) { result ->
        if (result.isSuccessful) {
            val croppedUri = result.uriContent

            if (croppedUri == null) {
                viewModel.onAction(
                    AdminAddItemAction.ImageFailed(
                        message = "The cropped image could not be opened."
                    )
                )
            } else {
                viewModel.readCroppedImage(
                    uri = croppedUri,
                    contentResolver = context.applicationContext.contentResolver
                )
            }
        } else {
            val error = result.error

            // Normal cancellation leaves the current image unchanged.
            if (error != null && error !is CancellationException) {
                viewModel.onAction(
                    AdminAddItemAction.ImageFailed(
                        message = "Unable to crop the image. Please try again."
                    )
                )
            }
        }
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { selectedUri ->
        if (selectedUri != null) {
            cropLauncher.launch(
                CropImageContractOptions(
                    uri = selectedUri,
                    cropImageOptions = CropImageOptions(
                        activityTitle = "Crop Menu Item Image",
                        cropMenuCropButtonTitle = "Done",

                        fixAspectRatio = true,
                        aspectRatioX = 1,
                        aspectRatioY = 1,

                        outputCompressFormat = Bitmap.CompressFormat.PNG,

                        outputRequestWidth = 100,
                        outputRequestHeight = 100,
                        outputRequestSizeOptions =
                            CropImageView.RequestSizeOptions.RESIZE_INSIDE
                    )
                )
            )
        }
    }

    LaunchedEffect(uiState.pendingItem) {
        val draft = uiState.pendingItem

        if (draft != null) {
            viewModel.onPublishRequestHandled()
            currentOnPublishItem(draft)
        }
    }

    AdminAddItemScreen(
        uiState = uiState,
        modifier = modifier,
        onAction = { action ->
            when (action) {
                AdminAddItemAction.BackClicked -> {
                    onNavigateBack()
                }

                AdminAddItemAction.PickImageClicked -> {
                    if (
                        !uiState.isReadingImage &&
                        uiState.pendingItem == null
                    ) {
                        imagePicker.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts
                                    .PickVisualMedia
                                    .ImageOnly
                            )
                        )
                    }
                }

                else -> viewModel.onAction(action)
            }
        }
    )
}