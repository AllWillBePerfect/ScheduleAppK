package com.example.scheduleappk

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.enter.EnterFragment
import com.example.rxtest.sharednumber.AddFragment
import com.example.rxtest.sharednumber.FlowNumberFragment
import com.example.rxtest.sharednumber.NumberFragment
import com.example.scheduleappk.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)


//        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        launchEnterScreen()
//        launchRxTestScreen()
    }

    private fun launchEnterScreen() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container_main, EnterFragment())
            commit()
        }
    }

    private fun launchRxTestScreen() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container_main, FlowNumberFragment())
            commit()
        }
    }
}