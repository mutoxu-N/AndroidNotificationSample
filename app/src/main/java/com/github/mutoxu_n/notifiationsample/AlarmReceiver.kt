package com.github.mutoxu_n.notifiationsample

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmReceiver: BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onReceive(context: Context, intent: Intent) {
        // 権限確認
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "通知の権限がありません", Toast.LENGTH_SHORT).show()
            return
        }

        val notification = NotificationCompat.Builder(context,
            MainActivity.NOTIFICATION_DELAY
        )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("遅延通知")
            .setContentText("5秒前に設定された通知")
            .build()

        with(NotificationManagerCompat.from(context)) {
            notify(R.string.app_name, notification)
        }
    }
}