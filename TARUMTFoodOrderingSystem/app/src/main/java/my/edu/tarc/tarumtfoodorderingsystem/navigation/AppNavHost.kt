package my.edu.tarc.tarumtfoodorderingsystem.navigation

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import my.edu.tarc.tarumtfoodorderingsystem.presentation.auth.LoginAuthenticator
import my.edu.tarc.tarumtfoodorderingsystem.presentation.auth.LoginRoute
import my.edu.tarc.tarumtfoodorderingsystem.presentation.auth.LoginViewModel

object AppRoutes {
    const val LOGIN = "login"
    const val OTP_LOGIN = "otp_login"
    const val REGISTER = "register"
    const val HOME = "home"
}

@Composable
fun AppNavHost(
    authRepository: LoginAuthenticator,
    isDarkTheme: Boolean,
    onDarkThemeChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoutes.LOGIN,
        modifier = modifier
    ) {
        composable(AppRoutes.LOGIN) {
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
                    navController.navigate(AppRoutes.OTP_LOGIN) {
                        launchSingleTop = true
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(AppRoutes.REGISTER) {
                        launchSingleTop = true
                    }
                },
                onLoginSuccess = {
                    navController.navigate(AppRoutes.HOME) {
                        popUpTo(AppRoutes.LOGIN) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(AppRoutes.OTP_LOGIN) {
            PlaceholderScreen(
                title = "Email code login",
                message = "OTPLoginScreen will be added here.",
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppRoutes.REGISTER) {
            PlaceholderScreen(
                title = "Register User",
                message = "The registration screen will be added here.",
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppRoutes.HOME) {
            PlaceholderScreen(
                title = "Home",
                message = "Login successful.",
                onBack = {
                    // Navigation only; this does not sign out of Supabase.
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(AppRoutes.HOME) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
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