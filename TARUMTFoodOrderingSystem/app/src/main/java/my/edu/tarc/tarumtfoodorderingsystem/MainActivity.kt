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
import my.edu.tarc.tarumtfoodorderingsystem.ui.theme.TARUMTFoodOrderingSystemTheme

class MainActivity : ComponentActivity() {

    // This is a placeholder because supabase not implemented yet
    private val authRepository: LoginAuthenticator by lazy {
        LoginAuthenticator { _, _ ->
            throw IllegalStateException(
                "Supabase login has not been implemented yet."
            )
        }
    }

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
                    isDarkTheme = isDarkTheme,
                    onDarkThemeChanged = { enabled ->
                        isDarkTheme = enabled
                    }
                )
            }
        }
    }
}