package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.canteenMaintenance.addCanteen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

@Composable
fun AdminAddCanteenRoute(
    viewModel: AdminAddCanteenViewModel,
    onNavigateBack: () -> Unit,
    onCreateCanteen: (NewCanteenDraft) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val currentOnCreateCanteen by rememberUpdatedState(
        newValue = onCreateCanteen
    )

    val cropLauncher = rememberLauncherForActivityResult(
        contract = CropImageContract()
    ) { result ->
        if (result.isSuccessful) {
            val croppedUri = result.uriContent

            if (croppedUri == null) {
                viewModel.onAction(
                    AdminAddCanteenAction.ImageReadingFailed(
                        message = "The cropped image could not be opened."
                    )
                )
            } else {
                viewModel.onAction(
                    AdminAddCanteenAction.ImageReadingStarted
                )

                scope.launch {
                    try {
                        val dimensions = withContext(Dispatchers.IO) {
                            val options = BitmapFactory.Options().apply {
                                inJustDecodeBounds = true
                            }

                            val stream = context.contentResolver
                                .openInputStream(croppedUri)
                                ?: error("Unable to open cropped image.")

                            stream.use {
                                BitmapFactory.decodeStream(
                                    it,
                                    null,
                                    options
                                )
                            }

                            options.outWidth to options.outHeight
                        }

                        viewModel.onAction(
                            AdminAddCanteenAction.ImageSelected(
                                uri = croppedUri.toString(),
                                width = dimensions.first,
                                height = dimensions.second
                            )
                        )
                    } catch (exception: CancellationException) {
                        viewModel.onAction(
                            AdminAddCanteenAction.ImageReadingFailed(
                                message = "Image processing was interrupted. " +
                                        "Please try again."
                            )
                        )

                        throw exception
                    } catch (exception: Exception) {
                        viewModel.onAction(
                            AdminAddCanteenAction.ImageReadingFailed(
                                message = "Unable to read the cropped image. " +
                                        "Please try another image."
                            )
                        )
                    }
                }
            }
        } else if (result.error != null) {
            // Keep any previously selected image.
            viewModel.onAction(
                AdminAddCanteenAction.ImageReadingFailed(
                    message = "Cropping was cancelled or could not be completed. " +
                            "You can choose the image again."
                )
            )
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

    LaunchedEffect(uiState.pendingCanteen) {
        val draft = uiState.pendingCanteen

        if (draft != null) {
            viewModel.onCreateRequestHandled()
            currentOnCreateCanteen(draft)
        }
    }

    AdminAddCanteenScreen(
        uiState = uiState,
        modifier = modifier,
        onAction = { action ->
            when (action) {
                AdminAddCanteenAction.BackClicked -> {
                    onNavigateBack()
                }

                AdminAddCanteenAction.PickImageClicked -> {
                    if (
                        !uiState.isReadingImage &&
                        uiState.pendingCanteen == null
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