package com.example.tp7kts

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.concurrent.thread

class exercice2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_exercice2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val edt = findViewById<TextView>(R.id.stock)
        val btn = findViewById<Button>(R.id.btnCommencer)
        val prgrss = findViewById<ProgressBar>(R.id.progressBar2)
        val prd=findViewById<TextView>(R.id.produit3)
        btn.setOnClickListener {
            simulation(prgrss, edt,prd)
        }
    }

    private fun simulation(progressBar: ProgressBar, textView: TextView,prd: TextView) {
        Thread {
            runOnUiThread {
                progressBar.progress = 0
                textView.text = "simulation en cours"
            }
            var produit = 0
            val verrou = Object()
            val producteur = thread(start = true) {
                while (produit in 0..10) {
                    synchronized(verrou) {
                        if (produit < 10) {
                            produit++
                        } else {
                            println("Il n'y a pas de place pour ajouter")
                        }
                    }
                    Thread.sleep(500)
                }
            }
            val consommateur = thread(start = true) {
                while (produit in 0..10) {
                    synchronized(verrou) {
                        if (produit > 0) {
                            produit--
                        } else {
                            println("Aucun produit disponible")
                        }
                    }
                    Thread.sleep(1000)
                }
            }
            for (i in 1..20) {
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                    return@Thread
                }
                val pourcentage = i * 5
                val stock = synchronized(verrou) {
                    produit
                }
                runOnUiThread {
                    progressBar.progress = pourcentage
                    prd.text="les produit dans le stock: $stock"
                    textView.text = "Telechargement en cours : $pourcentage%"
                    if (pourcentage == 100) {
                        textView.text = "l'etat du stock $produit produit"
                        producteur.interrupt()
                        consommateur.interrupt()
                    }
                }
            }
        }.start()
    }
}
