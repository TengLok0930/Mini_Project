package my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun WorkerProfileRoute(
    viewModel: WorkerProfileViewModel,
    isDarkTheme: Boolean,
    onDarkThemeChanged: (Boolean) -> Unit,
    onNavigateBack: () -> Unit,
    onLogoutRequested: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    WorkerProfileScreen(
        uiState = uiState,
        isDarkTheme = isDarkTheme,
        modifier = modifier,
        onAction = { action ->
            when (action) {
                WorkerProfileAction.BackClicked -> {
                    onNavigateBack()
                }

                is WorkerProfileAction.ThemeSelected -> {
                    onDarkThemeChanged(action.isDarkTheme)
                }

                WorkerProfileAction.ConfirmLogoutClicked -> {
                    viewModel.onAction(
                        WorkerProfileAction.DismissLogoutClicked
                    )
                    onLogoutRequested()
                }

                else -> {
                    viewModel.onAction(action)
                }
            }
        }
    )
}