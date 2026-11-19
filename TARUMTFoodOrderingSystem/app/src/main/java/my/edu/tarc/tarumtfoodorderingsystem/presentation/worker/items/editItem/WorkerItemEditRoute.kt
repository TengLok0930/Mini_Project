package my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.items.editItem

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun WorkerItemEditRoute(
    viewModel: WorkerItemEditViewModel,
    onNavigateBack: () -> Unit,
    onSaveChanges: (WorkerItemEditValues) -> Boolean,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    WorkerItemEditScreen(
        uiState = uiState,
        modifier = modifier,
        onAction = { action ->
            when (action) {
                WorkerItemEditAction.BackClicked -> {
                    onNavigateBack()
                }

                WorkerItemEditAction.SaveChangesClicked -> {
                    val values = viewModel.validateForSave()

                    if (values != null) {
                        val saved = onSaveChanges(values)

                        if (saved) {
                            onNavigateBack()
                        } else {
                            viewModel.onSaveFailed()
                        }
                    }
                }

                else -> {
                    viewModel.onAction(action)
                }
            }
        }
    )
}