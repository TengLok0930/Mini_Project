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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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

object AppRoutes {

    // Auth
    const val AUTH_GATE = "auth_gate"
    const val LOGIN = "login"
    const val EMAIL_LOGIN = "email_login"
    const val OTP_LOGIN = "otp_login"
    const val REGISTER = "register"

    // Customer
    const val CUSTOMER = "customer"

    // Worker
    const val WORKER = "worker"

    // Admin
    const val ADMIN = "admin"
    const val CANTEEN_MAINTENANCE = "canteen_maintenance"
    const val CREATE_CANTEEN = "create_canteen"
    const val STORE_MAINTENANCE = "store_maintenance"
    const val MENU_MAINTENANCE = "menu_maintenance"
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
            PlaceholderScreen(
                title = "Customer",
                message = "Customer screen will be added here.",
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        /////////////////////////////////////////////////////////
        /////////////////// WORKER SCREEN ///////////////////////
        /////////////////////////////////////////////////////////

        composable(AppRoutes.WORKER) {
            PlaceholderScreen(
                title = "Worker",
                message = "Worker screen will be added here.",
                onBack = {
                    navController.popBackStack()
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
                }
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

        // Store Maintenance side
        composable(AppRoutes.STORE_MAINTENANCE) {
            PlaceholderScreen(
                title = "Store Maintenance",
                message = "Store maintenance screen will be added here.",
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // Menu Maintenance side
        composable(AppRoutes.MENU_MAINTENANCE) {
            PlaceholderScreen(
                title = "Menu Maintenance",
                message = "Menu maintenance screen will be added here.",
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // Order Reporting side
        composable(AppRoutes.ORDER_REPORTING) {
            PlaceholderScreen(
                title = "Order Reporting",
                message = "Order reporting screen will be added here.",
                onBack = {
                    navController.popBackStack()
                }
            )
        }
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