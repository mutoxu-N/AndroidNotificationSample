package com.github.mutoxu_n.notifiationsample.MainActivity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Bundle
import android.os.SystemClock
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import androidx.lifecycle.lifecycleScope
import com.github.mutoxu_n.notifiationsample.R
import com.github.mutoxu_n.notifiationsample.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime

@SuppressLint("InlinedApi")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    companion object {
        const val KEY_TEXT_REPLY = "KEY_TEXT_REPLAY"
        private const val NOTIFICATION_DEFAULT = "NOTIFICATION_DEFAULT"
        private const val NOTIFICATION_PRIORITY_MIN = "NOTIFICATION_PRIORITY_MIN"
        private const val NOTIFICATION_PRIORITY_LOW = "NOTIFICATION_PRIORITY_LOW"
        private const val NOTIFICATION_PRIORITY_HIGH = "NOTIFICATION_PRIORITY_HIGH"
        private const val NOTIFICATION_PRIORITY_DEFAULT = "NOTIFICATION_PRIORITY_DEFAULT"
        private const val NOTIFICATION_PRIORITY_NONE = "NOTIFICATION_PRIORITY_NONE"
        private const val NOTIFICATION_DELAY_SCOPE = "NOTIFICATION_DELAY_SCOPE"
        private const val NOTIFICATION_VIBE = "NOTIFICATION_VIBE"
        private const val NOTIFICATION_SOUND = "NOTIFICATION_SOUND"
        private const val NOTIFICATION_PROGRESS = "NOTIFICATION_PROGRESS"
        private const val SAMPLE_GROUP = "SAMPLE_GROUP"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // チャンネル削除
        notificationManager.deleteNotificationChannel(NOTIFICATION_DEFAULT)
        notificationManager.deleteNotificationChannel(NOTIFICATION_PRIORITY_MIN)
        notificationManager.deleteNotificationChannel(NOTIFICATION_PRIORITY_LOW)
        notificationManager.deleteNotificationChannel(NOTIFICATION_PRIORITY_HIGH)
        notificationManager.deleteNotificationChannel(NOTIFICATION_PRIORITY_DEFAULT)
        notificationManager.deleteNotificationChannel(NOTIFICATION_PRIORITY_NONE)
        notificationManager.deleteNotificationChannel(NOTIFICATION_DELAY_SCOPE)
        notificationManager.deleteNotificationChannel(NOTIFICATION_VIBE)
        notificationManager.deleteNotificationChannel(NOTIFICATION_SOUND)
        notificationManager.deleteNotificationChannel(NOTIFICATION_PROGRESS)

        // チャンネル作成
        notificationManager.createNotificationChannel(
            NotificationChannel(
                NOTIFICATION_DEFAULT,
                "デフォルト通知",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description="Builder設定時に使用するチャンネル"
                setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), audioAttributes)
                vibrationPattern = longArrayOf(0, 500)
            }
        )

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
            NOTIFICATION_PRIORITY_DEFAULT,
            "優先度通知(DEFAULT)",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply { description = "優先度がDEFAULTの通知" }
        )

        notificationManager.createNotificationChannel(NotificationChannel(
                NOTIFICATION_PRIORITY_HIGH,
                "優先度通知(HIGH)",
                NotificationManager.IMPORTANCE_HIGH
            ).apply { description = "優先度がHIGHの通知" }
        )

        notificationManager.createNotificationChannel(NotificationChannel(
                NOTIFICATION_PRIORITY_NONE,
                "優先度通知(NONE)",
                NotificationManager.IMPORTANCE_NONE
            ).apply { description = "優先度がNONEの通知" }
        )

        notificationManager.createNotificationChannel(NotificationChannel(
                NOTIFICATION_VIBE,
                "カスタムバイブレーション",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "バイブレーションを設定した通知"
                vibrationPattern = longArrayOf(
                    0,   200,
                    200, 200,
                    200, 200,

                    600, 200,
                    200, 200,
                    200, 200,

                    600, 200,
                    200, 200,
                    200, 200,
                    200, 200,
                    200, 200,
                    200, 200,
                    200, 200)
            }
        )

        notificationManager.createNotificationChannel(NotificationChannel(
                NOTIFICATION_SOUND,
                "通知音",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "音が鳴る通知"
                setSound(
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                    audioAttributes
                )
            }
        )

        notificationManager.createNotificationChannel(
            NotificationChannel(
                NOTIFICATION_PROGRESS,
                "進捗表示通知",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description="進捗状況を表示するような通知"
                setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), audioAttributes)
                vibrationPattern = longArrayOf(0, 500)
            }
        )


        // クリックリスナ
        binding.btMin.setOnClickListener { createSimpleNotification(
            "優先度MIN", "優先度がMINの通知",
            NOTIFICATION_PRIORITY_MIN) }

        binding.btLow.setOnClickListener { createSimpleNotification(
            "優先度LOW", "優先度がLOWの通知",
            NOTIFICATION_PRIORITY_LOW) }

        binding.btDefault.setOnClickListener { createSimpleNotification(
            "優先度DEFAULT", "優先度がDEFAULTの通知",
            NOTIFICATION_PRIORITY_DEFAULT) }

        binding.btHigh.setOnClickListener { createSimpleNotification(
            "優先度HIGH", "優先度がHIGHの通知",
            NOTIFICATION_PRIORITY_HIGH) }

        binding.btNone.setOnClickListener { createSimpleNotification(
            "優先度NONE", "優先度がNONEの通知",
            NOTIFICATION_PRIORITY_NONE) }

        binding.btGroup.setOnClickListener {
            val notification = NotificationCompat.Builder(this@MainActivity, NOTIFICATION_DEFAULT)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("通知@${ZonedDateTime.now().toLocalTime()}")
                .setContentText("通知の内容\n${ZonedDateTime.now()}")
                .setGroup(SAMPLE_GROUP)
                .build()
            createNotification(notification, SystemClock.uptimeMillis().toInt())
        }

        binding.btGroupSummary.setOnClickListener {
            val summary = NotificationCompat.Builder(this@MainActivity, NOTIFICATION_DEFAULT)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("サマリー")
                .setContentText("通知のまとめ")
                .setGroup(SAMPLE_GROUP)
                .setGroupSummary(true)
                .build()
            createNotification(summary, 999)
        }

        binding.btScope.setOnClickListener { lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                Thread.sleep(5_000)
                createSimpleNotification("時間差通知(5s)",
                    "非同期処理で5秒後に発行した通知", NOTIFICATION_DELAY_SCOPE)
            }
        } }

        binding.btVibe.setOnClickListener { createSimpleNotification(
            "カスタムバイブレーション", "振動をカスタムした通知",
            NOTIFICATION_VIBE) }

        binding.btSound.setOnClickListener { createSimpleNotification(
            "デフォルト通知音", "通知音が鳴る通知",
            NOTIFICATION_SOUND
        ) }

        binding.btAction.setOnClickListener {
            val pendIntent = PendingIntent.getActivities(
                this@MainActivity,
                0,
                arrayOf(Intent(this@MainActivity, ClickedActivity::class.java)),
                PendingIntent.FLAG_IMMUTABLE
            )
            val action = NotificationCompat.Action.Builder(
                R.drawable.ic_launcher_foreground,
                "開く", pendIntent
            ).build()

            createNotificationWithAction(
                "1ボタン通知",
                "アクションボタン付きの通知",
                NOTIFICATION_DEFAULT,
                action
            )
        }

        binding.btInput.setOnClickListener {
            val remoteInput = RemoteInput.Builder(KEY_TEXT_REPLY).run {
                setLabel("メッセージを入力してください")
                build()
            }
            val pendIntent = PendingIntent.getActivities(
                this@MainActivity,
                1,
                arrayOf(Intent(this@MainActivity, ClickedActivity::class.java)),
                PendingIntent.FLAG_MUTABLE
            )
            val action = NotificationCompat.Action.Builder(
                R.drawable.ic_launcher_foreground,
                "返信する", pendIntent
            ).addRemoteInput(remoteInput).build()

            createNotificationWithAction(
                "入力可能通知",
                "ボタンを押下すると文字列を入力できる",
                NOTIFICATION_DEFAULT,
                action
            )
        }

        binding.btProgress.setOnClickListener {
            // 通知作成
            val title = "進捗通知"
            val content = "進捗バーが進んでいく"
            val PROGRESS_MAX = 100
            var progressCurrent = 0

            val builder = NotificationCompat.Builder(this, NOTIFICATION_PROGRESS)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(content)
                .setOnlyAlertOnce(true)

            lifecycleScope.launch { withContext(Dispatchers.IO){
                // 0%
                builder.setProgress(PROGRESS_MAX, progressCurrent, false)
                createNotification(builder.build())

                while(progressCurrent < PROGRESS_MAX) {
                    Thread.sleep(50)
                    progressCurrent++
                    builder
                        .setProgress(PROGRESS_MAX, progressCurrent, false)
                        .setContentText("$content ($progressCurrent%)")
                    createNotification(builder.build())
                }

                // 100%
                Thread.sleep(50)
                builder
                    .setProgress(PROGRESS_MAX, progressCurrent, false)
                    .setContentText("ダウンロード完了")
                    .setOnlyAlertOnce(false)
                createNotification(builder.build())
            } }
        }

        binding.btPush.setOnClickListener {
            // 通知作成
            val pendIntent = PendingIntent.getActivities(
                this@MainActivity,
                2,
                arrayOf(Intent(this@MainActivity, ClickedActivity::class.java)),
                PendingIntent.FLAG_IMMUTABLE
            )

            val notification = NotificationCompat.Builder(this, NOTIFICATION_DEFAULT)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("押下可能な通知")
                .setContentText("通知を押すとIntentが起動する")
                .setContentIntent(pendIntent)
                .build()

            createNotification(notification)
        }

        binding.btImage.setOnClickListener {
            val notification = NotificationCompat.Builder(this@MainActivity, NOTIFICATION_DEFAULT)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("画像の通知")
                .setContentText("画像を添付した通知")
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.small))
                .build()
            createNotification(notification)
        }

        binding.btBigImage.setOnClickListener {
            val notification = NotificationCompat.Builder(this@MainActivity, NOTIFICATION_DEFAULT)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("画像の通知")
                .setContentText("大きな画像を添付した通知")
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.large))
                .setStyle(
                    NotificationCompat.BigPictureStyle()
                        .bigPicture(BitmapFactory.decodeResource(resources, R.drawable.large))
                        .bigLargeIcon(null as Bitmap?)
                )
                .build()
            createNotification(notification)
        }

        binding.btInbox.setOnClickListener {
            val notification = NotificationCompat.Builder(this@MainActivity, NOTIFICATION_DEFAULT)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("INBOX通知")
                .setSubText("サブテキスト")
                .setContentText("受信トレイスタイルの通知")
                .setStyle(
                    NotificationCompat.InboxStyle()
                        .addLine("LINE1").addLine("LINE2").addLine("LINE3").addLine("LINE4")
                        .addLine("LINE5").addLine("LINE6").addLine("LINE7").addLine("LINE8")
                )
                .build()

            createNotification(notification)
        }

        binding.btLong.setOnClickListener {
            val notification = NotificationCompat.Builder(this@MainActivity, NOTIFICATION_DEFAULT)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("長文通知")
                .setContentText("普通より長い文を通知として表示できる")
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(Data.SAMPLE_LONG_TEXT)
                )
                .build()
            createNotification(notification)
        }

        setContentView(binding.root)
    }

    @SuppressLint("InlinedApi")
    private fun createSimpleNotification(title: String, content: String, channel: String) {
        // 権限確認
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "通知の権限がありません", Toast.LENGTH_SHORT).show()
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 0)
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

    @SuppressLint("InlinedApi")
    private fun createNotificationWithAction(title: String, content: String, channel: String, action: NotificationCompat.Action) {
        // 権限確認
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "通知の権限がありません", Toast.LENGTH_SHORT).show()
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 0)
            return
        }

        // 通知作成
        val builder = NotificationCompat.Builder(this, channel)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(content)
            .addAction(action)

        with(NotificationManagerCompat.from(this)) {
            notify(R.string.app_name, builder.build())
        }
    }


    @SuppressLint("InlinedApi")
    private fun createNotification(notification: Notification, id: Int = R.string.app_name) {
        // 権限確認
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "通知の権限がありません", Toast.LENGTH_SHORT).show()
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 0)
            return
        }

        with(NotificationManagerCompat.from(this)) {
            notify(id, notification)
        }
    }


}