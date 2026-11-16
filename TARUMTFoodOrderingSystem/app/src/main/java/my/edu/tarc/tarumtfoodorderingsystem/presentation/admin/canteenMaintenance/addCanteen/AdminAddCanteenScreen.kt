package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.canteenMaintenance.addCanteen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppActionButton
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppFieldLabel
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppTopBar

@Composable
fun AdminAddCanteenScreen(
    uiState: AdminAddCanteenUiState,
    onAction: (AdminAddCanteenAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val focusManager = LocalFocusManager.current

    val interactionsEnabled = uiState.pendingCanteen == null

    val submit: () -> Unit = {
        focusManager.clearFocus()
        onAction(AdminAddCanteenAction.CreateClicked)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colors.background,
        topBar = {
            AppTopBar(
                title = "New Canteen",
                onBackClick = {
                    focusManager.clearFocus()
                    onAction(AdminAddCanteenAction.BackClicked)
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .imePadding(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 560.dp)
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(
                            horizontal = 22.dp,
                            vertical = 24.dp
                        )
                ) {
                    AppFieldLabel(
                        title = "Canteen Name",
                        required = true
                    )

                    Spacer(Modifier.height(8.dp))

                    TextField(
                        value = uiState.name,
                        onValueChange = {
                            onAction(
                                AdminAddCanteenAction.NameChanged(it)
                            )
                        },
                        enabled = interactionsEnabled,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholder = {
                            Text("Enter Canteen Name")
                        },
                        isError = uiState.nameError != null,
                        supportingText = uiState.nameError?.let { error ->
                            { Text(error) }
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = colors.surfaceVariant,
                            unfocusedContainerColor = colors.surfaceVariant,
                            disabledContainerColor = colors.surfaceVariant,
                            errorContainerColor = colors.surfaceVariant,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            errorIndicatorColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        )
                    )

                    Spacer(Modifier.height(40.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        ) {
                            AppFieldLabel(
                                title = "Attach image",
                                required = true
                            )
                        }

                        Text(
                            text = "Crop to square\nSaved up to\n100px × 100px",
                            style = MaterialTheme.typography.labelMedium,
                            color = colors.onSurfaceVariant
                        )

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(140.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(Color.White)
                                    .clickable(
                                        enabled = interactionsEnabled &&
                                                !uiState.isReadingImage,
                                        role = Role.Button,
                                        onClickLabel = "Choose canteen image"
                                    ) {
                                        focusManager.clearFocus()
                                        onAction(
                                            AdminAddCanteenAction
                                                .PickImageClicked
                                        )
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                when {
                                    uiState.isReadingImage -> {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(28.dp),
                                            color = colors.primary
                                        )
                                    }

                                    uiState.imageUri != null -> {
                                        AsyncImage(
                                            model = uiState.imageUri,
                                            contentDescription =
                                                "Selected canteen image",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }

                                    else -> {
                                        Text(
                                            text = "Tap to attach",
                                            style = MaterialTheme
                                                .typography
                                                .bodySmall,
                                            color = Color.DarkGray,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }

                            if (
                                uiState.imageUri != null ||
                                uiState.imageError != null
                            ) {
                                TextButton(
                                    enabled = interactionsEnabled &&
                                            !uiState.isReadingImage,
                                    onClick = {
                                        onAction(
                                            AdminAddCanteenAction
                                                .RemoveImageClicked
                                        )
                                    }
                                ) {
                                    Text("Remove image")
                                }
                            }
                        }
                    }

                    uiState.imageError?.let { error ->
                        Spacer(Modifier.height(12.dp))

                        Text(
                            text = error,
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.error
                        )
                    }
                }

                AppActionButton(
                    title = "+ Create New Canteen",
                    onClick = submit,
                    enabled = interactionsEnabled,
                    isLoading = uiState.isReadingImage,
                    modifier = Modifier.padding(
                        horizontal = 22.dp,
                        vertical = 20.dp
                    )
                )
            }
        }
    }
}
