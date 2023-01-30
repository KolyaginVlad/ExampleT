package com.example.examplet.ui.services

import com.google.firebase.messaging.FirebaseMessagingService

class PushService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        
    }
}