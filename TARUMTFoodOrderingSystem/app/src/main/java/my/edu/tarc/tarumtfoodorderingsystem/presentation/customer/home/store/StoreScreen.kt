package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.store

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppCustomerTopBar
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.CustomerBottomBar
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.CustomerTab

@Composable
fun StoreScreen(
    uiState: StoreUiState,
    onAction: (StoreAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val stores = uiState.availableStores

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colors.background,
        topBar = {
            AppCustomerTopBar(
                title = "Choose a Store!",
                subtitle = uiState.canteen?.name,
                onBackClick = {
                    onAction(
                        StoreAction.BackClicked
                    )
                },
                onInformationClick = {
                    onAction(
                        StoreAction.InformationClicked
                    )
                }
            )
        },
        bottomBar = {
            CustomerBottomBar(
                selectedTab = CustomerTab.HOME,
                onTabSelected = { tab ->
                    when (tab) {
                        CustomerTab.ORDERS -> {
                            onAction(StoreAction.OrdersClicked)
                        }

                        CustomerTab.HOME -> {
                            onAction(StoreAction.HomeClicked)
                        }

                        CustomerTab.PROFILE -> {
                            onAction(StoreAction.ProfileClicked)
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
            LazyColumn(
                modifier = Modifier
                    .widthIn(max = 560.dp)
                    .fillMaxSize(),
                contentPadding = PaddingValues(
                    horizontal = 22.dp,
                    vertical = 18.dp
                ),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item(
                    key = "stores_heading",
                    contentType = "heading"
                ) {
                    Text(
                        text = "Available stores (${stores.size})",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = colors.secondary
                    )
                }

                if (uiState.canteen == null) {
                    item(
                        key = "canteen_unavailable",
                        contentType = "message"
                    ) {
                        Text(
                            text = "This canteen is unavailable. " +
                                    "Please return and choose another canteen.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.onSurfaceVariant
                        )
                    }
                } else if (stores.isEmpty()) {
                    item(
                        key = "stores_empty",
                        contentType = "message"
                    ) {
                        Text(
                            text = "No stores are currently available " +
                                    "in this canteen.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.onSurfaceVariant
                        )
                    }
                }

                items(
                    items = stores,
                    key = { store ->
                        store.id
                    },
                    contentType = {
                        "store"
                    }
                ) { store ->
                    CustomerStoreCard(
                        store = store,
                        onClick = {
                            onAction(
                                StoreAction.StoreClicked(
                                    storeId = store.id
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
private fun CustomerStoreCard(
    store: CustomerStoreItem,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.surfaceVariant,
            contentColor = colors.onSurface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 108.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomerStoreImage(
                store = store
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        horizontal = 12.dp,
                        vertical = 14.dp
                    )
            ) {
                Text(
                    text = store.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colors.onSurface
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "${store.foodCount} food(s) available!",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.onSurfaceVariant
                )

                Spacer(Modifier.height(5.dp))

                Text(
                    text = "${store.beverageCount} beverage(s) available!",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun CustomerStoreImage(
    store: CustomerStoreItem
) {
    val imageModel: Any? = store.imageUrl
        ?.takeIf { it.isNotBlank() }
        ?: store.imageRes

    Box(
        modifier = Modifier
            .size(108.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
    ) {
        if (imageModel != null) {
            AsyncImage(
                model = imageModel,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}