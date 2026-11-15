package my.edu.tarc.tarumtfoodorderingsystem.navigation

import android.content.pm.ApplicationInfo
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
}

// When implementing backend remember to delete authRepository, registrationService (they are placeholders for front end)

@Composable
fun AppNavHost(
    authRepository: LoginAuthenticator,
    registrationService: RegistrationService,
    isDarkTheme: Boolean,
    onDarkThemeChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoutes.LOGIN,
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
                    val destination = when (role) {
                        TestLoginRole.CUSTOMER -> AppRoutes.CUSTOMER
                        TestLoginRole.WORKER -> AppRoutes.WORKER
                        TestLoginRole.ADMIN -> AppRoutes.ADMIN
                    }
                    navController.navigate(destination) {
                        launchSingleTop = true
                    }
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
                        // Frontend test destination only.
                        // Real OTP authentication will later return the account role.
                        val destination = if (
                            email.trim().equals(
                                other = "worker@tarc.edu.my",
                                ignoreCase = true
                            )
                        ) {
                            AppRoutes.WORKER
                        } else {
                            AppRoutes.CUSTOMER
                        }

                        navController.navigate(destination) {
                            popUpTo(AppRoutes.LOGIN) {
                                inclusive = true
                            }

                            launchSingleTop = true
                        }
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
            PlaceholderScreen(
                title = "Admin",
                message = "Admin screen will be added here.",
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
                Text("Back to login")
            }
        }
    }
}