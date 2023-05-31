package com.example.composetutorial.authentication

import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.common.api.ApiException

interface OneTapLoginCredsListener {
    fun onCredsReceived(credential: SignInCredential, idToken: String)
    fun onCredsFailed(exception: ApiException?)
}