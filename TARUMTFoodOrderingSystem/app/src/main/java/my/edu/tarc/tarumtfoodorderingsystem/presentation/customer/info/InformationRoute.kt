package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.info

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun InformationRoute(
    viewModel: InformationViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    InformationScreen(
        uiState = uiState,
        modifier = modifier,
        onAction = { action ->
            when (action) {
                InformationAction.CloseClicked -> {
                    onNavigateBack()
                }

                InformationAction.CampusMapClicked -> {
                    try {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(uiState.campusMapUrl)
                        )

                        context.startActivity(intent)
                    } catch (_: ActivityNotFoundException) {
                        viewModel.onMapOpenFailed()
                    } catch (_: SecurityException) {
                        viewModel.onMapOpenFailed()
                    }
                }

                InformationAction.DismissMapErrorClicked -> {
                    viewModel.onAction(action)
                }
            }
        }
    )
}