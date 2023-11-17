package com.bigbigdw.manavara.login

import android.app.Activity
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.lifecycle.lifecycleScope
import com.bigbigdw.manavara.R
import com.bigbigdw.manavara.util.screen.BackOnPressed
import com.bigbigdw.manavara.login.screen.ScreenLogin
import com.bigbigdw.manavara.login.viewModels.ViewModelLogin
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ActivityLogin : ComponentActivity() {
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    var storagePermissionLauncher: ActivityResultLauncher<IntentSenderRequest>? = null

    private val viewModelLogin: ViewModelLogin by viewModels()

    private lateinit var auth: FirebaseAuth

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModelLogin.sideEffects
            .onEach { Toast.makeText(this@ActivityLogin, it, Toast.LENGTH_SHORT).show() }
            .launchIn(lifecycleScope)

        auth = Firebase.auth

        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()

        setContent {
            val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass
            ScreenLogin(
                doLogin = { doLogin() },
                widthSizeClass = widthSizeClass,
                viewModelLogin = viewModelLogin,
                activity = this@ActivityLogin
            )
            BackOnPressed()
        }

        storagePermissionLauncher = registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModelLogin.loginResult(
                    activity = this@ActivityLogin,
                    oneTapClient = oneTapClient,
                    data = result.data
                )
            }
        }
    }

    private fun doLogin() {
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(this) { result ->
                try {
                    storagePermissionLauncher?.launch(
                        IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                    )
                } catch (e: IntentSender.SendIntentException) {
                    Log.e("ActivityLogin", "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener(this) { e ->
                Log.d("ActivityLogin", e.localizedMessage)
            }
    }
}