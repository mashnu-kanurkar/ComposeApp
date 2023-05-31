package com.example.composetutorial.authentication

import android.content.IntentSender
import android.util.Log
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import java.lang.Exception

class GoogleOneTapProvider(private val loginLifecycleObserver: LoginLifecycleObserver,
                           private val oneTapClient: SignInClient,
                           private val webClientId: String,
                           private val oneTapLoginCredsListener: OneTapLoginCredsListener): OnSuccessListener<BeginSignInResult>, OnFailureListener {

    private val TAG = "GoogleAuthProvider"
    fun configSignInClient(webClientId: String,
                           isSignIn: Boolean = true): BeginSignInRequest {
        return BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(webClientId)
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(isSignIn)
                    .build())
            .build();
    }

    fun beginOneTapCredential(isSignIn:Boolean = true){
        oneTapClient.beginSignIn(configSignInClient(webClientId, isSignIn))
            .addOnSuccessListener(this)
    }

    override fun onSuccess(beginSignResult: BeginSignInResult?) {
        Log.d(TAG, "On sucess: ${beginSignResult}")
        try {
            loginLifecycleObserver.launchGoogleSignIn(result = beginSignResult!!, oneTapLoginCredsListener)
        }catch (e: IntentSender.SendIntentException) {
            Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
        }
    }

    override fun onFailure(exception: Exception) {
        Log.d(TAG,"No credentials from signIn: ${exception.localizedMessage}")
        beginOneTapCredential(false)
    }
}