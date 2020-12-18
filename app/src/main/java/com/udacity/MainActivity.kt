package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_main.view.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private var fileName: String = ""
    private var status: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))



        custom_button.setOnClickListener {

            custom_button.setState(ButtonState.Loading)
            checkRadioButton()
        }
    }


    private fun download(url: String) {

        val request =
                DownloadManager.Request(Uri.parse(url))
                        .setTitle(getString(R.string.app_name))
                        .setDescription(getString(R.string.app_description))
                        .setRequiresCharging(false)
                        .setAllowedOverMetered(true)
                        .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
                downloadManager.enqueue(request)// enqueue puts the download request in the queue.

    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.e("Test", "Rceiver ")
            //Fetching the download id received with the broadcast
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            val action = intent?.action
            if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                if (downloadID == id) {
                    Log.e("Test", "ACTION_DOWNLOAD_COMPLETE " + id)
                    custom_button.setState(ButtonState.Completed)
                    status = "SUCCES"
                    createChannel(
                            getString(R.string.download_notification_channel_id),
                            getString(R.string.download_notification_channel_name)
                    )
                    val notificationManager = ContextCompat.getSystemService(applicationContext,
                            NotificationManager::class.java) as NotificationManager
                    notificationManager.sendNotification(getString(R.string.notification_description), applicationContext, fileName,status)

                }
            }


            //Checking if the received broadcast is for our enqueued download by matching download id

        }

    }

    private fun createChannel(channelId: String, channelName: String) {
        // TODO: Step 1.6 START create a channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                    channelId,
                    channelName, NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Your download is complete"
            val notificationManager =
                    applicationContext.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }

    }

    private fun checkRadioButton() {
        when (radioGroupChoice.checkedRadioButtonId) {
            -1 -> displayToast()
            R.id.radioButtonLoadApp -> download(URL).apply {
                fileName = radioGroupChoice.radioButtonLoadApp.text.toString()
                Log.e("Test", "Filename " + fileName)
            }
            R.id.radioButtonGlide -> download(URL_GLIDE).apply {
                fileName = radioGroupChoice.radioButtonLoadApp.text.toString()
            }
            R.id.radioButtonRetrofit -> download(URL_RETROFIT).apply {
                fileName = radioGroupChoice.radioButtonLoadApp.text.toString()
            }
        }
    }

    fun displayToast() {
        Toast.makeText(applicationContext, "Please select the file to Download", Toast.LENGTH_SHORT).show()
        custom_button.setState(ButtonState.Completed)
    }

    companion object {
        private const val URL =
                "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val URL_GLIDE =
                "https://github.com/bumptech/glide/archive/master.zip"
        private const val URL_RETROFIT =
                "https://github.com/square/retrofit/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }

}
