package com.example.composetutorial.authentication

import android.app.Activity
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes

class LoginLifecycleObserver(private val registry: ActivityResultRegistry,
                             private val oneTapSignInClient: SignInClient, ): DefaultLifecycleObserver {
    private val TAG = "LoginLifecycleObserver"
    private lateinit var activityLauncher:  ActivityResultLauncher<IntentSenderRequest>
    private lateinit var oneTapClient: SignInClient
    private var showOneTapUI = true
    private lateinit var oneTapLoginCredsListener: OneTapLoginCredsListener

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        oneTapClient = oneTapSignInClient
        activityLauncher = registry.register("key", owner, ActivityResultContracts.StartIntentSenderForResult()){
            result ->
            if (result.resultCode == Activity.RESULT_OK){
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                    val idToken = credential.googleIdToken
                    when {
                        idToken != null -> {
                            // Got an ID token from Google. Use it to authenticate
                            // with your backend.
                            Log.d(TAG, "Got ID token.")
                            oneTapLoginCredsListener.onCredsReceived(credential, idToken)
                        }
                        else -> {
                            // Shouldn't happen.
                            Log.d(TAG, "No ID token or password!")
                            oneTapLoginCredsListener.onCredsFailed(null)
                        }
                    }
                }catch (exception: ApiException){
                    when(exception.statusCode){
                        CommonStatusCodes.CANCELED -> {
                            Log.d(TAG, "One-tap dialog was closed.")
                            // Don't re-prompt the user.
                            showOneTapUI = false
                        }
                        CommonStatusCodes.NETWORK_ERROR -> {
                            Log.d(TAG, "One-tap encountered a network error.")
                            // Try again or just ignore.
                        }
                        else -> {
                            Log.d(TAG, "Couldn't get credential from result." +
                                    " (${exception.localizedMessage})")
                            oneTapLoginCredsListener.onCredsFailed(exception)
                        }
                    }
                }
            }else if (result.resultCode == Activity.RESULT_CANCELED){

            }
        }

    }

    fun launchGoogleSignIn(result: BeginSignInResult, oneTapLoginCredsListener: OneTapLoginCredsListener){
        this.oneTapLoginCredsListener = oneTapLoginCredsListener
        activityLauncher.launch(IntentSenderRequest.Builder(result.pendingIntent.intentSender).build())
    }
}