package com.github.mutoxu_n.notifiationsample.MainActivity

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import com.github.mutoxu_n.notifiationsample.R
import com.github.mutoxu_n.notifiationsample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val NOTIFICATION_INSTANT = "NOTIFICATION_INSTANT"
        private const val NOTIFICATION_DELAY_10S = "NOTIFICATION_DELAY_10S"
        private const val NOTIFICATION_DELAY_1M = "NOTIFICATION_DELAY_1M"
        private const val NOTIFICATION_DELAY_5M = "NOTIFICATION_DELAY_5M"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        binding = ActivityMainBinding.inflate(layoutInflater)

        // チャンネル作成
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val instantChannel = NotificationChannel(
            NOTIFICATION_INSTANT,
            "インスタント通知",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply { description = "設定された後, 即時に発火する通知" }

        val delay10sChannel = NotificationChannel(
            NOTIFICATION_DELAY_10S,
            "時間差通知",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply { description = "10秒後に発火する通知" }

        val delay1mChannel = NotificationChannel(
            NOTIFICATION_DELAY_1M,
            "時間差通知",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply { description = "1分後に発火する通知" }

        val delay5mChannel = NotificationChannel(
            NOTIFICATION_DELAY_5M,
            "時間差通知",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply { description = "5分後に発火する通知" }

        notificationManager.createNotificationChannel(instantChannel)
        notificationManager.createNotificationChannel(delay10sChannel)
        notificationManager.createNotificationChannel(delay1mChannel)
        notificationManager.createNotificationChannel(delay5mChannel)

        // クリックリスナ
        binding.btNow.setOnClickListener { createSimpleNotification("タイトル", "通知の内容\n改行後の内容") }

        setContentView(binding.root)
    }

    private fun createSimpleNotification(title: String, content: String) {
        // 権限確認
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "通知の権限がありません", Toast.LENGTH_SHORT).show()
            return
        }

        // 通知作成
        val builder = Notification.Builder(this, NOTIFICATION_INSTANT)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(content)
        with(NotificationManagerCompat.from(this)) {
            notify(R.string.app_name, builder.build())
        }
    }
}