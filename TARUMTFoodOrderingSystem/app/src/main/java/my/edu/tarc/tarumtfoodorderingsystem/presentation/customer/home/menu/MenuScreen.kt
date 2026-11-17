package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import java.math.BigDecimal
import java.util.Locale
import my.edu.tarc.tarumtfoodorderingsystem.R
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppTopBar

@Composable
fun MenuScreen(
    uiState: MenuUiState,
    onAction: (MenuAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    val storeItems = uiState.storeItems
    val topSellingItems = uiState.topSellingItems
    val categories = uiState.itemsByCategory

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colors.background,
        topBar = {
            AppTopBar(
                title = "Choose an Item!",
                subtitle = uiState.storeSubtitle,
                onBackClick = {
                    onAction(MenuAction.BackClicked)
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onAction(MenuAction.CartClicked)
                },
                modifier = Modifier.size(72.dp),
                shape = CircleShape,
                containerColor = colors.primary,
                contentColor = colors.onPrimary
            ) {
                Icon(
                    painter = painterResource(R.drawable.cart_40px),
                    contentDescription = "Open cart",
                    modifier = Modifier.size(34.dp)
                )
            }
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
                    top = 24.dp,
                    bottom = 112.dp
                )
            ) {
                if (storeItems.isEmpty()) {
                    item(
                        key = "menu_empty",
                        contentType = "message"
                    ) {
                        Text(
                            text = "No menu items are currently available " +
                                    "in this store.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.onSurfaceVariant,
                            modifier = Modifier.padding(
                                horizontal = 20.dp
                            )
                        )
                    }
                }

                if (topSellingItems.isNotEmpty()) {
                    item(
                        key = "top_selling_heading",
                        contentType = "heading"
                    ) {
                        MenuSectionHeading(
                            title = "Top selling items 🔥"
                        )
                    }

                    items(
                        items = topSellingItems,
                        key = { item ->
                            "top_selling_${item.id}"
                        },
                        contentType = {
                            "menu_item"
                        }
                    ) { item ->
                        CustomerMenuItemRow(
                            item = item,
                            onClick = {
                                onAction(
                                    MenuAction.ItemClicked(
                                        itemId = item.id
                                    )
                                )
                            }
                        )
                    }
                }

                categories.forEach { (category, categoryItems) ->
                    item(
                        key = "category_heading_$category",
                        contentType = "heading"
                    ) {
                        MenuSectionHeading(
                            title = "$category (${categoryItems.size})",
                            modifier = Modifier.padding(
                                top = 24.dp
                            )
                        )
                    }

                    items(
                        items = categoryItems,
                        key = { item ->
                            "category_item_${item.id}"
                        },
                        contentType = {
                            "menu_item"
                        }
                    ) { item ->
                        CustomerMenuItemRow(
                            item = item,
                            onClick = {
                                onAction(
                                    MenuAction.ItemClicked(
                                        itemId = item.id
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MenuSectionHeading(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.secondary,
        modifier = modifier.padding(
            start = 20.dp,
            end = 20.dp,
            bottom = 10.dp
        )
    )
}

@Composable
private fun CustomerMenuItemRow(
    item: CustomerMenuItem,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    val priceColor = if (colors.background.luminance() < 0.5f) {
        Color(0xFF4CF05A)
    } else {
        Color(0xFF08752D)
    }

    val formattedPrice = remember(item.priceInSen) {
        String.format(
            Locale.US,
            "RM %,.2f",
            BigDecimal.valueOf(item.priceInSen, 2)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                enabled = item.isAvailable,
                onClick = onClick
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 18.dp,
                    vertical = 16.dp
                ),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomerMenuItemImage(
                item = item
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (item.isAvailable) {
                        colors.onSurface
                    } else {
                        colors.onSurfaceVariant
                    }
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "${item.quantitySold} sold",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.onSurfaceVariant
                )

                Spacer(Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = if (item.isAvailable) {
                            "Quantity left: ${item.quantityRemaining}"
                        } else {
                            "Sold out"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = if (item.isAvailable) {
                            colors.onSurfaceVariant
                        } else {
                            colors.error
                        },
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = formattedPrice,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (item.isAvailable) {
                            priceColor
                        } else {
                            colors.onSurfaceVariant
                        }
                    )
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(
                horizontal = 10.dp
            ),
            color = colors.outlineVariant
        )
    }
}

@Composable
private fun CustomerMenuItemImage(
    item: CustomerMenuItem
) {
    val imageModel: Any? = item.imageUrl
        ?.takeIf { it.isNotBlank() }
        ?: item.imageRes

    Box(
        modifier = Modifier
            .size(84.dp)
            .clip(RoundedCornerShape(18.dp))
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