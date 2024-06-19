package com.example.musicplayerlite.media

import com.example.musicplayerlite.repository.IMediaRepository
import kotlinx.coroutines.CoroutineScope

class MusicPlayerController(
    private val musicRepository: IMediaRepository,
    private val musicPlayerManager: MusicPlayerManager,
    private val coroutineScope: CoroutineScope,
) {
    fun setShuffle() = musicPlayerManager.configMedia {  }
    fun setLooping() = musicPlayerManager.configMedia { isLooping = true }
    fun seekTo(duration: Int) =  musicPlayerManager.configMedia { seekTo(duration) }
    fun getDuration(): Int = musicPlayerManager.getMediaPlayer().duration
    fun getPosition(): Int = musicPlayerManager.getMediaPlayer().currentPosition
    fun hasPlaying(): Boolean = musicPlayerManager.getMediaPlayer().isPlaying
    fun pauseSong() {
        musicPlayerManager.configMedia { pause() }
/*        lifecycleScope.launch {
            if (isActive) {
                musicDataStore.updateCurrentSong(
                    indexSong = indexSong,
                    song = listSong[indexSong],
                    isPlaying = false,
                    duration = getDuration(),
                )
            }
        }*/
    }
    fun resumeSong() {
/*        mediaPlayer?.start()
        lifecycleScope.launch {
            if (isActive) {
                musicDataStore.updateCurrentSong(
                    indexSong = indexSong,
                    song = listSong[indexSong],
                    isPlaying = true,
                    duration = getDuration(),
                )
            }
        }*/
    }

/*    fun setSongIndex(index: Int) {
        if (index == indexSong) return
        indexSong = index
    }*/


/*    private fun playSong(song: Song) {
        val currentSong = listSong.getOrNull(indexSong) ?: return
        mediaPlayer?.reset()
        mediaPlayer?.setDataSource(applicationContext, song.mediaUri)
        mediaPlayer?.prepareAsync()
    }*/

/*    fun playPrevious() {
        indexSong--
        if (indexSong < 0) indexSong = listSong.lastIndex
        playSong()
    }*/
}