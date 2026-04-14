package com.example.photogalleryapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.OnBackPressedCallback
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    val allPhotos = mutableListOf<Photo>()
    val displayedPhotos = mutableListOf<Photo>()
    lateinit var adapter: PhotoAdapter
    var selectionMode = false
    var currentCategory = "All"

    lateinit var selectionToolbar: LinearLayout
    lateinit var tvSelectedCount: TextView
    lateinit var gridView: GridView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupPhotos()

        gridView = findViewById(R.id.gridView)
        selectionToolbar = findViewById(R.id.selectionToolbar)
        tvSelectedCount = findViewById(R.id.tvSelectedCount)

        adapter = PhotoAdapter(this, displayedPhotos, selectionMode)
        gridView.adapter = adapter

        setupCategoryButtons()
        setupGridEvents()
        setupToolbarButtons()
        setupFab()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (selectionMode) {
                    exitSelectionMode()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    fun setupPhotos() {
        allPhotos.addAll(listOf(
            Photo(1, R.drawable.photo_nature_1, "Forest Path", "Nature"),
            Photo(2, R.drawable.photo_nature_2, "Green Hills", "Nature"),
            Photo(3, R.drawable.photo_nature_3, "Sunlit Meadow", "Nature"),
            Photo(4, R.drawable.photo_city_1, "Downtown", "City"),
            Photo(5, R.drawable.photo_city_2, "Night Skyline", "City"),
            Photo(6, R.drawable.photo_city_3, "Bridge View", "City"),
            Photo(7, R.drawable.photo_animals_1, "Wild Fox", "Animals"),
            Photo(8, R.drawable.photo_animals_2, "Colorful Bird", "Animals"),
            Photo(9, R.drawable.photo_food_1, "Fresh Salad", "Food"),
            Photo(10, R.drawable.photo_food_2, "Berry Cake", "Food"),
            Photo(11, R.drawable.photo_travel_1, "Ocean Pier", "Travel"),
            Photo(12, R.drawable.photo_travel_2, "Mountain Trail", "Travel")
        ))
        displayedPhotos.addAll(allPhotos)
    }

    fun setupCategoryButtons() {
        val categories = listOf("All", "Nature", "City", "Animals", "Food", "Travel")
        val tabBar = findViewById<LinearLayout>(R.id.tabBar)
        for (category in categories) {
            val btn = Button(this)
            btn.text = category
            btn.setOnClickListener { filterByCategory(category) }
            tabBar.addView(btn)
        }
    }

    fun filterByCategory(category: String) {
        currentCategory = category
        exitSelectionMode()
        displayedPhotos.clear()
        if (category == "All") {
            displayedPhotos.addAll(allPhotos)
        } else {
            displayedPhotos.addAll(allPhotos.filter { it.category == category })
        }
        adapter.notifyDataSetChanged()
    }

    fun setupGridEvents() {
        gridView.setOnItemClickListener { _, _, position, _ ->
            if (selectionMode) {
                val photo = displayedPhotos[position]
                photo.isSelected = !photo.isSelected
                updateSelectionCount()
                adapter.notifyDataSetChanged()
            } else {
                val photo = displayedPhotos[position]
                val intent = Intent(this, FullscreenActivity::class.java)
                intent.putExtra("resourceId", photo.resourceId)
                startActivity(intent)
            }
        }
        gridView.setOnItemLongClickListener { _, _, position, _ ->
            if (!selectionMode) {
                selectionMode = true
                adapter.selectionMode = true
                selectionToolbar.visibility = View.VISIBLE
            }
            val photo = displayedPhotos[position]
            photo.isSelected = true
            updateSelectionCount()
            adapter.notifyDataSetChanged()
            true
        }
    }

    fun setupToolbarButtons() {
        val btnDelete = findViewById<Button>(R.id.btnDelete)
        val btnShare = findViewById<Button>(R.id.btnShare)
        btnDelete.setOnClickListener {
            val selected = displayedPhotos.filter { it.isSelected }
            val count = selected.size
            allPhotos.removeAll(selected.toSet())
            displayedPhotos.removeAll(selected.toSet())
            adapter.notifyDataSetChanged()
            Toast.makeText(this, "$count photos deleted", Toast.LENGTH_SHORT).show()
            exitSelectionMode()
        }
        btnShare.setOnClickListener {
            exitSelectionMode()
        }
    }

    fun setupFab() {
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        val randomDrawables = listOf(
            R.drawable.photo_nature_1, R.drawable.photo_city_1,
            R.drawable.photo_animals_1, R.drawable.photo_food_1, R.drawable.photo_travel_1
        )
        val randomCategories = listOf("Nature", "City", "Animals", "Food", "Travel")
        fab.setOnClickListener {
            val newId = (allPhotos.maxOfOrNull { it.id } ?: 0) + 1
            val index = (0..4).random()
            val newPhoto = Photo(newId, randomDrawables[index], "Photo $newId", randomCategories[index])
            allPhotos.add(newPhoto)
            if (currentCategory == "All" || currentCategory == newPhoto.category) {
                displayedPhotos.add(newPhoto)
                adapter.notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun updateSelectionCount() {
        val count = displayedPhotos.count { it.isSelected }
        tvSelectedCount.text = "$count selected"
    }

    fun exitSelectionMode() {
        selectionMode = false
        adapter.selectionMode = false
        selectionToolbar.visibility = View.GONE
        displayedPhotos.forEach { it.isSelected = false }
        adapter.notifyDataSetChanged()
    }
}
