package my.edu.tarc.tarumtfoodorderingsystem.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import my.edu.tarc.tarumtfoodorderingsystem.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onBackClick: (() -> Unit)? = null,
    onLogoutClick: (() -> Unit)? = null,
    @DrawableRes backIconResId: Int =
        if (subtitle.isNullOrBlank()) {
            R.drawable.arrow_back_48px
        } else {
            R.drawable.arrow_back_40px
        }
) {
    val colors = MaterialTheme.colorScheme
    val hasSubtitle = !subtitle.isNullOrBlank()

    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colors.primary,
            titleContentColor = colors.onPrimary,
            navigationIconContentColor = colors.onPrimary,
            actionIconContentColor = colors.onPrimary
        ),
        title = {
            Column(
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (hasSubtitle) {
                    Spacer(Modifier.height(2.dp))

                    Text(
                        text = subtitle.orEmpty(),
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.onPrimary.copy(alpha = 0.8f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        },
        navigationIcon = {
            if (onBackClick != null) {
                IconButton(
                    onClick = onBackClick
                ) {
                    Icon(
                        painter = painterResource(backIconResId),
                        contentDescription = "Back",
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        },
        actions = {
            if (onLogoutClick != null) {
                IconButton(
                    onClick = onLogoutClick
                ) {
                    Icon(
                        painter = painterResource(
                            R.drawable.logout_48px
                        ),
                        contentDescription = "Log out",
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppCustomerTopBar(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onBackClick: (() -> Unit)? = null,
    onInformationClick: (() -> Unit)? = null,
    @DrawableRes backIconResId: Int =
        if (subtitle.isNullOrBlank()) {
            R.drawable.arrow_back_48px
        } else {
            R.drawable.arrow_back_40px
        },
    backContentDescription: String = "Back"
) {
    val colors = MaterialTheme.colorScheme
    val hasSubtitle = !subtitle.isNullOrBlank()

    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colors.primary,
            titleContentColor = colors.onPrimary,
            navigationIconContentColor = colors.onPrimary,
            actionIconContentColor = colors.onPrimary
        ),
        title = {
            Column(
                modifier = Modifier.padding(
                    vertical = 4.dp
                )
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (hasSubtitle) {
                    Spacer(
                        modifier = Modifier.height(2.dp)
                    )

                    Text(
                        text = subtitle.orEmpty(),
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.onPrimary.copy(
                            alpha = 0.8f
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        },
        navigationIcon = {
            if (onBackClick != null) {
                IconButton(
                    onClick = onBackClick
                ) {
                    Icon(
                        painter = painterResource(
                            id = backIconResId
                        ),
                        contentDescription = backContentDescription,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        },
        actions = {
            if (onInformationClick != null) {
                IconButton(
                    onClick = onInformationClick
                ) {
                    Icon(
                        painter = painterResource(
                            id = R.drawable.info_40px
                        ),
                        contentDescription = "Ordering information",
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }
    )
}
