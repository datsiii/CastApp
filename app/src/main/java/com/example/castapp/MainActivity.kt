package com.example.castapp

import androidx.mediarouter.app.MediaRouteButton
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManagerListener



class MainActivity : AppCompatActivity() {

    private val castViewModel: CastViewModel by viewModels()

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
        CastButtonFactory.setUpMediaRouteButton(applicationContext, mediaRouteButton)
        val textView = findViewById<TextView>(R.id.textView)


        castViewModel.currentSession.observe(this, Observer { sessionModel ->
            sessionModel.session?.let {
                textView.text = "Session is active"
            }?: run {
                textView.text = "No active session"
            }
        })
    }

}