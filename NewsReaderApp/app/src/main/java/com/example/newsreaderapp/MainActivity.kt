package com.example.newsreaderapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView

class MainActivity : AppCompatActivity() {

    var isBookmarked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nestedScrollView = findViewById<NestedScrollView>(R.id.nestedScrollView)
        val btnBookmark = findViewById<ImageButton>(R.id.btnBookmark)
        val btnShare = findViewById<ImageButton>(R.id.btnShare)
        val btnNavIntro = findViewById<Button>(R.id.btnNavIntro)
        val btnNavKeyPoints = findViewById<Button>(R.id.btnNavKeyPoints)
        val btnNavAnalysis = findViewById<Button>(R.id.btnNavAnalysis)
        val btnNavConclusion = findViewById<Button>(R.id.btnNavConclusion)
        val sectionIntroduction = findViewById<TextView>(R.id.sectionIntroduction)
        val sectionKeyPoints = findViewById<TextView>(R.id.sectionKeyPoints)
        val sectionAnalysis = findViewById<TextView>(R.id.sectionAnalysis)
        val sectionConclusion = findViewById<TextView>(R.id.sectionConclusion)
        val btnBackToTop = findViewById<Button>(R.id.btnBackToTop)

        val articleTitle = "The Rise of Artificial Intelligence: How Machines Are Reshaping Our World"

        btnNavIntro.setOnClickListener {
            nestedScrollView.smoothScrollTo(0, sectionIntroduction.top)
        }
        btnNavKeyPoints.setOnClickListener {
            nestedScrollView.smoothScrollTo(0, sectionKeyPoints.top)
        }
        btnNavAnalysis.setOnClickListener {
            nestedScrollView.smoothScrollTo(0, sectionAnalysis.top)
        }
        btnNavConclusion.setOnClickListener {
            nestedScrollView.smoothScrollTo(0, sectionConclusion.top)
        }

        btnBackToTop.setOnClickListener {
            nestedScrollView.smoothScrollTo(0, 0)
        }

        btnBookmark.setOnClickListener {
            isBookmarked = !isBookmarked
            if (isBookmarked) {
                btnBookmark.setImageResource(R.drawable.ic_bookmark)
                Toast.makeText(this, "Article Bookmarked", Toast.LENGTH_SHORT).show()
            } else {
                btnBookmark.setImageResource(R.drawable.ic_bookmark_border)
                Toast.makeText(this, "Bookmark Removed", Toast.LENGTH_SHORT).show()
            }
        }

        btnShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, articleTitle)
            startActivity(Intent.createChooser(intent, "Share Article"))
        }
    }
}
