package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.menu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.canteen.CanteenAction

@Composable
fun MenuRoute(
    viewModel: MenuViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToItemDetails: (Int) -> Unit,
    onNavigateToInformation: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MenuScreen(
        uiState = uiState,
        modifier = modifier,
        onAction = { action ->
            when (action) {
                MenuAction.BackClicked -> {
                    onNavigateBack()
                }

                MenuAction.CartClicked -> {
                    onNavigateToCart()
                }

                is MenuAction.ItemClicked -> {
                    val selectedItem = uiState.storeItems.firstOrNull {
                        it.id == action.itemId
                    }

                    if (selectedItem != null && selectedItem.isAvailable) {
                        onNavigateToItemDetails(selectedItem.id)
                    }
                }

                MenuAction.InformationClicked -> {
                    onNavigateToInformation()
                }
            }
        }
    )
}