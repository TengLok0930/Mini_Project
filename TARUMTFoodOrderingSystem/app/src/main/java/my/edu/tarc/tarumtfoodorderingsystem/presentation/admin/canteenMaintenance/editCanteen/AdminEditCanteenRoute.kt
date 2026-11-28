package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.canteenMaintenance.editCanteen

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
fun AdminEditCanteenRoute(
    viewModel: AdminEditCanteenViewModel,
    onNavigateBack: () -> Unit,
    onDeleteCanteen: (canteenId: Int) -> Unit,
    onUpdateCanteen: (UpdatedCanteenDraft) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val currentOnUpdate by rememberUpdatedState(onUpdateCanteen)
    val currentOnDelete by rememberUpdatedState(onDeleteCanteen)

    val cropLauncher = rememberLauncherForActivityResult(
        contract = CropImageContract()
    ) { result ->
        if (result.isSuccessful) {
            val uri = result.uriContent

            if (uri != null) {
                viewModel.readCroppedImage(
                    uri = uri,
                    contentResolver = context.applicationContext.contentResolver
                )
            } else {
                viewModel.onAction(
                    AdminEditCanteenAction.ImageFailed(
                        "The cropped image could not be opened."
                    )
                )
            }
        } else {
            val error = result.error

            if (error != null && error !is CancellationException) {
                viewModel.onAction(
                    AdminEditCanteenAction.ImageFailed(
                        "Unable to crop the image. Please try again."
                    )
                )
            }
        }
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            cropLauncher.launch(
                CropImageContractOptions(
                    uri = uri,
                    cropImageOptions = CropImageOptions(
                        activityTitle = "Crop Canteen Image",
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

    LaunchedEffect(uiState.pendingUpdate) {
        uiState.pendingUpdate?.let { draft ->
            viewModel.onUpdateHandled()
            currentOnUpdate(draft)
        }
    }

    LaunchedEffect(uiState.pendingDelete) {
        uiState.pendingDelete?.let { canteenId ->
            viewModel.onDeleteHandled()
            currentOnDelete(canteenId)
        }
    }

    AdminEditCanteenScreen(
        uiState = uiState,
        modifier = modifier,
        onAction = { action ->
            when (action) {
                AdminEditCanteenAction.BackClicked -> {
                    onNavigateBack()
                }

                AdminEditCanteenAction.PickImageClicked -> {
                    if (
                        uiState.canteenId != null &&
                        !uiState.isReadingImage &&
                        uiState.pendingUpdate == null &&
                        uiState.pendingDelete == null
                    ) {
                        imagePicker.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    }
                }

                else -> viewModel.onAction(action)
            }
        }
    )
}