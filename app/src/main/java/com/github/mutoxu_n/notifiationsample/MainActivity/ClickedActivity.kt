package com.github.mutoxu_n.notifiationsample.MainActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.mutoxu_n.notifiationsample.R
import com.github.mutoxu_n.notifiationsample.databinding.ActivityClickedBinding

class ClickedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClickedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClickedBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}