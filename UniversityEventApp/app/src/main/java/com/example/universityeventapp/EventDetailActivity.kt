package com.example.universityeventapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.CollapsingToolbarLayout

class EventDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)

        @Suppress("DEPRECATION")
        val event = intent.getSerializableExtra("event") as Event

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val collapsingToolbar = findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbar)
        collapsingToolbar.title = event.title

        findViewById<ImageView>(R.id.ivEventHeader).setBackgroundResource(event.imageRes)
        findViewById<TextView>(R.id.tvDetailTitle).text = event.title
        findViewById<TextView>(R.id.tvDetailDate).text = "Date: ${event.date}"
        findViewById<TextView>(R.id.tvDetailTime).text = "Time: ${event.time}"
        findViewById<TextView>(R.id.tvDetailVenue).text = "Venue: ${event.venue}"
        findViewById<TextView>(R.id.tvDetailOrganizer).text = "Organizer: ${event.category} Department"
        findViewById<TextView>(R.id.tvDetailDescription).text = event.description

        val photos = listOf(
            android.R.color.holo_blue_light,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light,
            android.R.color.holo_purple
        )
        val recyclerPhotos = findViewById<RecyclerView>(R.id.recyclerPhotos)
        recyclerPhotos.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerPhotos.adapter = PhotoAdapter(photos)

        val speakers = listOf(
            Pair("Dr. Ahmed Khan", "Professor of Computer Science"),
            Pair("Ms. Fatima Malik", "Industry Expert, Google"),
            Pair("Mr. Tariq Islam", "Head of Research, BUET")
        )
        val recyclerSpeakers = findViewById<RecyclerView>(R.id.recyclerSpeakers)
        recyclerSpeakers.layoutManager = LinearLayoutManager(this)
        recyclerSpeakers.adapter = SpeakerAdapter(speakers)

        findViewById<Button>(R.id.btnRegister).setOnClickListener {
            val intent = Intent(this, SeatBookingActivity::class.java)
            intent.putExtra("event", event)
            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
