package com.example.composetutorial.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.composetutorial.R
import com.example.composetutorial.ui.screens.GoogleLoginScreen
import com.example.composetutorial.authentication.LoginLifecycleObserver
import com.example.composetutorial.authentication.LoginViewModel
import com.example.composetutorial.authentication.LoginViewModelFactory
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient

class LoginActivity : ComponentActivity() {
    private lateinit var oneTapClient: SignInClient
    private val TAG = "LoginActivity"
    private lateinit var loginLifecycleObserver: LoginLifecycleObserver
    private lateinit var webClientId: String
    private lateinit var loginViewModel: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        oneTapClient = Identity.getSignInClient(this)
        loginLifecycleObserver = LoginLifecycleObserver(activityResultRegistry, oneTapSignInClient = oneTapClient)
        lifecycle.addObserver(loginLifecycleObserver)
        webClientId = getString(R.string.default_web_client_id)
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(loginLifecycleObserver)).get(
            LoginViewModel::class.java)

        setContent {
           GoogleLoginScreen(backAction = {backAction()},
               onGoogleSignInClick = { loginViewModel.beginGoogleSignIn(oneTapClient, webClientId, true) },
               onGuestLoginClick = { loginViewModel.beginAnonymousSignIn() },
               loginViewModel = loginViewModel)
        }
    }

    private fun backAction(){

    }
}






