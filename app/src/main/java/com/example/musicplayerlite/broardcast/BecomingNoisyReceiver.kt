package com.example.musicplayerlite.broardcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager

class BecomingNoisyReceiver : BroadcastReceiver() {

    var onBecomingNoisy: (Boolean) -> Unit = {}

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == AudioManager.ACTION_AUDIO_BECOMING_NOISY) {
            onBecomingNoisy.invoke(true)
        }
    }
}
