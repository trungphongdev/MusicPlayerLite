package com.example.musicplayerlite.common

import androidx.annotation.IntDef


interface PlaybackInfoListener {

    @IntDef(*[Action.LOOPING, Action.PLAYING, Action.PAUSED, Action.SHUFFLE, Action.NEXT, Action.PREVIOUS])
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Action {
        companion object {
            const val LOOPING = 0
            const val PLAYING = 1
            const val PAUSED = 2
            const val SHUFFLE = 3
            const val NEXT = 4
            const val PREVIOUS = 5
        }
    }

   fun onDurationChanged(duration: Int)

   fun onPositionChanged(position: Int)

   fun onStateChanged(@Action action: Int)

   fun onPlayBackCompleted()


}