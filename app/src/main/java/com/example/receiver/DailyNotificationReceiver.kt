package com.example.receiver

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.MainActivity

class DailyNotificationReceiver : BroadcastReceiver() {

    companion object {
        private const val CHANNEL_ID = "daily_learn_channel"
        private const val NOTIFICATION_ID = 8801
    }

    override fun onReceive(context: Context, intent: Intent) {
        val todayEntry = com.example.data.model.DailyWordDatabase.getTodayWordEntry()
        val mainIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            mainIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Day ${todayEntry.dayNumber}/100: ${todayEntry.word} ${todayEntry.themeEmoji}")
            .setContentText("${todayEntry.urduMeaning} • ${todayEntry.dailySentence}")
            .setStyle(NotificationCompat.BigTextStyle().bigText("${todayEntry.word} (${todayEntry.urduMeaning}): ${todayEntry.definition}\n\nToday's Sentence: \"${todayEntry.dailySentence}\" (${todayEntry.sentenceUrduTranslation})"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        notificationManager?.notify(NOTIFICATION_ID, builder.build())
    }
}
