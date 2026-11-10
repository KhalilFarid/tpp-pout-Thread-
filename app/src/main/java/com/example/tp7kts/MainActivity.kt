package com.example.tp7kts

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val edt = findViewById<TextView>(R.id.sauvagarder)
        val btn = findViewById<Button>(R.id.btn)
        val prgrss = findViewById<ProgressBar>(R.id.progressBar)
        btn.setOnClickListener {
            sauvagarder(prgrss, edt)
        }
    }
    private fun sauvagarder(progressBar: ProgressBar, textView: TextView) {
        Thread {
            runOnUiThread {
                progressBar.progress = 0
                textView.text = "Téléchargement en cours : 0%"
            }
            for (i in 1..10) {
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                    return@Thread
                }
                val pourcentage = i * 10
                runOnUiThread {
                    progressBar.progress = pourcentage
                    textView.text = "Telechargement en cours : $pourcentage%"
                    if (pourcentage == 100) {
                        textView.text = "Telechargement terminé !"
                    }
                }
            }
        }.start()
    }
}