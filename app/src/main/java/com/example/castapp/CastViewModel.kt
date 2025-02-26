package com.example.castapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManager
import com.google.android.gms.cast.framework.SessionManagerListener
import com.example.castapp.CastSessionModel
import com.google.android.gms.cast.MediaInfo

class CastViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val VIDEO_URL = "https://videolink-test.mycdn.me/?pct=1&sig=6QNOvp0y3BE&ct=0&clientType=45&mid=193241622673&type=5"
    }

    private val _currentSession = MutableLiveData<CastSessionModel>()
    val currentSession: LiveData<CastSessionModel> get() = _currentSession

    private val mediaSessionListener = object : SessionManagerListener<CastSession> {
        override fun onSessionStarted(session: CastSession, sessionId: String) {
            _currentSession.value = CastSessionModel(session)
            startCasting(session)
        }

        override fun onSessionEnding(session: CastSession) {
            stopCasting(session)
        }

        override fun onSessionResumed(session: CastSession, wasSuspended: Boolean) {
            _currentSession.value = CastSessionModel(session)
            startCasting(session)
        }

        override fun onSessionStartFailed(session: CastSession, errorCode: Int) {
            stopCasting(session)
        }

        override fun onSessionEnded(p0: CastSession, p1: Int) {
            // do nothing
        }

        override fun onSessionResumeFailed(p0: CastSession, p1: Int) {
            // do nothing
        }

        override fun onSessionResuming(p0: CastSession, p1: String) {
            // do nothing
        }

        override fun onSessionStarting(p0: CastSession) {
            // do nothing
        }

        override fun onSessionSuspended(p0: CastSession, p1: Int) {
            // do nothing
        }

    }

    private val sessionManager = CastContext.getSharedInstance(application).sessionManager

    init {
        sessionManager.addSessionManagerListener(mediaSessionListener, CastSession::class.java)
    }

    fun startCasting(session: CastSession) {
        val mediaInfo = MediaInfo.Builder(VIDEO_URL)
            .setContentType("video/mp4")
            .build()

        session.remoteMediaClient?.load(mediaInfo, true, 0)
    }

    fun stopCasting(session: CastSession) {
        session.remoteMediaClient?.stop()
    }

    override fun onCleared() {
        super.onCleared()
        sessionManager.removeSessionManagerListener(mediaSessionListener, CastSession::class.java)
    }
}
