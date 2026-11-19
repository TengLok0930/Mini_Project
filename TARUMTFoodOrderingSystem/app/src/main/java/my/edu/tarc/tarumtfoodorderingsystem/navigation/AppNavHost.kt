package my.edu.tarc.tarumtfoodorderingsystem.navigation

import android.content.pm.ApplicationInfo
import android.widget.Toast
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.io.IOException
import my.edu.tarc.tarumtfoodorderingsystem.data.local.UserPreferencesRepository
import my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.adminPanel.AdminPanelRoute
import my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.adminPanel.AdminPanelViewModel
import my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.canteenMaintenance.AdminCanteenRoute
import my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.canteenMaintenance.AdminCanteenViewModel
import my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.canteenMaintenance.addCanteen.AdminAddCanteenRoute
import my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.canteenMaintenance.addCanteen.AdminAddCanteenViewModel
import my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.canteenMaintenance.editCanteen.AdminEditCanteenRoute
import my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.canteenMaintenance.editCanteen.AdminEditCanteenViewModel
import my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.menuMaintenance.AdminMenuRoute
import my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.menuMaintenance.AdminMenuViewModel
import my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.menuMaintenance.addMenuItem.AdminAddItemRoute
import my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.menuMaintenance.addMenuItem.AdminAddItemViewModel
import my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.orderReporting.AdminReportRoute
import my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.orderReporting.AdminReportViewModel
import my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.storeMaintenance.AdminStoreRoute
import my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.storeMaintenance.AdminStoreViewModel
import my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.storeMaintenance.addStore.AdminAddStoreRoute
import my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.storeMaintenance.addStore.AdminAddStoreViewModel
import my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.storeMaintenance.editStore.AdminEditStoreRoute
import my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.storeMaintenance.editStore.AdminEditStoreViewModel
import my.edu.tarc.tarumtfoodorderingsystem.presentation.auth.LoginAuthenticator
import my.edu.tarc.tarumtfoodorderingsystem.presentation.auth.LoginRoute
import my.edu.tarc.tarumtfoodorderingsystem.presentation.auth.LoginViewModel
import my.edu.tarc.tarumtfoodorderingsystem.presentation.auth.TestLoginRole
import my.edu.tarc.tarumtfoodorderingsystem.presentation.auth.emailLogin.EmailLoginRoute
import my.edu.tarc.tarumtfoodorderingsystem.presentation.auth.emailLogin.EmailLoginViewModel
import my.edu.tarc.tarumtfoodorderingsystem.presentation.auth.otp.OTPLoginRoute
import my.edu.tarc.tarumtfoodorderingsystem.presentation.auth.otp.OTPLoginViewModel
import my.edu.tarc.tarumtfoodorderingsystem.presentation.auth.register.RegisterRoute
import my.edu.tarc.tarumtfoodorderingsystem.presentation.auth.register.RegisterViewModel
import my.edu.tarc.tarumtfoodorderingsystem.presentation.auth.register.RegistrationService
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.canteen.CanteenRoute
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.canteen.CanteenViewModel
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.cart.CartRoute
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.cart.CartViewModel
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.menu.MenuRoute
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.menu.MenuViewModel
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.orderItem.OrderItemRoute
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.orderItem.OrderItemViewModel
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.payment.PaymentRoute
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.payment.PaymentViewModel
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.payment.paymentSuccess.PaymentSuccessRoute
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.payment.paymentSuccess.PaymentSuccessViewModel
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.store.StoreRoute
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.store.StoreViewModel
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.orders.OrdersRoute
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.orders.OrdersViewModel
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.orders.purchaseHistory.PurchaseHistoryRoute
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.orders.purchaseHistory.PurchaseHistoryViewModel
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.profile.CustomerProfileRoute
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.profile.CustomerProfileViewModel
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.profile.voucher.VoucherRoute
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.profile.voucher.VoucherViewModel
import my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.completedOrders.WorkerCompletedOrderRoute
import my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.completedOrders.WorkerCompletedOrderViewModel
import my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.orders.WorkerOrderRoute
import my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.orders.WorkerOrderViewModel
import my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.orders.pendingOrders.WorkerPendingOrderAction
import my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.orders.pendingOrders.WorkerPendingOrderRoute
import my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.orders.pendingOrders.WorkerPendingOrderViewModel
import my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.orders.pickupOrders.WorkerPickupOrderRoute
import my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.orders.pickupOrders.WorkerPickupOrderViewModel

object AppRoutes {

    // Auth
    const val AUTH_GATE = "auth_gate"
    const val LOGIN = "login"
    const val EMAIL_LOGIN = "email_login"
    const val OTP_LOGIN = "otp_login"
    const val REGISTER = "register"

    // Customer
    const val CUSTOMER = "customer"
    const val CUSTOMER_STORES = "customer_stores/{canteenId}"

    fun customerStores(canteenId: Int): String {
        return "customer_stores/$canteenId"
    }

    const val CUSTOMER_MENU = "customer_menu/{storeId}?storeName={storeName}&canteenName={canteenName}"

    fun customerMenu(
        storeId: Int,
        storeName: String,
        canteenName: String
    ): String {
        val encodedStoreName = android.net.Uri.encode(storeName)
        val encodedCanteenName = android.net.Uri.encode(canteenName)

        return "customer_menu/$storeId" +
                "?storeName=$encodedStoreName" +
                "&canteenName=$encodedCanteenName"
    }

