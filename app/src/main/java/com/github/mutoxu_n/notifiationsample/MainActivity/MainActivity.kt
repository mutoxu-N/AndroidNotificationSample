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
import androidx.lifecycle.lifecycleScope
import com.github.mutoxu_n.notifiationsample.R
import com.github.mutoxu_n.notifiationsample.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val NOTIFICATION_PRIORITY_MIN = "NOTIFICATION_PRIORITY_MIN"
        private const val NOTIFICATION_PRIORITY_LOW = "NOTIFICATION_PRIORITY_LOW"
        private const val NOTIFICATION_PRIORITY_HIGH = "NOTIFICATION_PRIORITY_HIGH"
        private const val NOTIFICATION_PRIORITY_DEFAULT = "NOTIFICATION_PRIORITY_DEFAULT"
        private const val NOTIFICATION_PRIORITY_NONE = "NOTIFICATION_PRIORITY_NONE"
        private const val NOTIFICATION_DELAY_5s = "NOTIFICATION_DELAY_5s"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        binding = ActivityMainBinding.inflate(layoutInflater)

        // チャンネル作成
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(NotificationChannel(
                NOTIFICATION_PRIORITY_MIN,
                "優先度通知(MIN)",
                NotificationManager.IMPORTANCE_MIN
            ).apply { description = "優先度がMINの通知" }
        )

        notificationManager.createNotificationChannel(NotificationChannel(
                NOTIFICATION_PRIORITY_LOW,
                "優先度通知(LOW)",
                NotificationManager.IMPORTANCE_LOW
            ).apply { description = "優先度がLOWの通知" }
        )

        notificationManager.createNotificationChannel(NotificationChannel(
                NOTIFICATION_PRIORITY_HIGH,
                "優先度通知(HIGH)",
                NotificationManager.IMPORTANCE_HIGH
            ).apply { description = "優先度がHIGHの通知" }
        )

        notificationManager.createNotificationChannel(NotificationChannel(
                NOTIFICATION_PRIORITY_DEFAULT,
                "優先度通知(DEFAULT)",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply { description = "優先度がDEFAULTの通知" }
        )

        notificationManager.createNotificationChannel(NotificationChannel(
                NOTIFICATION_PRIORITY_NONE,
                "優先度通知(NONE)",
                NotificationManager.IMPORTANCE_NONE
            ).apply { description = "優先度がNONEの通知" }
        )

        notificationManager.createNotificationChannel(NotificationChannel(
                NOTIFICATION_DELAY_5s,
                "時間差通知(5s)",
                NotificationManager.IMPORTANCE_HIGH
            ).apply { description = "5秒後に発火する通知" }
        )

        // クリックリスナ
        binding.btMin.setOnClickListener { createSimpleNotification(
            "優先度MIN", "優先度がMINの通知",
            NOTIFICATION_PRIORITY_MIN) }

        binding.btLow.setOnClickListener { createSimpleNotification(
            "優先度LOW", "優先度がLOWの通知",
            NOTIFICATION_PRIORITY_LOW) }

        binding.btHigh.setOnClickListener { createSimpleNotification(
            "優先度HIGH", "優先度がHIGHの通知",
            NOTIFICATION_PRIORITY_HIGH) }

        binding.btDefault.setOnClickListener { createSimpleNotification(
            "優先度DEFAULT", "優先度がDEFAULTの通知",
            NOTIFICATION_PRIORITY_DEFAULT) }

        binding.btNone.setOnClickListener { createSimpleNotification(
            "優先度NONE", "優先度がNONEの通知",
            NOTIFICATION_PRIORITY_NONE) }

        binding.bt10s.setOnClickListener { lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                Thread.sleep(5_000)
                createSimpleNotification("時間差通知(5s)",
                    "5秒前に実行された通知", NOTIFICATION_DELAY_5s)
            }
        } }

        setContentView(binding.root)
    }

    private fun createSimpleNotification(title: String, content: String, channel: String) {
        // 権限確認
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "通知の権限がありません", Toast.LENGTH_SHORT).show()
            return
        }

        // 通知作成
        val builder = Notification.Builder(this, channel)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(content)

        with(NotificationManagerCompat.from(this)) {
            notify(R.string.app_name, builder.build())
        }
    }
}