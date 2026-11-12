package my.edu.tarc.tarumtfoodorderingsystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import my.edu.tarc.tarumtfoodorderingsystem.navigation.AppNavHost
import my.edu.tarc.tarumtfoodorderingsystem.presentation.auth.LoginAuthenticator
import my.edu.tarc.tarumtfoodorderingsystem.presentation.auth.TestLoginRole
import my.edu.tarc.tarumtfoodorderingsystem.presentation.auth.register.RegistrationService
import my.edu.tarc.tarumtfoodorderingsystem.ui.theme.TARUMTFoodOrderingSystemTheme

class MainActivity : ComponentActivity() {

    // This is a placeholder because supabase not implemented yet (Need replace later)
    private val authRepository: LoginAuthenticator by lazy {
        LoginAuthenticator { userId, _ ->
            when (userId) {
                2222222 -> TestLoginRole.WORKER
                3333333 -> TestLoginRole.ADMIN
                else -> TestLoginRole.CUSTOMER
            }
        }
    }

    private val registrationService: RegistrationService by lazy {
        RegistrationService { _, _, _ ->
            throw IllegalStateException(
                "Supabase registration has not been implemented yet."
            )
        }
    }
    //////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val systemDarkTheme = isSystemInDarkTheme()

            var isDarkTheme by rememberSaveable {
                mutableStateOf(systemDarkTheme)
            }

            TARUMTFoodOrderingSystemTheme(
                darkTheme = isDarkTheme
            ) {
                AppNavHost(
                    authRepository = authRepository,
                    registrationService = registrationService,
                    isDarkTheme = isDarkTheme,
                    onDarkThemeChanged = { enabled ->
                        isDarkTheme = enabled
                    }
                )
            }
        }
    }
}