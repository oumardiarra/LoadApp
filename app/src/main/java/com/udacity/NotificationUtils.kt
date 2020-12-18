/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat

// Notification ID.
private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

// TODO: Step 1.1 extension function to send messages (GIVEN)
/**
 * Builds and delivers the notification.
 *
 * @param context, activity context.
 */
fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context, fileName: String, status: String) {

    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
    contentIntent.putExtra("filename", fileName)
    contentIntent.putExtra("status", status)
    val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
    )


    val downloadImage = BitmapFactory.decodeResource(
            applicationContext.resources,
            R.drawable.ic_download
    )
    val bigPicStyle = NotificationCompat.BigPictureStyle()
            .bigPicture(downloadImage)
            .bigLargeIcon(null)

    /*val snoozeIntent = Intent(applicationContext, SnoozeReceiver::class.java)
    val snoozePendingIntent: PendingIntent = PendingIntent.getBroadcast(
        applicationContext,
        REQUEST_CODE,
        snoozeIntent,
        FLAGS
    )
*/
    val builder = NotificationCompat.Builder(
            applicationContext,
            applicationContext.getString(R.string.download_notification_channel_id)
    )

    builder.setSmallIcon(R.drawable.ic_download)
            .setContentTitle(applicationContext.getString(R.string.notification_title))
            .setContentText(messageBody)
            .setAutoCancel(true)
            // TODO: Step 2.1 add style to builder
            .setStyle(bigPicStyle)
            .setLargeIcon(downloadImage)
            .addAction(
                    R.drawable.ic_download,
                    applicationContext.getString(R.string.check_status),
                    pendingIntent
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
    // TODO: Step 1.4 call notify
    notify(NOTIFICATION_ID, builder.build())

}

fun NotificationManager.cancelAllNotifications() {
    cancelAll()
}

// TODO: Step 1.14 Cancel all notifications
