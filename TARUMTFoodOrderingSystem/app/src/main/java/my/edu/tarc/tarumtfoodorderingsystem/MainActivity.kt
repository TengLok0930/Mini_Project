package my.edu.tarc.tarumtfoodorderingsystem

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kotlinx.io.IOException
import my.edu.tarc.tarumtfoodorderingsystem.data.local.UserPreferencesRepository
import my.edu.tarc.tarumtfoodorderingsystem.navigation.AppNavHost
import my.edu.tarc.tarumtfoodorderingsystem.presentation.auth.AuthValidation
import my.edu.tarc.tarumtfoodorderingsystem.presentation.auth.LoginAuthenticator
import my.edu.tarc.tarumtfoodorderingsystem.presentation.auth.TestLoginRole
import my.edu.tarc.tarumtfoodorderingsystem.presentation.auth.register.RegistrationService
import my.edu.tarc.tarumtfoodorderingsystem.ui.theme.TARUMTFoodOrderingSystemTheme

class MainActivity : ComponentActivity() {

    // Repositories
    private val userPreferencesRepository by lazy { UserPreferencesRepository(applicationContext) }

    // This is a placeholder because supabase not implemented yet (Need replace later)
    private val authRepository: LoginAuthenticator by lazy {
        LoginAuthenticator { userId, _ ->
            AuthValidation.roleForUserId(userId)
                ?: throw IllegalArgumentException("Invalid credentials")
        }
    }

    private val registrationService: RegistrationService by lazy {
        RegistrationService { _, _, _, _->
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

            val savedDarkTheme by userPreferencesRepository.isDarkTheme
                .collectAsStateWithLifecycle(
                    initialValue = null
                )

            val isDarkTheme = savedDarkTheme ?: systemDarkTheme

            TARUMTFoodOrderingSystemTheme(
                darkTheme = isDarkTheme
            ) {
                AppNavHost(
                    authRepository = authRepository,
                    registrationService = registrationService,
                    userPreferencesRepository = userPreferencesRepository,
                    isDarkTheme = isDarkTheme,
                    onDarkThemeChanged = { enabled ->
                        lifecycleScope.launch {
                            try {
                                userPreferencesRepository.saveDarkTheme(
                                    isDarkTheme = enabled
                                )
                            } catch (exception: IOException) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Unable to save appearance. Please try again.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                )
            }
        }
    }
}