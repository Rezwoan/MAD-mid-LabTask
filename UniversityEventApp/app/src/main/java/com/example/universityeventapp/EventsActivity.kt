package com.example.universityeventapp

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip

class EventsActivity : AppCompatActivity() {
    private lateinit var adapter: EventAdapter
    private val allEvents = SampleData.events
    private var currentCategory = "All"
    private var currentQuery = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Events"

        adapter = EventAdapter(allEvents) { event ->
            val intent = Intent(this, EventDetailActivity::class.java)
            intent.putExtra("event", event)
            startActivity(intent)
        }
        val recycler = findViewById<RecyclerView>(R.id.recyclerEvents)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                currentQuery = newText ?: ""
                applyFilter()
                return true
            }
        })

        val chips = mapOf(
            R.id.chipAll to "All",
            R.id.chipTech to "Tech",
            R.id.chipSports to "Sports",
            R.id.chipCultural to "Cultural",
            R.id.chipAcademic to "Academic",
            R.id.chipSocial to "Social"
        )
        chips.forEach { (id, category) ->
            findViewById<Chip>(id).setOnClickListener {
                currentCategory = category
                applyFilter()
            }
        }
    }

    private fun applyFilter() {
        val filtered = allEvents.filter { event ->
            (currentCategory == "All" || event.category == currentCategory) &&
            (currentQuery.isEmpty() || event.title.contains(currentQuery, ignoreCase = true))
        }
        adapter.updateList(filtered)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
