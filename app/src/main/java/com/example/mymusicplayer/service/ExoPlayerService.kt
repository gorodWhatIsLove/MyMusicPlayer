package com.example.mymusicplayer.service

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.widget.ImageView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat.IMPORTANCE_HIGH
import androidx.media.MediaBrowserServiceCompat
import com.example.mymusicplayer.AppMedia
import com.example.mymusicplayer.di.ComponentMedia
import com.example.mymusicplayer.presentation.MainActivity
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import javax.inject.Inject


class ExoPlayerService : MediaBrowserServiceCompat() {

    private lateinit var component: ComponentMedia

    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?): BrowserRoot? {
        return BrowserRoot("", null)
    }

    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaBrowserCompat.MediaItem>>) {
        TODO("Not yet implemented")
    }

    private var mMediaSession: MediaSessionCompat? = null
    private lateinit var mStateBuilder: PlaybackStateCompat.Builder
    @Inject
    lateinit var mExoPlayer: ExoPlayer
    private var oldUri: Uri? = null

    private val playerNotificationManager: PlayerNotificationManager by lazy {
        PlayerNotificationManager
            .Builder(
                baseContext,
                NOTIFICATION_ID,
                CHANNEL_ID
            )
            .setNotificationListener(notificationListener)
            .setMediaDescriptionAdapter(descriptionAdapter)
            .setChannelImportance(IMPORTANCE_HIGH)
            .setSmallIconResourceId(R.drawable.notification_template_icon_bg)
            .setChannelDescriptionResourceId(com.example.mymusicplayer.R.string.app_name)
            .setNextActionIconResourceId(R.drawable.exo_icon_next)
            .setPreviousActionIconResourceId(R.drawable.exo_icon_previous)
            .setPauseActionIconResourceId(R.drawable.exo_icon_pause)
            .setPlayActionIconResourceId(R.drawable.exo_icon_play)
            .setChannelNameResourceId(com.example.mymusicplayer.R.string.app_name)
            .build()
    }

    private val mMediaSessionCallback = object : MediaSessionCompat.Callback() {
        override fun onPlayFromUri(uri: Uri?, extras: Bundle?) {
            super.onPlayFromUri(uri, extras)
//            uri?.let {
//                val mediaSource = extractMediaSourceFromUri(uri)
//                if (uri != oldUri)
//                    play(mediaSource)
//                else play() // this song was paused so we don't need to reload it
//                oldUri = uri
//            }
        }

        override fun onPause() {
            super.onPause()
            pause()
        }

        override fun onStop() {
            super.onStop()
            stop()
        }
    }

    private val descriptionAdapter: PlayerNotificationManager.MediaDescriptionAdapter = object :
        PlayerNotificationManager.MediaDescriptionAdapter {
        override fun getCurrentContentTitle(player: Player): CharSequence {
            return player.currentMediaItem?.mediaMetadata?.title ?: ""
        }

        override fun createCurrentContentIntent(player: Player): PendingIntent? {
            val openAppIntent: Intent = Intent(applicationContext, MainActivity.javaClass)
            return PendingIntent.getActivity(
                applicationContext, 0, openAppIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        }

        override fun getCurrentContentText(player: Player): CharSequence? {
            return null
        }

        override fun getCurrentLargeIcon(player: Player, callback: PlayerNotificationManager.BitmapCallback): Bitmap? {
            val view = ImageView(applicationContext)
            view.setImageURI(player.currentMediaItem?.mediaMetadata?.artworkUri)

            if(view.drawable == null) {
                return null
            }
            val bitmapDrawable: BitmapDrawable = view.drawable as BitmapDrawable
            return bitmapDrawable.bitmap
        }
    }

    private val notificationListener = object : PlayerNotificationManager.NotificationListener {
        override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
            super.onNotificationCancelled(notificationId, dismissedByUser)
            stopForeground(true)
            if(mExoPlayer.isPlaying) {
                mExoPlayer.pause()
            }
        }

        override fun onNotificationPosted(notificationId: Int, notification: Notification, ongoing: Boolean) {
            super.onNotificationPosted(notificationId, notification, ongoing)
            startForeground(notificationId, notification)
        }
    }

    override fun onCreate() {
        super.onCreate()
        component = (application as AppMedia).appComponent
        component.inject(this)
//        initializePlayer()
        initializeExtractor()
        initializeAttributes()
        playerNotificationManager.setPlayer(mExoPlayer)
        playerNotificationManager.setPriority(NotificationCompat.PRIORITY_MAX)
        playerNotificationManager.setUseRewindAction(false)
        playerNotificationManager.setUseFastForwardAction(false)
        mMediaSession = MediaSessionCompat(baseContext, "tag for debugging").apply {
            // Enable callbacks from MediaButtons and TransportControls
            setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
            )
            // Set initial PlaybackState with ACTION_PLAY, so media buttons can start the player
            mStateBuilder = PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY or PlaybackStateCompat.ACTION_PLAY_PAUSE)
            setPlaybackState(mStateBuilder.build())

            // methods that handle callbacks from a media controller
            setCallback(mMediaSessionCallback)

            // Set the session's token so that client activities can communicate with it
            setSessionToken(sessionToken)
            isActive = true
        }
    }

    private var mAttrs: AudioAttributes? = null

    private fun play(mediaSource: MediaSource) {
//        if (mExoPlayer == null) initializePlayer()
        mExoPlayer.apply {
            // AudioAttributes here from exoplayer package !!!
            mAttrs?.let { initializeAttributes() }
            // In 2.9.X you don't need to manually handle audio focus :D
            mAttrs?.let { setAudioAttributes(it, true) }
            setMediaSource(mediaSource)
            play()
        }
    }

    private fun play() {
        mExoPlayer.apply {
            mExoPlayer.playWhenReady = true
            updatePlaybackState(PlaybackStateCompat.STATE_PLAYING)
            mMediaSession?.isActive = true
        }
    }

    fun initializePlayer() {
        mExoPlayer = ExoPlayer.Builder(baseContext).build()
    }

    private fun pause() {
        mExoPlayer.apply {
            playWhenReady = false
            if (playbackState == PlaybackStateCompat.STATE_PLAYING) {
                updatePlaybackState(PlaybackStateCompat.STATE_PAUSED)
            }
        }
    }

    private fun stop() {
        // release the resources when the service is destroyed
        mExoPlayer.playWhenReady = false
        playerNotificationManager.setPlayer(null)
        mExoPlayer.release()
//        mExoPlayer = null
        updatePlaybackState(PlaybackStateCompat.STATE_STOPPED)
        mMediaSession?.isActive = false
        mMediaSession?.release()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        stop()
    }

    private fun updatePlaybackState(state: Int) {
        // You need to change the state because the action taken in the controller depends on the state !!!
        mMediaSession?.setPlaybackState(
            PlaybackStateCompat.Builder().setState(
                state // this state is handled in the media controller
                , 0L, 1.0f // Speed playing
            ).build()
        )
    }

    private fun initializeAttributes() {
        mAttrs = AudioAttributes.Builder().setUsage(C.USAGE_MEDIA)
            .setContentType(C.CONTENT_TYPE_MUSIC)
            .build()
    }

    private lateinit var mExtractorFactory: ProgressiveMediaSource.Factory

    private fun initializeExtractor() {
        val userAgent = Util.getUserAgent(baseContext, "Application Name")
        mExtractorFactory = ProgressiveMediaSource.Factory(DefaultDataSourceFactory(this, userAgent))
    }

    private fun extractMediaSourceFromUri(uri: Uri): MediaSource {

        return mExtractorFactory.createMediaSource(MediaItem.fromUri(uri))
    }

    companion object {
        const val NOTIFICATION_ID = 1243;
        const val CHANNEL_ID = "ExoPlayerChannel";
    }

}