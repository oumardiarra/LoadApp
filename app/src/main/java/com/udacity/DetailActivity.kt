package com.udacity

import android.app.NotificationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        val fileName = getIntent().getStringExtra("filename")
        val status = getIntent().getStringExtra("status")
        Log.e("Test", "Filename is ${fileName}")
        textViwFileName.text = fileName
        textViewStatus.text = status
        val notificationManager = ContextCompat.getSystemService(
            applicationContext,
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.cancelAllNotifications()
    }

}
