package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.canteen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import my.edu.tarc.tarumtfoodorderingsystem.R
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppTopBar
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.CustomerBottomBar
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.CustomerTab

@Composable
fun CanteenScreen(
    uiState: CanteenUiState,
    onAction: (CanteenAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val canteens = uiState.availableCanteens

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colors.background,
        topBar = {
            AppTopBar(title = "Canteen")
        },
        bottomBar = {
            CustomerBottomBar(
                selectedTab = CustomerTab.HOME,
                onTabSelected = { tab ->
                    when (tab) {
                        CustomerTab.ORDERS -> {
                            onAction(CanteenAction.OrdersClicked)
                        }

                        CustomerTab.HOME -> Unit

                        CustomerTab.PROFILE -> {
                            onAction(CanteenAction.ProfileClicked)
                        }
                    }
                }
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
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .widthIn(max = 560.dp)
                    .fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 18.dp,
                    end = 18.dp,
                    top = 14.dp,
                    bottom = 24.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(22.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item(
                    key = "canteen_heading",
                    span = {
                        GridItemSpan(maxLineSpan)
                    }
                ) {
                    Text(
                        text = "Choose a canteen",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = colors.secondary
                    )
                }

                if (canteens.isEmpty()) {
                    item(
                        key = "canteens_empty",
                        span = {
                            GridItemSpan(maxLineSpan)
                        }
                    ) {
                        Text(
                            text = "No canteens are currently available.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.onSurfaceVariant,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    }
                }

                items(
                    items = canteens,
                    key = { canteen ->
                        canteen.id
                    }
                ) { canteen ->
                    CustomerCanteenCard(
                        canteen = canteen,
                        onClick = {
                            onAction(
                                CanteenAction.CanteenClicked(
                                    canteenId = canteen.id
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun CustomerCanteenCard(
    canteen: CustomerCanteenItem,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.surfaceVariant,
            contentColor = colors.onSurfaceVariant
        )
    ) {
        CustomerCanteenImage(
            canteen = canteen
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 44.dp)
                .padding(
                    horizontal = 10.dp,
                    vertical = 10.dp
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = canteen.name,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun CustomerCanteenImage(
    canteen: CustomerCanteenItem
) {
    val imageModel: Any? = canteen.imageUrl
        ?.takeIf { it.isNotBlank() }
        ?: canteen.imageRes

    val imageModifier = Modifier
        .fillMaxWidth()
        .aspectRatio(1f)

    if (imageModel == null) {
        CanteenImagePlaceholder(
            modifier = imageModifier
        )
    } else {
        val painter = rememberAsyncImagePainter(
            model = imageModel
        )

        Box(
            modifier = imageModifier,
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.foundation.Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            when (painter.state) {
                is AsyncImagePainter.State.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                }

                is AsyncImagePainter.State.Error -> {
                    CanteenImagePlaceholder(
                        modifier = Modifier.fillMaxSize(),
                        message = "Image unavailable"
                    )
                }

                else -> Unit
            }
        }
    }
}

@Composable
private fun CanteenImagePlaceholder(
    modifier: Modifier = Modifier,
    message: String = "Canteen image"
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.home_24px),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = message,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}