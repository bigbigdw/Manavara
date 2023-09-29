package com.bigbigdw.manavara.main

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.bigbigdw.manavara.main.screen.ScreenSplash
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

class ActivitySpalsh : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private var currentUser :  FirebaseUser? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            checkLogin()
        } else {
            checkLogin()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerNotification()
        askNotificationPermission()

        setContent {
            ScreenSplash()
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.

        auth = Firebase.auth
        currentUser = auth.currentUser
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

        auth = Firebase.auth
        currentUser = auth.currentUser

        checkUserExist(user = currentUser)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun askNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            checkLogin()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun checkUserExist(user: FirebaseUser?){
        val mRootRef = FirebaseDatabase.getInstance().reference.child("USER").child(user?.uid ?: "")

        mRootRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){
                    val intent = Intent(this@ActivitySpalsh, ActivityMain::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this@ActivitySpalsh, ActivityLogin::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}