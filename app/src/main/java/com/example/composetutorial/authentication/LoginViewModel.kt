package com.example.composetutorial.authentication

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.*
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val loginLifecycleObserver: LoginLifecycleObserver): ViewModel(),
    OneTapLoginCredsListener, LoginStateListener {
    private val TAG = "LoginViewModel"
    private val firebaseAuthenticator = FirebaseAuthenticator(this)
    private val _currentUser = MutableStateFlow<FirebaseUser?>(firebaseAuthenticator.currentUser.value)
    var currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()
    var loginState by mutableStateOf<LoginState?>(null)
    private set
    init {

        viewModelScope.launch {
            firebaseAuthenticator.currentUser.collect{
                _currentUser.value = it
            }
        }
    }

    fun beginGoogleSignIn(oneTapClient: SignInClient,
                    webClientId: String,
                    isSignIn:Boolean = true){
        GoogleOneTapProvider(loginLifecycleObserver,oneTapClient, webClientId, this)
            .beginOneTapCredential(isSignIn)

    }
    fun beginAnonymousSignIn(){
        Log.d(TAG, "isAnonymous: ${currentUser.value?.isAnonymous}")
        if (currentUser.value?.isAnonymous == false){
            Log.d(TAG, "You are already logged in using Google")
            loginState = LoginState.SUCCESS
        }else{
            firebaseAuthenticator.signInAnonymously()
        }
    }

    override fun onCredsReceived(credential: SignInCredential, idToken: String) {
        firebaseAuthenticator.beginIdentifiedSignIn(idToken)
    }

    override fun onCredsFailed(exception: ApiException?) {
        Log.e(TAG, "Firebase login failed: ${exception}")
    }

    override fun onLoginInitiated() {
        loginState = LoginState.STARTED
    }

    override fun onLoginSuccessful() {
        loginState = LoginState.SUCCESS
    }

    override fun onLoginFailed() {
        loginState = LoginState.FAILED
    }

}

class LoginViewModelFactory(private val lifecycleObserver: LoginLifecycleObserver): ViewModelProvider.Factory{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(lifecycleObserver) as T
    }
}