    const val CUSTOMER_ORDER_ITEM =
        "customer_order_item/{itemId}"

    fun customerOrderItem(
        itemId: Int
    ): String {
        return "customer_order_item/$itemId"
    }

    const val CUSTOMER_CART =
        "customer_cart/{storeId}?storeName={storeName}&canteenName={canteenName}"

    fun customerCart(
        storeId: Int,
        storeName: String,
        canteenName: String
    ): String {
        val encodedStoreName = android.net.Uri.encode(storeName)
        val encodedCanteenName = android.net.Uri.encode(canteenName)

        return "customer_cart/$storeId" +
                "?storeName=$encodedStoreName" +
                "&canteenName=$encodedCanteenName"
    }

    const val CUSTOMER_PAYMENT = "customer_payment"
    const val CUSTOMER_PAYMENT_SUCCESS = "customer_payment_success/{orderId}"

    fun customerPaymentSuccess(orderId: Int): String {
        return "customer_payment_success/$orderId"
    }

    // Order side
    const val CUSTOMER_ORDERS = "customer_orders"
    const val CUSTOMER_PURCHASE_HISTORY = "customer_purchase_history"

    // Profile side
    const val CUSTOMER_PROFILE = "customer_profile"
    const val CUSTOMER_VOUCHERS = "customer_vouchers"

    // Worker
    const val WORKER = "worker"
    const val WORKER_ACTIVE_ORDERS = "worker_active_orders"
    const val WORKER_PICKUPS = "worker_pickups"

    // Completed Order side
    const val WORKER_COMPLETED_ORDERS = "worker_completed_orders"

    // Items side
    const val WORKER_ITEMS = "worker_items"

    // Admin
    const val ADMIN = "admin"
    const val CANTEEN_MAINTENANCE = "canteen_maintenance"
    const val CREATE_CANTEEN = "create_canteen"
    const val EDIT_CANTEEN = "edit_canteen/{canteenId}"

    fun editCanteen(canteenId: Int): String {
        return "edit_canteen/$canteenId"
    }

    const val STORE_MAINTENANCE = "store_maintenance"
    const val CREATE_STORE = "create_store/{canteenId}"

    fun createStore(canteenId: Int): String {
        return "create_store/$canteenId"
    }

    const val EDIT_STORE = "edit_store/{storeId}"

    fun editStore(storeId: Int): String {
        return "edit_store/$storeId"
    }
    const val MENU_MAINTENANCE = "menu_maintenance"
    const val CREATE_MENU_ITEM = "create_menu_item/{storeId}"

    fun createMenuItem(storeId: Int): String {
        return "create_menu_item/$storeId"
    }
    const val ORDER_REPORTING = "order_reporting"
}

// When implementing backend remember to delete authRepository, registrationService (they are placeholders for front end)

