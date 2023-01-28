package com.example.examplet.ui

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Application : android.app.Application() {
    override fun onCreate() {
        super.onCreate()

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task->
            if(!task.isSuccessful){
                return@addOnCompleteListener
            }
            val token = task.result
            Log.d("TAG","Token $token")
        }
    }
}