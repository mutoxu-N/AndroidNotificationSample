package com.github.mutoxu_n.notifiationsample

import android.Manifest
import android.app.Notification
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class SyncWorker(private val context: Context, params: WorkerParameters): Worker(context, params) {
    companion object {
        const val WORKER_TAG = "TAG_SYNC_WORKER"
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun doWork(): Result {
        // バックグラウンドでサーバーと通信し, 新たな通知があったら通知を作成する.
        // メッセージアプリなどの即時通知が必要な場合は, Firebase Cloud Messaging などをしようすると良いだろう

        // 権限確認
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return Result.failure()
        }

        // 通知作成
        val builder = Notification.Builder(context, MainActivity.NOTIFICATION_DEFAULT)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("定期通知")
            .setContentText("定期的に実行される通知")

        with(NotificationManagerCompat.from(context)) {
            notify(R.string.app_name, builder.build())
        }
        return Result.success()
    }
}