@Composable
fun AppNavHost(
    authRepository: LoginAuthenticator,
    registrationService: RegistrationService,
    userPreferencesRepository: UserPreferencesRepository,
    isDarkTheme: Boolean,
    onDarkThemeChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
){
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Navigation helpers
    fun navigateAfterLogin(role: TestLoginRole) {
        scope.launch {
            try {
                userPreferencesRepository.saveLogin(role.name)
            } catch (exception: IOException) {
                Toast.makeText(
                    context,
                    "Signed in, but your login could not be remembered.",
                    Toast.LENGTH_LONG
                ).show()
            }

            val destination = when (role) {
                TestLoginRole.CUSTOMER -> AppRoutes.CUSTOMER
                TestLoginRole.WORKER -> AppRoutes.WORKER
                TestLoginRole.ADMIN -> AppRoutes.ADMIN
            }

            navController.navigate(destination) {
                popUpTo(AppRoutes.LOGIN) {
                    inclusive = true
                }

                launchSingleTop = true
            }
        }
    }

    fun navigateToCustomerOrders() {
        navController.navigate(AppRoutes.CUSTOMER_ORDERS) {
            popUpTo(AppRoutes.CUSTOMER) {
                inclusive = false
            }

            launchSingleTop = true
        }
    }

    fun navigateToCustomerProfile() {
        navController.navigate(AppRoutes.CUSTOMER_PROFILE) {
            popUpTo(AppRoutes.CUSTOMER) {
                inclusive = false
            }

            launchSingleTop = true
        }
    }

    fun navigateToWorkerDestination(
        route: String
    ) {
        if (route == AppRoutes.WORKER) {
            navController.popBackStack(
                route = AppRoutes.WORKER,
                inclusive = false
            )
        } else {
            navController.navigate(route) {
                popUpTo(AppRoutes.WORKER) {
                    inclusive = false
                }

                launchSingleTop = true
            }
        }
    }

    fun logoutUser() {
        scope.launch {
            try {
                userPreferencesRepository.clearLogin()

                navController.navigate(AppRoutes.LOGIN) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }

                    launchSingleTop = true
                }
            } catch (exception: IOException) {
                Toast.makeText(
                    context,
                    "Unable to clear your saved login. Please try again.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = AppRoutes.AUTH_GATE,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        enterTransition = {
            fadeIn(
                animationSpec = tween(280)
            ) + slideInHorizontally(
                initialOffsetX = { width -> width / 12 },
                animationSpec = tween(280)
            )
        },
        exitTransition = {
            fadeOut(
                animationSpec = tween(220)
            ) + slideOutHorizontally(
                targetOffsetX = { width -> width / 12 },
                animationSpec = tween(280)
            )
        },
        popEnterTransition = {
            fadeIn(
                animationSpec = tween(280)
            ) + slideInHorizontally(
                initialOffsetX = { width -> -width / 12 },
                animationSpec = tween(280)
            )
        },
        popExitTransition = {
            fadeOut(
                animationSpec = tween(220)
            ) + slideOutHorizontally(
                targetOffsetX = { width -> width / 12 },
                animationSpec = tween(280)
            )
        }
    ) {
        composable(
            route = AppRoutes.AUTH_GATE,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            LaunchedEffect(userPreferencesRepository) {
                val role = userPreferencesRepository.userRole.first()

                val destination = when (role) {
                    "CUSTOMER" -> AppRoutes.CUSTOMER
                    "WORKER" -> AppRoutes.WORKER
                    "ADMIN" -> AppRoutes.ADMIN
                    else -> AppRoutes.LOGIN
                }

                navController.navigate(destination) {
                    popUpTo(AppRoutes.AUTH_GATE) {
                        inclusive = true
                    }

                    launchSingleTop = true
                }
            }

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }

        /////////////////////////////////////////////////////////
        //////////////////// AUTH SCREENS ///////////////////////
        /////////////////////////////////////////////////////////
        composable(
            route = AppRoutes.LOGIN,
            exitTransition = {
                if (targetState.destination.route == AppRoutes.EMAIL_LOGIN) {
                    ExitTransition.None
                } else {
                    null
                }
            },
            popEnterTransition = {
                if (initialState.destination.route == AppRoutes.EMAIL_LOGIN) {
                    EnterTransition.None
                } else {
                    null
                }
            }
        ) {
            val loginViewModel: LoginViewModel = viewModel {
                LoginViewModel(
                    authenticator = authRepository
                )
            }

            LoginRoute(
                viewModel = loginViewModel,
                isDarkTheme = isDarkTheme,
                onDarkThemeChanged = onDarkThemeChanged,
                onNavigateToOTPLogin = {
                    navController.navigate(AppRoutes.EMAIL_LOGIN) {
                        launchSingleTop = true
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(AppRoutes.REGISTER) {
                        launchSingleTop = true
                    }
                },
                onLoginSuccess = { role ->
                    navigateAfterLogin(role)
                }
            )
        }

        composable(
            route = AppRoutes.EMAIL_LOGIN,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            val emailLoginViewModel: EmailLoginViewModel = viewModel()

            EmailLoginRoute(
                viewModel = emailLoginViewModel,
                isDarkTheme = isDarkTheme,
                onDarkThemeChanged = onDarkThemeChanged,
                onBackToPasswordLogin = {
                    navController.popBackStack()
                },
                onContinueToOTP = { email ->
                    // Temporarily hand the validated email to the OTP destination. (Need change later on backend)
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("otp_email", email)

                    navController.navigate(AppRoutes.OTP_LOGIN) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(AppRoutes.OTP_LOGIN) { backStackEntry ->
            val context = LocalContext.current

            val isDebuggable = (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0

            val email = remember(backStackEntry) {
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<String>("otp_email")
                    .orEmpty()
            }

            if (email.isBlank()) {
                PlaceholderScreen(
                    title = "Email missing",
                    message = "Go back and enter your registered email again.",
                    onBack = {
                        navController.popBackStack()
                    }
                )
            } else {
                val otpViewModel: OTPLoginViewModel = viewModel {
                    OTPLoginViewModel(
                        email = email,
                        enableDebugTesting = isDebuggable
                    )
                }

                OTPLoginRoute(
                    viewModel = otpViewModel,

                    onNavigateBack = {
                        navController.popBackStack()
                    },

                    onChangeEmail = {
                        navController.popBackStack()
                    },

                    onVerificationSuccess = {
                        val role = if (
                            email.trim().equals(
                                other = "worker@tarc.edu.my",
                                ignoreCase = true
                            )
                        ) {
                            TestLoginRole.WORKER
                        } else {
                            TestLoginRole.CUSTOMER
                        }

                        navigateAfterLogin(role)
                    }
                )
            }
        }

        composable(AppRoutes.REGISTER) {
            val registerViewModel: RegisterViewModel = viewModel {
                RegisterViewModel(
                    registrationService = registrationService
                )
            }

            RegisterRoute(
                viewModel = registerViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onRegistrationSuccess = {
                    navController.popBackStack(
                        route = AppRoutes.LOGIN,
                        inclusive = false
                    )
                }
            )
        }

        /////////////////////////////////////////////////////////
        ////////////////// CUSTOMER SCREEN //////////////////////
        /////////////////////////////////////////////////////////

        composable(AppRoutes.CUSTOMER) {
            val context = LocalContext.current

            val canteenViewModel: CanteenViewModel = viewModel()

            CanteenRoute(
                viewModel = canteenViewModel,
                onNavigateToStores = { canteenId ->
                    navController.navigate(AppRoutes.customerStores(canteenId)) {
                        launchSingleTop = true
                    }
                },
                onNavigateToOrders = {
                    navigateToCustomerOrders()
                },
                onNavigateToProfile = {
                    navigateToCustomerProfile()
                }
            )
        }

        // Home side
        composable(
            route = AppRoutes.CUSTOMER_STORES,
            arguments = listOf(
                navArgument("canteenId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val context = LocalContext.current

            val canteenId = requireNotNull(
                backStackEntry.arguments
            ).getInt("canteenId")

            val storeViewModel: StoreViewModel = viewModel {
                StoreViewModel(canteenId = canteenId)
            }

            StoreRoute(
                viewModel = storeViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToMenu = { storeId ->
                    val state = storeViewModel.uiState.value

                    val selectedStore = state.availableStores.firstOrNull {
                        it.id == storeId
                    }

                    val selectedCanteen = state.canteen

                    if (selectedStore != null && selectedCanteen != null) {
                        navController.navigate(
                            AppRoutes.customerMenu(
                                storeId = selectedStore.id,
                                storeName = selectedStore.name,
                                canteenName = selectedCanteen.name
                            )
                        ) {
                            launchSingleTop = true
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "This store is unavailable. Please select another store.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                onNavigateToOrders = {
                    navigateToCustomerOrders()
                },
                onNavigateToHome = {
                    navController.popBackStack(
                        route = AppRoutes.CUSTOMER,
                        inclusive = false
                    )
                },
                onNavigateToProfile = {
                    navigateToCustomerProfile()
                }
            )
        }

        composable(
            route = AppRoutes.CUSTOMER_MENU,
            arguments = listOf(
                navArgument("storeId") {
                    type = NavType.IntType
                },
                navArgument("storeName") {
                    type = NavType.StringType
                    defaultValue = "Store"
                },
                navArgument("canteenName") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val arguments = requireNotNull(
                backStackEntry.arguments
            )

            val storeId = arguments.getInt("storeId")

            val storeName = arguments.getString("storeName")
                ?: "Store"

            val canteenName = arguments.getString("canteenName")
                .orEmpty()

            val menuViewModel: MenuViewModel = viewModel {
                MenuViewModel(
                    storeId = storeId,
                    storeName = storeName,
                    canteenName = canteenName
                )
            }

            MenuRoute(
                viewModel = menuViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToCart = {
                    val state = menuViewModel.uiState.value

                    navController.navigate(
                        AppRoutes.customerCart(
                            storeId = state.storeId,
                            storeName = state.storeName,
                            canteenName = state.canteenName
                        )
                    ) {
                        launchSingleTop = true
                    }
                },
                onNavigateToItemDetails = { itemId ->
                    navController.navigate(
                        AppRoutes.customerOrderItem(
                            itemId = itemId
                        )
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = AppRoutes.CUSTOMER_ORDER_ITEM,
            arguments = listOf(
                navArgument("itemId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val itemId = requireNotNull(
                backStackEntry.arguments
            ).getInt("itemId")

            val menuEntry = remember(backStackEntry) {
                navController.getBackStackEntry(
                    AppRoutes.CUSTOMER_MENU
                )
            }

            val menuViewModel = customerMenuViewModel(
                menuEntry = menuEntry
            )

            val cartViewModel = customerCartViewModel(
                menuEntry = menuEntry
            )

            val cartState by cartViewModel.uiState.collectAsStateWithLifecycle()

            val item = menuViewModel.uiState.value.storeItems
                .firstOrNull {
                    it.id == itemId
                }

            if (item == null) {
                PlaceholderScreen(
                    title = "Item unavailable",
                    message = "Return to the menu and select another item.",
                    onBack = {
                        navController.popBackStack()
                    }
                )
            } else {
                val quantityAlreadyInCart = cartState.items
                    .filter {
                        it.menuItemId == item.id
                    }
                    .sumOf {
                        it.quantity
                    }

                val availableQuantity = (
                        item.quantityRemaining - quantityAlreadyInCart
                        ).coerceAtLeast(0)

                val orderItemViewModel: OrderItemViewModel = viewModel {
                    OrderItemViewModel(
                        item = item
                    )
                }

                OrderItemRoute(
                    viewModel = orderItemViewModel,
                    availableQuantity = availableQuantity,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onAddToCart = { draft ->
                        val added = cartViewModel.addItem(
                            draft = draft
                        )

                        if (added) {
                            Toast.makeText(
                                context,
                                "Added to cart",
                                Toast.LENGTH_SHORT
                            ).show()

                            navController.popBackStack()
                        } else {
                            Toast.makeText(
                                context,
                                "Unable to add this quantity. Please check availability.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                )
            }
        }

        // Cart side (From menu)
        composable(
            route = AppRoutes.CUSTOMER_CART,
            arguments = listOf(
                navArgument("storeId") {
                    type = NavType.IntType
                },
                navArgument("storeName") {
                    type = NavType.StringType
                    defaultValue = "Store"
                },
                navArgument("canteenName") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val menuEntry = remember(backStackEntry) {
                navController.getBackStackEntry(
                    AppRoutes.CUSTOMER_MENU
                )
            }

            val cartViewModel = customerCartViewModel(
                menuEntry = menuEntry
            )

            CartRoute(
                viewModel = cartViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToPayment = {
                    navController.navigate(
                        AppRoutes.CUSTOMER_PAYMENT
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }

        // Payment
        composable(
            route = AppRoutes.CUSTOMER_PAYMENT
        ) { backStackEntry ->
            val menuEntry = remember(backStackEntry) {
                navController.getBackStackEntry(
                    AppRoutes.CUSTOMER_MENU
                )
            }

            val customerEntry = remember(backStackEntry) {
                navController.getBackStackEntry(
                    AppRoutes.CUSTOMER
                )
            }

            val cartViewModel = customerCartViewModel(
                menuEntry = menuEntry
            )

            val ordersViewModel: OrdersViewModel = viewModel(
                viewModelStoreOwner = customerEntry
            )

            val paymentViewModel: PaymentViewModel = viewModel {
                PaymentViewModel(
                    cartState = cartViewModel.uiState
                )
            }

            PaymentRoute(
                viewModel = paymentViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onRequestPayment = { request ->
                    val cartState = cartViewModel.uiState.value

                    val order = ordersViewModel.completeDemoPayment(
                        request = request,
                        storeName = cartState.storeName,
                        canteenName = cartState.canteenName
                    )

                    if (order == null) {
                        paymentViewModel.allowPaymentRetry()

                        Toast.makeText(
                            context,
                            "All pickup numbers are currently in use. Please try again later.",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        navController.navigate(
                            AppRoutes.customerPaymentSuccess(
                                orderId = order.id
                            )
                        ) {
                            popUpTo(AppRoutes.CUSTOMER) {
                                inclusive = false
                            }

                            launchSingleTop = true
                        }
                    }
                }
            )
        }

        composable(
            route = AppRoutes.CUSTOMER_PAYMENT_SUCCESS,
            arguments = listOf(
                navArgument("orderId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val orderId = requireNotNull(
                backStackEntry.arguments
            ).getInt("orderId")

            val customerEntry = remember(backStackEntry) {
                navController.getBackStackEntry(
                    AppRoutes.CUSTOMER
                )
            }

            val ordersViewModel: OrdersViewModel = viewModel(
                viewModelStoreOwner = customerEntry
            )

            val order = ordersViewModel.uiState.value.orders
                .firstOrNull {
                    it.id == orderId
                }

            if (order == null) {
                PlaceholderScreen(
                    title = "Receipt unavailable",
                    message = "This demo receipt is no longer available. " +
                            "Orders are stored in memory only.",
                    onBack = {
                        navigateToCustomerOrders()
                    }
                )
            } else {
                val paymentSuccessViewModel: PaymentSuccessViewModel = viewModel {
                    PaymentSuccessViewModel(
                        order = order
                    )
                }

                PaymentSuccessRoute(
                    viewModel = paymentSuccessViewModel,
                    onNavigateToOrders = {
                        navigateToCustomerOrders()
                    }
                )
            }
        }

        // Order side
        composable(
            route = AppRoutes.CUSTOMER_ORDERS,
            exitTransition = {
                if (targetState.destination.route == AppRoutes.CUSTOMER_PURCHASE_HISTORY) {
                    ExitTransition.None
                } else {
                    null
                }
            },
            popEnterTransition = {
                if (initialState.destination.route == AppRoutes.CUSTOMER_PURCHASE_HISTORY) {
                    EnterTransition.None
                } else {
                    null
                }
            }
        ) {
            val customerEntry = remember(navController) {
                navController.getBackStackEntry(
                    AppRoutes.CUSTOMER
                )
            }

            val ordersViewModel: OrdersViewModel = viewModel(
                viewModelStoreOwner = customerEntry
            )

            OrdersRoute(
                viewModel = ordersViewModel,
                isDarkTheme = isDarkTheme,
                onNavigateToPurchaseHistory = {
                    navController.navigate(
                        AppRoutes.CUSTOMER_PURCHASE_HISTORY
                    ) {
                        launchSingleTop = true
                    }
                },
                onNavigateToHome = {
                    navController.popBackStack(
                        route = AppRoutes.CUSTOMER,
                        inclusive = false
                    )
                },
                onNavigateToProfile = {
                    navigateToCustomerProfile()
                }
            )
        }

        composable(
            route = AppRoutes.CUSTOMER_PURCHASE_HISTORY,
            exitTransition = {
                if (targetState.destination.route == AppRoutes.CUSTOMER_ORDERS) {
                    ExitTransition.None
                } else {
                    null
                }
            },
            popEnterTransition = {
                if (initialState.destination.route == AppRoutes.CUSTOMER_ORDERS) {
                    EnterTransition.None
                } else {
                    null
                }
            }
        ) {
            val purchaseHistoryViewModel: PurchaseHistoryViewModel = viewModel()

            PurchaseHistoryRoute(
                viewModel = purchaseHistoryViewModel,
                isDarkTheme = isDarkTheme,
                onNavigateToOrders = {
                    val returnedToOrders = navController.popBackStack(
                        route = AppRoutes.CUSTOMER_ORDERS,
                        inclusive = false
                    )

                    if (!returnedToOrders) {
                        navigateToCustomerOrders()
                    }
                },
                onNavigateToHome = {
                    navController.popBackStack(
                        route = AppRoutes.CUSTOMER,
                        inclusive = false
                    )
                },
                onNavigateToProfile = {
                    navigateToCustomerProfile()
                }
            )
        }

        // Profile side
        composable(
            route = AppRoutes.CUSTOMER_PROFILE
        ) {
            val context = LocalContext.current

            val customerProfileViewModel: CustomerProfileViewModel = viewModel()

            CustomerProfileRoute(
                viewModel = customerProfileViewModel,
                isDarkTheme = isDarkTheme,
                onDarkThemeChanged = onDarkThemeChanged,
                onNavigateToOrders = {
                    navigateToCustomerOrders()
                },
                onNavigateToHome = {
                    navController.popBackStack(
                        route = AppRoutes.CUSTOMER,
                        inclusive = false
                    )
                },
                onNavigateToVouchers = {
                    navController.navigate(
                        AppRoutes.CUSTOMER_VOUCHERS
                    ) {
                        launchSingleTop = true
                    }
                },
                onLogoutRequested = {
                    logoutUser()
                }
            )
        }

        composable(
            route = AppRoutes.CUSTOMER_VOUCHERS
        ) {
            val voucherViewModel: VoucherViewModel = viewModel()

            VoucherRoute(
                viewModel = voucherViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToOrders = {
                    navigateToCustomerOrders()
                },
                onNavigateToHome = {
                    navController.popBackStack(
                        route = AppRoutes.CUSTOMER,
                        inclusive = false
                    )
                },
                onNavigateToProfile = {
                    val returnedToProfile = navController.popBackStack(
                        route = AppRoutes.CUSTOMER_PROFILE,
                        inclusive = false
                    )

                    if (!returnedToProfile) {
                        navigateToCustomerProfile()
                    }
                }
            )
        }
        /////////////////////////////////////////////////////////
        /////////////////// WORKER SCREEN ///////////////////////
        /////////////////////////////////////////////////////////

        // Order side
        composable(
            route = AppRoutes.WORKER,
            enterTransition = {
                EnterTransition.None
            },
            exitTransition = {
                ExitTransition.None
            },
            popEnterTransition = {
                EnterTransition.None
            },
            popExitTransition = {
                ExitTransition.None
            }
        ) {
            val workerPendingOrderViewModel:
                    WorkerPendingOrderViewModel = viewModel()

            WorkerPendingOrderRoute(
                viewModel = workerPendingOrderViewModel,
                onNavigateToCompletedOrders = {
                    navigateToWorkerDestination(
                        AppRoutes.WORKER_COMPLETED_ORDERS
                    )
                },
                onNavigateToItems = {
                    navigateToWorkerDestination(
                        AppRoutes.WORKER_ITEMS
                    )
                },
                onNavigateToActiveOrders = {
                    navigateToWorkerDestination(
                        AppRoutes.WORKER_ACTIVE_ORDERS
                    )
                },
                onNavigateToPickups = {
                    navigateToWorkerDestination(
                        AppRoutes.WORKER_PICKUPS
                    )
                },
                onLogoutRequested = {
                    logoutUser()
                }
            )
        }

        composable(
            route = AppRoutes.WORKER_ACTIVE_ORDERS,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) { backStackEntry ->
            val workerEntry = remember(backStackEntry) {
                navController.getBackStackEntry(
                    AppRoutes.WORKER
                )
            }

            val workerPendingOrderViewModel:
                    WorkerPendingOrderViewModel = viewModel(
                viewModelStoreOwner = workerEntry
            )

            val workerOrderViewModel: WorkerOrderViewModel = viewModel {
                WorkerOrderViewModel(
                    sharedState = workerPendingOrderViewModel.uiState
                )
            }

            WorkerOrderRoute(
                viewModel = workerOrderViewModel,
                onNavigateToPendingOrders = {
                    navigateToWorkerDestination(
                        AppRoutes.WORKER
                    )
                },
                onNavigateToPickups = {
                    navigateToWorkerDestination(
                        AppRoutes.WORKER_PICKUPS
                    )
                },
                onNavigateToCompletedOrders = {
                    navigateToWorkerDestination(
                        AppRoutes.WORKER_COMPLETED_ORDERS
                    )
                },
                onNavigateToItems = {
                    navigateToWorkerDestination(
                        AppRoutes.WORKER_ITEMS
                    )
                },
                onAcceptingOrdersChanged = { accepting ->
                    workerPendingOrderViewModel.onAction(
                        WorkerPendingOrderAction.AcceptingOrdersChanged(
                            accepting = accepting
                        )
                    )
                },
                onMarkReadyForPickup = { orderId ->
                    workerPendingOrderViewModel.markOrderReady(
                        orderId = orderId
                    )
                },
                onLogoutRequested = {
                    logoutUser()
                }
            )
        }

        composable(
            route = AppRoutes.WORKER_PICKUPS,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) { backStackEntry ->
            val workerEntry = remember(backStackEntry) {
                navController.getBackStackEntry(
                    AppRoutes.WORKER
                )
            }

            val workerPendingOrderViewModel:
                    WorkerPendingOrderViewModel = viewModel(
                viewModelStoreOwner = workerEntry
            )

            val workerPickupOrderViewModel:
                    WorkerPickupOrderViewModel = viewModel {
                WorkerPickupOrderViewModel(
                    sharedState = workerPendingOrderViewModel.uiState
                )
            }

            WorkerPickupOrderRoute(
                viewModel = workerPickupOrderViewModel,
                onNavigateToPendingOrders = {
                    navigateToWorkerDestination(
                        AppRoutes.WORKER
                    )
                },
                onNavigateToActiveOrders = {
                    navigateToWorkerDestination(
                        AppRoutes.WORKER_ACTIVE_ORDERS
                    )
                },
                onNavigateToCompletedOrders = {
                    navigateToWorkerDestination(
                        AppRoutes.WORKER_COMPLETED_ORDERS
                    )
                },
                onNavigateToItems = {
                    navigateToWorkerDestination(
                        AppRoutes.WORKER_ITEMS
                    )
                },
                onAcceptingOrdersChanged = { accepting ->
                    workerPendingOrderViewModel.onAction(
                        WorkerPendingOrderAction.AcceptingOrdersChanged(
                            accepting = accepting
                        )
                    )
                },
                onConfirmCollection = { orderId ->
                    workerPendingOrderViewModel.confirmOrderCollection(
                        orderId = orderId
                    )
                },
                onLogoutRequested = {
                    logoutUser()
                }
            )
        }

        // Completed order side
        composable(
            route = AppRoutes.WORKER_COMPLETED_ORDERS
        ) { backStackEntry ->
            val workerEntry = remember(backStackEntry) {
                navController.getBackStackEntry(
                    AppRoutes.WORKER
                )
            }

            val workerPendingOrderViewModel:
                    WorkerPendingOrderViewModel = viewModel(
                viewModelStoreOwner = workerEntry
            )

            val workerCompletedOrderViewModel:
                    WorkerCompletedOrderViewModel = viewModel {
                WorkerCompletedOrderViewModel(
                    sharedState = workerPendingOrderViewModel.uiState
                )
            }

            WorkerCompletedOrderRoute(
                viewModel = workerCompletedOrderViewModel,
                onNavigateToOrders = {
                    navigateToWorkerDestination(
                        AppRoutes.WORKER
                    )
                },
                onNavigateToItems = {
                    navigateToWorkerDestination(
                        AppRoutes.WORKER_ITEMS
                    )
                },
                onLogoutRequested = {
                    logoutUser()
                }
            )
        }

        // Items side
        composable(
            route = AppRoutes.WORKER_ITEMS
        ) {
            PlaceholderScreen(
                title = "Items",
                message = "The Worker Items screen will be added here.",
                onBack = {
                    navController.popBackStack(
                        route = AppRoutes.WORKER,
                        inclusive = false
                    )
                }
            )
        }

        /////////////////////////////////////////////////////////
        //////////////////// ADMIN SCREEN ///////////////////////
        /////////////////////////////////////////////////////////

        composable(AppRoutes.ADMIN) {
            val adminPanelViewModel: AdminPanelViewModel = viewModel()

            AdminPanelRoute(
                viewModel = adminPanelViewModel,

                onNavigateToCanteenMaintenance = {
                    navController.navigate(AppRoutes.CANTEEN_MAINTENANCE) {
                        launchSingleTop = true
                    }
                },

                onNavigateToStoreMaintenance = {
                    navController.navigate(AppRoutes.STORE_MAINTENANCE) {
                        launchSingleTop = true
                    }
                },

                onNavigateToMenuMaintenance = {
                    navController.navigate(AppRoutes.MENU_MAINTENANCE) {
                        launchSingleTop = true
                    }
                },

                onNavigateToOrderReporting = {
                    navController.navigate(AppRoutes.ORDER_REPORTING) {
                        launchSingleTop = true
                    }
                },

                onLogout = {
                    logoutUser()
                }
            )
        }

        // Canteen Maintenance side
        composable(AppRoutes.CANTEEN_MAINTENANCE) {
            val adminCanteenViewModel: AdminCanteenViewModel = viewModel()

            AdminCanteenRoute(
                viewModel = adminCanteenViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToCreateCanteen = {
                    navController.navigate(AppRoutes.CREATE_CANTEEN) {
                        launchSingleTop = true
                    }
                },
                onNavigateToEditCanteen = { canteenId ->
                    navController.navigate(
                        AppRoutes.editCanteen(canteenId)
                    ) {
                        launchSingleTop = true
                    }
                },
            )
        }

        composable(AppRoutes.CREATE_CANTEEN) {
            val context = LocalContext.current

            val adminAddCanteenViewModel: AdminAddCanteenViewModel = viewModel()

            AdminAddCanteenRoute(
                viewModel = adminAddCanteenViewModel,

                onNavigateBack = {
                    navController.popBackStack()
                },

                onCreateCanteen = { draft ->
                    // Front end test
                    Toast.makeText(
                        context,
                        "Form validated: ${draft.name}. Not saved yet.",
                        Toast.LENGTH_LONG
                    ).show()

                    navController.popBackStack()
                }
            )
        }

        composable(
            route = AppRoutes.EDIT_CANTEEN,
            arguments = listOf(
                navArgument("canteenId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val context = LocalContext.current

            val canteenId = requireNotNull(
                backStackEntry.arguments
            ).getInt("canteenId")

            // Reuse the ViewModel belonging to the maintenance destination.
            val maintenanceEntry = remember(backStackEntry) {
                navController.getBackStackEntry(
                    AppRoutes.CANTEEN_MAINTENANCE
                )
            }

            val maintenanceViewModel: AdminCanteenViewModel = viewModel(
                viewModelStoreOwner = maintenanceEntry
            )

            val editViewModel: AdminEditCanteenViewModel = viewModel {
                AdminEditCanteenViewModel(
                    canteen = maintenanceViewModel.uiState.value
                        .canteens
                        .firstOrNull {
                            it.id == canteenId
                        }
                )
            }

            AdminEditCanteenRoute(
                viewModel = editViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onUpdateCanteen = { draft ->
                    maintenanceViewModel.updateCanteen(
                        canteenId = draft.canteenId,
                        name = draft.name,
                        imageUri = draft.imageUri
                    )

                    Toast.makeText(
                        context,
                        "Canteen updated locally.",
                        Toast.LENGTH_SHORT
                    ).show()

                    navController.popBackStack()
                }
            )
        }

        // Store Maintenance side
        composable(AppRoutes.STORE_MAINTENANCE) {
            val adminStoreViewModel: AdminStoreViewModel = viewModel()

            AdminStoreRoute(
                viewModel = adminStoreViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToCreateStore = { canteenId ->
                    navController.navigate(
                        AppRoutes.createStore(canteenId)
                    ) {
                        launchSingleTop = true
                    }
                },
                onNavigateToEditStore = { storeId ->
                    navController.navigate(
                        AppRoutes.editStore(storeId)
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = AppRoutes.CREATE_STORE,
            arguments = listOf(
                navArgument("canteenId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val context = LocalContext.current

            val canteenId = requireNotNull(
                backStackEntry.arguments
            ).getInt("canteenId")

            val adminAddStoreViewModel: AdminAddStoreViewModel = viewModel {
                AdminAddStoreViewModel(
                    initialCanteenId = canteenId
                )
            }

            AdminAddStoreRoute(
                viewModel = adminAddStoreViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onCreateStore = { draft ->
                    // Frontend validation only.
                    // No store is saved or added to the list yet.
                    Toast.makeText(
                        context,
                        "Form validated: ${draft.name}. Not saved yet.",
                        Toast.LENGTH_LONG
                    ).show()

                    navController.popBackStack()
                }
            )
        }

        composable(
            route = AppRoutes.EDIT_STORE,
            arguments = listOf(
                navArgument("storeId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val context = LocalContext.current

            val storeId = requireNotNull(
                backStackEntry.arguments
            ).getInt("storeId")

            // Reuse the existing Store Maintenance ViewModel.
            val maintenanceEntry = remember(backStackEntry) {
                navController.getBackStackEntry(
                    AppRoutes.STORE_MAINTENANCE
                )
            }

            val maintenanceViewModel: AdminStoreViewModel = viewModel(
                viewModelStoreOwner = maintenanceEntry
            )

            val editViewModel: AdminEditStoreViewModel = viewModel {
                val state = maintenanceViewModel.uiState.value

                val store = state.stores.firstOrNull {
                    it.id == storeId
                }

                val canteen = state.canteens.firstOrNull {
                    it.id == store?.canteenId
                }

                AdminEditStoreViewModel(
                    store = store,
                    canteen = canteen
                )
            }

            AdminEditStoreRoute(
                viewModel = editViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onUpdateStore = { draft ->
                    maintenanceViewModel.updateStore(
                        storeId = draft.storeId,
                        name = draft.name,
                        imageUri = draft.imageUri
                    )

                    Toast.makeText(
                        context,
                        "Store updated locally.",
                        Toast.LENGTH_SHORT
                    ).show()

                    navController.popBackStack()
                }
            )
        }

        // Menu Maintenance side
        composable(AppRoutes.MENU_MAINTENANCE) {
            val adminMenuViewModel: AdminMenuViewModel = viewModel()

            AdminMenuRoute(
                viewModel = adminMenuViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToCreateMenuItem = { storeId ->
                    navController.navigate(
                        AppRoutes.createMenuItem(storeId)
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = AppRoutes.CREATE_MENU_ITEM,
            arguments = listOf(
                navArgument("storeId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val storeId = requireNotNull(
                backStackEntry.arguments
            ).getInt("storeId")

            val adminAddItemViewModel: AdminAddItemViewModel = viewModel {
                AdminAddItemViewModel(
                    initialStoreId = storeId
                )
            }

            AdminAddItemRoute(
                viewModel = adminAddItemViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onPublishItem = { draft ->
                    // Frontend validation only.
                    // Add repository/Supabase persistence here later.
                    Toast.makeText(
                        context,
                        "Form validated: ${draft.name}. Not published yet.",
                        Toast.LENGTH_LONG
                    ).show()

                    navController.popBackStack()
                }
            )
        }

        // Order Reporting side
        composable(AppRoutes.ORDER_REPORTING) {
            val adminReportViewModel: AdminReportViewModel = viewModel()

            AdminReportRoute(
                viewModel = adminReportViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
private fun customerMenuViewModel(
    menuEntry: NavBackStackEntry
): MenuViewModel {
    val arguments = requireNotNull(
        menuEntry.arguments
    )

    return viewModel(
        viewModelStoreOwner = menuEntry
    ) {
        MenuViewModel(
            storeId = arguments.getInt("storeId"),
            storeName = arguments.getString("storeName")
                ?: "Store",
            canteenName = arguments.getString("canteenName")
                .orEmpty()
        )
    }
}

@Composable
private fun customerCartViewModel(
    menuEntry: NavBackStackEntry
): CartViewModel {
    val arguments = requireNotNull(
        menuEntry.arguments
    )

    return viewModel(
        viewModelStoreOwner = menuEntry
    ) {
        CartViewModel(
            storeId = arguments.getInt("storeId"),
            storeName = arguments.getString("storeName")
                ?: "Store",
            canteenName = arguments.getString("canteenName")
                .orEmpty()
        )
    }
}

// Delete when the screens are implemented
@Composable
private fun PlaceholderScreen(
    title: String,
    message: String,
    onBack: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.CenterVertically
            )
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )

            Button(onClick = onBack) {
                Text("Back")
            }
        }
    }
}