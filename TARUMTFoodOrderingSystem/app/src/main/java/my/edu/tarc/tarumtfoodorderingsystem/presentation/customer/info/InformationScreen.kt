package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.info

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import my.edu.tarc.tarumtfoodorderingsystem.R
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppCustomerTopBar
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppNoticeDialog

@Composable
fun InformationScreen(
    uiState: InformationUiState,
    onAction: (InformationAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colors.background,
        topBar = {
            AppCustomerTopBar(
                title = "Tips for you",
                onBackClick = {
                    onAction(
                        InformationAction.CloseClicked
                    )
                },
                backIconResId = R.drawable.close_40px,
                backContentDescription = "Close information"
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 560.dp)
                    .fillMaxSize()
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(
                        horizontal = 20.dp,
                        vertical = 22.dp
                    )
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    color = colors.surfaceVariant,
                    border = BorderStroke(
                        width = 1.dp,
                        color = colors.outlineVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(22.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Text(
                            text = "Note:",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = colors.secondary
                        )

                        HorizontalDivider(
                            color = colors.outlineVariant
                        )

                        Text(
                            text = "Here is some useful information " +
                                    "before placing your order.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = colors.onSurface
                        )

                        Text(
                            text = "How to order",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = colors.onSurface
                        )

                        Column(
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            uiState.orderingSteps.forEachIndexed {
                                    index, instruction ->
                                InformationStep(
                                    number = index + 1,
                                    instruction = instruction
                                )
                            }
                        }

                        HorizontalDivider(
                            color = colors.outlineVariant
                        )

                        Text(
                            text = "General canteen operating hours",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = colors.onSurface
                        )

                        Text(
                            text = uiState.operatingHours,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.secondary
                        )

                        Text(
                            text = uiState.operatingHoursNote,
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.onSurfaceVariant
                        )

                        HorizontalDivider(
                            color = colors.outlineVariant
                        )

                        Text(
                            text = "Finding your way",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = colors.onSurface
                        )

                        Text(
                            text = "Use the KL Campus Map to help " +
                                    "you find your way around campus.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.onSurfaceVariant
                        )

                        TextButton(
                            onClick = {
                                onAction(
                                    InformationAction.CampusMapClicked
                                )
                            },
                            modifier = Modifier
                                .align(Alignment.Start)
                                .heightIn(min = 48.dp),
                            contentPadding = PaddingValues(
                                horizontal = 0.dp,
                                vertical = 8.dp
                            )
                        ) {
                            Text(
                                text = "View KL Campus Map",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = colors.secondary
                            )
                        }
                    }
                }
            }
        }
    }

    if (uiState.showMapError) {
        AppNoticeDialog(
            title = "Unable to open campus map",
            message = "No suitable app could open the map link, " +
                    "or opening it was blocked. Please try opening " +
                    "the link in a browser.",
            confirmText = "OK",
            onClose = {
                onAction(
                    InformationAction.DismissMapErrorClicked
                )
            }
        )
    }
}

@Composable
private fun InformationStep(
    number: Int,
    instruction: String
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "$number.",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = colors.secondary,
            modifier = Modifier.widthIn(
                min = 24.dp
            )
        )

        Text(
            text = instruction,
            style = MaterialTheme.typography.bodyLarge,
            color = colors.onSurface,
            modifier = Modifier.weight(1f)
        )
    }
}