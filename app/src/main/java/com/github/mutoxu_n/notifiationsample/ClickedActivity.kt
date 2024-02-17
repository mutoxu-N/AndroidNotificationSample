package com.github.mutoxu_n.notifiationsample

import android.app.NotificationManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.RemoteInput
import com.github.mutoxu_n.notifiationsample.databinding.ActivityClickedBinding

class ClickedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClickedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClickedBinding.inflate(layoutInflater)

        RemoteInput.getResultsFromIntent(intent)?.let {
            binding.msg.text = it.getCharSequence(MainActivity.KEY_TEXT_REPLY)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(R.string.app_name)
        }

        setContentView(binding.root)
    }
}