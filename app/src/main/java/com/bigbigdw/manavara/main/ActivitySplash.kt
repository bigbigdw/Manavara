package com.bigbigdw.manavara.main

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bigbigdw.manavara.R
import com.bigbigdw.manavara.login.ActivityLogin
import com.bigbigdw.manavara.login.screen.ScreenSplash
import com.bigbigdw.manavara.login.viewModels.ViewModelLogin
import com.bigbigdw.manavara.main.models.UserInfo
import com.bigbigdw.manavara.util.DataStoreManager
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class ActivitySplash : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private var currentUser :  FirebaseUser? = null

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    var storagePermissionLauncher: ActivityResultLauncher<IntentSenderRequest>? = null
    private val viewModelLogin: ViewModelLogin by viewModels()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            checkLogin()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModelLogin.sideEffects
            .onEach { Toast.makeText(this@ActivitySplash, it, Toast.LENGTH_SHORT).show() }
            .launchIn(lifecycleScope)

        registerNotification()
        askNotificationPermission()

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
            ScreenSplash()
        }

        storagePermissionLauncher = registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = Intent(this@ActivitySplash, ActivityMain::class.java)
                startActivity(intent)
                finish()
            } else {
                Log.d("ActivityLogin", "로그인 실패")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun registerNotification(){

        FirebaseMessaging.getInstance().subscribeToTopic("all")

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "공지사항"
        val channelName = "공지사항"
        val channelDescription = "공지사항"

        val notificationChannel: NotificationChannel?

        notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.description = channelDescription
        notificationManager.createNotificationChannel(notificationChannel)
    }

    private fun checkLogin(){

        val dataStore = DataStoreManager(this@ActivitySplash)

        val currentUser :  FirebaseUser?
        val auth: FirebaseAuth = Firebase.auth
        currentUser = auth.currentUser

        CoroutineScope(Dispatchers.IO).launch {
            checkUserExist(uid = currentUser?.uid ?: "")
            dataStore.setDataStoreString(DataStoreManager.UID, currentUser?.uid ?: "")

            val intent = Intent(this@ActivitySplash, ActivityLogin::class.java)
            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun askNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            checkLogin()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            checkLogin()
        }
    }

    private fun checkUserExist(uid: String){
        val mRootRef = FirebaseDatabase.getInstance().reference.child("USER").child(uid)

        mRootRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val dataStore = DataStoreManager(this@ActivitySplash)

                if(dataSnapshot.exists()){

                    CoroutineScope(Dispatchers.IO).launch {
                        dataStore.setDataStoreString(DataStoreManager.UID, uid)

                        val userInfo = dataSnapshot.child("USERINFO").getValue(UserInfo::class.java)

                        if(userInfo?.userStatus == "ALLOW"){
                            val intent = Intent(this@ActivitySplash, ActivityMain::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            val handler = Handler(Looper.getMainLooper())
                            handler.postDelayed({
                                Toast.makeText(this@ActivitySplash, "가입 승인 대기중입니다.", Toast.LENGTH_SHORT).show()
                            }, 0)
                        }
                    }

                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        dataStore.setDataStoreString(DataStoreManager.UID, uid)
                        val intent = Intent(this@ActivitySplash, ActivityLogin::class.java)
                        startActivity(intent)
                        finish()
                    }

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

}