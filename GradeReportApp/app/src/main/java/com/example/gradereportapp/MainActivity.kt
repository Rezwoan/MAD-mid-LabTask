package com.example.gradereportapp

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    data class SubjectEntry(val name: String, val obtained: Int, val total: Int)

    val subjects = mutableListOf<SubjectEntry>()

    lateinit var gradeTable: TableLayout
    lateinit var tvSummary: TextView
    lateinit var tvGpa: TextView
    lateinit var etSubjectName: EditText
    lateinit var etObtainedMarks: EditText
    lateinit var etTotalMarks: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        gradeTable = findViewById(R.id.gradeTable)
        tvSummary = findViewById(R.id.tvSummary)
        tvGpa = findViewById(R.id.tvGpa)
        etSubjectName = findViewById(R.id.etSubjectName)
        etObtainedMarks = findViewById(R.id.etObtainedMarks)
        etTotalMarks = findViewById(R.id.etTotalMarks)

        findViewById<Button>(R.id.btnAddSubject).setOnClickListener { handleAddSubject() }
        findViewById<Button>(R.id.btnShare).setOnClickListener { shareReport() }
    }

    fun getGrade(obtained: Int, total: Int): String {
        val pct = obtained.toDouble() / total * 100.0
        return when {
            pct >= 90 -> "A+"
            pct >= 80 -> "A"
            pct >= 70 -> "B+"
            pct >= 60 -> "B"
            pct >= 50 -> "C"
            pct >= 40 -> "D"
            else -> "F"
        }
    }

    fun getGradePoint(grade: String): Double = when (grade) {
        "A+" -> 4.0
        "A" -> 3.7
        "B+" -> 3.3
        "B" -> 3.0
        "C" -> 2.0
        "D" -> 1.0
        else -> 0.0
    }

    fun addSubjectRow(entry: SubjectEntry) {
        subjects.add(entry)
        val rowIndex = subjects.size - 1
        val grade = getGrade(entry.obtained, entry.total)
        val passed = grade != "F"

        val bgColor = when {
            passed && rowIndex % 2 == 0 -> "#E8F5E9".toColorInt()
            passed -> "#C8E6C9".toColorInt()
            rowIndex % 2 == 0 -> "#FFEBEE".toColorInt()
            else -> "#FFCDD2".toColorInt()
        }

        val row = TableRow(this)
        row.setBackgroundColor(bgColor)

        val gradeColor = if (passed) "#1B5E20".toColorInt() else "#B71C1C".toColorInt()
        val cellColor = "#212121".toColorInt()

        row.addView(makeCell(entry.name, Gravity.START or Gravity.CENTER_VERTICAL, cellColor, false))
        row.addView(makeCell("${entry.obtained}", Gravity.CENTER, cellColor, false))
        row.addView(makeCell("${entry.total}", Gravity.CENTER, cellColor, false))
        row.addView(makeCell(grade, Gravity.CENTER, gradeColor, true))

        gradeTable.addView(row, gradeTable.childCount - 1)
        updateSummaryAndGPA()
    }

    fun makeCell(text: String, gravity: Int, textColor: Int, bold: Boolean): TextView {
        return TextView(this).apply {
            this.text = text
            this.gravity = gravity
            this.textSize = 13f
            setTextColor(textColor)
            if (bold) setTypeface(null, Typeface.BOLD)
            setPadding(dpToPx(8), dpToPx(10), dpToPx(8), dpToPx(10))
            setSingleLine(false)
            layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
        }
    }

    fun updateSummaryAndGPA() {
        var passed = 0
        var gpaSum = 0.0
        subjects.forEach { entry ->
            val grade = getGrade(entry.obtained, entry.total)
            if (grade != "F") passed++
            gpaSum += getGradePoint(grade)
        }
        val total = subjects.size
        val failed = total - passed
        val gpa = if (total > 0) gpaSum / total else 0.0
        tvSummary.text = "Total: $total  |  Passed: $passed  |  Failed: $failed"
        tvGpa.text = String.format(Locale.US, "%.2f", gpa)
    }

    fun handleAddSubject() {
        val name = etSubjectName.text.toString().trim()
        val obtainedStr = etObtainedMarks.text.toString().trim()
        val totalStr = etTotalMarks.text.toString().trim()

        if (name.isEmpty() || obtainedStr.isEmpty() || totalStr.isEmpty()) {
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show()
            return
        }

        val obtained = obtainedStr.toIntOrNull()
        val total = totalStr.toIntOrNull()

        if (obtained == null || total == null) {
            Toast.makeText(this, "Marks must be valid numbers.", Toast.LENGTH_SHORT).show()
            return
        }
        if (total <= 0) {
            Toast.makeText(this, "Total marks must be greater than 0.", Toast.LENGTH_SHORT).show()
            return
        }
        if (obtained < 0) {
            Toast.makeText(this, "Obtained marks cannot be negative.", Toast.LENGTH_SHORT).show()
            return
        }
        if (obtained > total) {
            Toast.makeText(this, "Obtained marks cannot exceed total marks.", Toast.LENGTH_SHORT).show()
            return
        }

        addSubjectRow(SubjectEntry(name, obtained, total))
        etSubjectName.text.clear()
        etObtainedMarks.text.clear()
        etTotalMarks.text.clear()
        etSubjectName.requestFocus()
    }

    fun shareReport() {
        if (subjects.isEmpty()) {
            Toast.makeText(this, "No subjects to share.", Toast.LENGTH_SHORT).show()
            return
        }
        val sb = StringBuilder()
        sb.appendLine("================================")
        sb.appendLine("     STUDENT GRADE REPORT")
        sb.appendLine("================================")
        sb.appendLine("Name       : Rezwoan Ahmed")
        sb.appendLine("Student ID : 2023-CS-001")
        sb.appendLine("Semester   : Spring 2025")
        sb.appendLine("Department : CSE")
        sb.appendLine("--------------------------------")
        sb.appendLine(String.format(Locale.US, "%-22s %8s %6s %6s", "Subject", "Obtained", "Total", "Grade"))
        sb.appendLine("--------------------------------")
        var passed = 0
        var gpaSum = 0.0
        subjects.forEach { entry ->
            val grade = getGrade(entry.obtained, entry.total)
            if (grade != "F") passed++
            gpaSum += getGradePoint(grade)
            sb.appendLine(String.format(Locale.US, "%-22s %8d %6d %6s", entry.name, entry.obtained, entry.total, grade))
        }
        val total = subjects.size
        val gpa = if (total > 0) gpaSum / total else 0.0
        sb.appendLine("--------------------------------")
        sb.appendLine("Total: $total  |  Passed: $passed  |  Failed: ${total - passed}")
        sb.appendLine(String.format(Locale.US, "GPA  : %.2f / 4.00", gpa))
        sb.appendLine("================================")
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Grade Report - Rezwoan Ahmed")
            putExtra(Intent.EXTRA_TEXT, sb.toString())
        }
        startActivity(Intent.createChooser(intent, "Share Grade Report via"))
    }

    fun dpToPx(dp: Int): Int = (dp * resources.displayMetrics.density).toInt()
}
