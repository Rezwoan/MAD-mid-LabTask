package com.example.fitnesstrackerdashboardapp

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val dailyGoal = 10000
    private var currentSteps = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvDate = findViewById<TextView>(R.id.tvDate)
        val tvStepsCount = findViewById<TextView>(R.id.tvStepsCount)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val tvProgressPercent = findViewById<TextView>(R.id.tvProgressPercent)
        val btnUpdateStats = findViewById<Button>(R.id.btnUpdateStats)

        // Display current date
        val sdf = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
        tvDate.text = sdf.format(Date())

        btnUpdateStats.setOnClickListener {
            val editText = EditText(this).apply {
                hint = "Enter step count"
                inputType = InputType.TYPE_CLASS_NUMBER
                setPadding(48, 24, 48, 24)
            }

            AlertDialog.Builder(this)
                .setTitle("Update Steps")
                .setMessage("Enter your current step count:")
                .setView(editText)
                .setPositiveButton("Update") { _, _ ->
                    val input = editText.text.toString().trim()
                    if (input.isNotEmpty()) {
                        currentSteps = input.toInt()
                        tvStepsCount.text = currentSteps.toString()

                        val progress = ((currentSteps.toFloat() / dailyGoal) * 100)
                            .toInt()
                            .coerceAtMost(100)

                        progressBar.progress = progress
                        tvProgressPercent.text = "$progress%"

                        if (progress >= 100) {
                            Toast.makeText(
                                this,
                                "Amazing! You've crushed your daily goal! Keep it up! 🎉",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}
