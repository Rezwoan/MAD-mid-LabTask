package com.example.universityeventapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val featuredEvent = SampleData.events[0]
        findViewById<TextView>(R.id.tvFeaturedTitle).text = featuredEvent.title
        findViewById<TextView>(R.id.tvFeaturedDate).text = featuredEvent.date

        findViewById<Button>(R.id.btnBrowseEvents).setOnClickListener {
            startActivity(Intent(this, EventsActivity::class.java))
        }
        findViewById<Button>(R.id.btnRegisterNow).setOnClickListener {
            val intent = Intent(this, EventDetailActivity::class.java)
            intent.putExtra("event", featuredEvent)
            startActivity(intent)
        }
    }
}
