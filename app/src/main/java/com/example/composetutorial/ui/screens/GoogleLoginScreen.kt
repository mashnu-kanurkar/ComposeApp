package com.example.composetutorial.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Login
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composetutorial.R
import com.example.composetutorial.ui.theme.ComposeTutorialTheme
import androidx.compose.runtime.getValue
import com.example.composetutorial.authentication.LoginState
import com.example.composetutorial.authentication.LoginViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoogleLoginScreen(backAction: (() -> Unit)? = null,
                      onGoogleSignInClick: ()-> Unit,
                      onGuestLoginClick : ()-> Unit,
                      loginViewModel: LoginViewModel
){

    val snackbarHostState = remember { SnackbarHostState() }
    val currentUser by loginViewModel.currentUser.collectAsState(null)
    ComposeTutorialTheme() {
        Surface(modifier = Modifier) {
            Log.d("GoogleLogin", "Fireuser -> ${loginViewModel.currentUser}")
            Scaffold(modifier = Modifier,
                topBar = {
                    TopAppBar(
                        navigationIcon = {
                            if (backAction != null){
                                IconButton(
                                    modifier = Modifier,
                                    onClick = { backAction() },
                                ) {
                                    Icon(Icons.Filled.ArrowBack, "backIcon")
                                }
                            }
                        },
                        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                        title = {
                        Text(stringResource(id = R.string.login),
                            style = androidx.compose.material.MaterialTheme.typography.h6)
                    })
                },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            }) { contentPadding ->
                Box(modifier = Modifier.padding(contentPadding)) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Image(modifier = Modifier.padding(4.dp), painter = painterResource(id = R.drawable.logo), contentDescription = "logo")
                        Spacer(modifier = Modifier.size(2.dp, 4.dp))
                        GoogleSignIn(modifier = Modifier
                            .fillMaxWidth()
                            .padding(48.dp, 0.dp), onClick = onGoogleSignInClick )
                        Spacer(modifier = Modifier.size(2.dp, 4.dp))
                        GuestLoginButton(modifier = Modifier
                            .fillMaxWidth()
                            .padding(48.dp, 0.dp), onClick = onGuestLoginClick)
                    }
                    when(loginViewModel.loginState){
                        LoginState.STARTED -> CircularProgressIndicator()
                        LoginState.SUCCESS -> if (currentUser != null){
                            LaunchedEffect(Unit ){
                                snackbarHostState
                                    .showSnackbar("Hello ${currentUser?.displayName}", duration = SnackbarDuration.Short)
                            }
                        }
                        LoginState.FAILED -> LaunchedEffect(Unit ){
                            snackbarHostState
                                .showSnackbar("Login failed", duration = SnackbarDuration.Short)
                        }
                        else -> {}
                    }
                }

            }
        }
    }

}

@Composable
fun GuestLoginButton(modifier: Modifier = Modifier, onClick: () -> Unit){
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),) {
        Icon(imageVector = Icons.Filled.Login, contentDescription = null)
        Spacer(modifier = Modifier.size(ButtonDefaults.IconSize))
        Text(text = stringResource(id = R.string.guest_sign_in))
    }
}

@Composable
fun GoogleSignIn(modifier: Modifier = Modifier, onClick: () -> Unit){
    Button(modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),) {
        Image(painter = painterResource(id = R.drawable.ic_icons8_google),
            contentDescription = null,
            modifier = Modifier.size(ButtonDefaults.IconSize))
        Spacer(modifier = Modifier.size(ButtonDefaults.IconSize))
        Text(text = stringResource(id = R.string.google_sign_in))
    }
}


@Preview
@Composable
fun LoginScreenPreview( ){
    //GoogleLoginScreen( onGoogleSignInClick = { onGmailLoginClick() }, onGuestLoginClick = { onGuestLoginClick()}, snackbarController = SnackbarController())
}