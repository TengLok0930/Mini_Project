package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.storeMaintenance.addStore

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
fun AdminAddStoreRoute(
    viewModel: AdminAddStoreViewModel,
    onNavigateBack: () -> Unit,
    onCreateStore: (NewStoreDraft) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val currentOnCreateStore by rememberUpdatedState(
        newValue = onCreateStore
    )

    val cropLauncher = rememberLauncherForActivityResult(
        contract = CropImageContract()
    ) { result ->
        if (result.isSuccessful) {
            val croppedUri = result.uriContent

            if (croppedUri == null) {
                viewModel.onAction(
                    AdminAddStoreAction.ImageReadingFailed(
                        message = "The cropped image could not be opened."
                    )
                )
            } else {
                viewModel.onAction(
                    AdminAddStoreAction.ImageReadingStarted
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
                            AdminAddStoreAction.ImageSelected(
                                uri = croppedUri.toString(),
                                width = dimensions.first,
                                height = dimensions.second
                            )
                        )
                    } catch (exception: CancellationException) {
                        viewModel.onAction(
                            AdminAddStoreAction.ImageReadingFailed(
                                message = "Image processing was interrupted. " +
                                        "Please try again."
                            )
                        )

                        throw exception
                    } catch (exception: Exception) {
                        viewModel.onAction(
                            AdminAddStoreAction.ImageReadingFailed(
                                message = "Unable to read the cropped image. " +
                                        "Please choose another image."
                            )
                        )
                    }
                }
            }
        } else if (result.error != null) {
            viewModel.onAction(
                AdminAddStoreAction.ImageReadingFailed(
                    message = "Cropping was cancelled or could not be completed. " +
                            "Please choose the image again."
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
                        activityTitle = "Crop Store Image",
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

    LaunchedEffect(uiState.pendingStore) {
        val draft = uiState.pendingStore

        if (draft != null) {
            viewModel.onCreateRequestHandled()
            currentOnCreateStore(draft)
        }
    }

    AdminAddStoreScreen(
        uiState = uiState,
        modifier = modifier,
        onAction = { action ->
            when (action) {
                AdminAddStoreAction.BackClicked -> {
                    onNavigateBack()
                }

                AdminAddStoreAction.PickImageClicked -> {
                    if (
                        !uiState.isReadingImage &&
                        uiState.pendingStore == null
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