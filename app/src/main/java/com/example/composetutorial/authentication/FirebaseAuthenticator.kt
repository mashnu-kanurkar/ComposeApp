package com.example.composetutorial.authentication

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.lang.Exception
import kotlin.math.log

class FirebaseAuthenticator(private val loginStateListener: LoginStateListener): OnCompleteListener<AuthResult>, OnFailureListener {
    private val TAG = "FirebaseAuthenticator"
    private var auth: FirebaseAuth = Firebase.auth
    private var _currentUser = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    var currentUser: StateFlow<FirebaseUser?> = _currentUser

    private var loginStateNumber = 0

    fun beginIdentifiedSignIn(idToken: String){
        loginStateListener.onLoginInitiated()
        if (currentUser.value?.isAnonymous == true){
            linkAccount(idToken)
        }else{
            signInWithGoogle(idToken)
        }
    }
    fun signInAnonymously(){
        loginStateListener.onLoginInitiated()
        loginStateNumber = 1
        auth.signInAnonymously()
            .addOnCompleteListener(this)
            .addOnFailureListener(this)
    }
    private fun signInWithGoogle(idToken: String){
        loginStateNumber = 2
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this)
            .addOnFailureListener(this)
    }

    private fun linkAccount(idToken: String){
        if (currentUser.value?.isAnonymous == true){
            loginStateNumber = 3
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.currentUser?.linkWithCredential(credential)?.addOnCompleteListener(this)?.addOnFailureListener(this)
        }
    }

    override fun onComplete(task: Task<AuthResult>) {
        if (task.isSuccessful) {
            // Sign in success, update UI with the signed-in user's information
            Log.d(TAG, "signInWithCredential:success")
            loginStateListener.onLoginSuccessful()
            if (loginStateNumber == 3){
                Log.d("TAG", "Account linking successful")
            }
            _currentUser.value = auth.currentUser

        } else {
            // If sign in fails, display a message to the user.
                loginStateListener.onLoginFailed()
            Log.w(TAG, "signInWithCredential:failure", task.exception)
            _currentUser.value = null
        }
    }

    override fun onFailure(exception: Exception) {
        Log.w(TAG, "signInWithCredential:failure", exception)
        loginStateListener.onLoginFailed()
       _currentUser.value = null
    }
}

interface LoginStateListener{
    fun onLoginInitiated()
    fun onLoginSuccessful()
    fun onLoginFailed()
}
enum class LoginState{
    STARTED,
    SUCCESS,
    FAILED
}