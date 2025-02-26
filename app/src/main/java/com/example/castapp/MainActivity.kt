package com.example.castapp

import androidx.mediarouter.app.MediaRouteButton
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManagerListener



class MainActivity : AppCompatActivity() {
    private var currentSession: CastSession? = null

    private val mediaSessionListener = object : SessionManagerListener<CastSession> {

        override fun onSessionStarted(session: CastSession, sessionId: String) {
            currentSession = session

            checkAndStartCasting()
        }

        override fun onSessionEnding(session: CastSession) {
            stopCasting()
        }

        override fun onSessionResumed(session: CastSession, wasSuspended: Boolean) {
            currentSession = session
            checkAndStartCasting()
        }

        override fun onSessionStartFailed(session: CastSession, p1: Int) {
            stopCasting()
        }

        override fun onSessionEnded(p0: CastSession, p1: Int) {
            TODO("Not yet implemented")
        }


        override fun onSessionResumeFailed(session: CastSession, p1: Int) {
            TODO("Not yet implemented")
        }

        override fun onSessionSuspended(session: CastSession, p1: Int) {
            TODO("Not yet implemented")
        }

        override fun onSessionStarting(session: CastSession) {
            TODO("Not yet implemented")
        }

        override fun onSessionResuming(session: CastSession, sessionId: String) {
            TODO("Not yet implemented")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val mediaRouteButton = findViewById<MediaRouteButton>(R.id.mediaRouteButton)
        val castContext = CastContext.getSharedInstance(this)
        CastButtonFactory.setUpMediaRouteButton(applicationContext, mediaRouteButton)
    }

    override fun onStart() {
        super.onStart()

        val sessionManager = CastContext.getSharedInstance(this).sessionManager
        sessionManager.addSessionManagerListener(mediaSessionListener, CastSession::class.java)
        currentSession = sessionManager.currentCastSession
    }

    override fun onStop() {
        super.onStop()

        val sessionManager = CastContext.getSharedInstance(this).sessionManager
        sessionManager.removeSessionManagerListener(mediaSessionListener, CastSession::class.java)
    }

    private fun checkAndStartCasting() {
        currentSession?.let { session ->

            val mediaInfo =
                MediaInfo.Builder("https://videolink-test.mycdn.me/?pct=1&sig=6QNOvp0y3BE&ct=0&clientType=45&mid=193241622673&type=5")
                    .setContentType("video/mp4")
                    .build()

            val remoteMediaClient = session.remoteMediaClient
            remoteMediaClient?.load(mediaInfo, true, 0)
        }
    }

    private fun stopCasting() {
        currentSession?.let { session ->
            session.remoteMediaClient?.stop()
        }
    }

}