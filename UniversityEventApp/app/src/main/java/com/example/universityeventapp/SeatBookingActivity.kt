package com.example.universityeventapp

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlin.random.Random

class SeatBookingActivity : AppCompatActivity() {
    companion object {
        const val ROWS = 8
        const val COLS = 6
        const val TOTAL = ROWS * COLS
    }

    val seatStates = IntArray(TOTAL)
    val seatViews = arrayOfNulls<TextView>(TOTAL)
    var selectedCount = 0
    var pricePerSeat = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seat_booking)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Book Seats"

        @Suppress("DEPRECATION")
        val event = intent.getSerializableExtra("event") as? Event
        pricePerSeat = event?.price ?: 0.0

        val random = Random(42)
        for (i in 0 until TOTAL) {
            seatStates[i] = if (random.nextFloat() < 0.3f) 1 else 0
        }

        val seatSize = (44 * resources.displayMetrics.density).toInt()
        val margin = (3 * resources.displayMetrics.density).toInt()
        val table = findViewById<TableLayout>(R.id.tableSeats)

        for (row in 0 until ROWS) {
            val tableRow = TableRow(this)
            tableRow.layoutParams = TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )
            for (col in 0 until COLS) {
                val index = row * COLS + col
                val seat = TextView(this)
                val params = TableRow.LayoutParams(seatSize, seatSize)
                params.setMargins(margin, margin, margin, margin)
                seat.layoutParams = params
                seat.gravity = Gravity.CENTER
                seat.text = (index + 1).toString()
                seat.textSize = 9f
                seat.setTextColor(Color.WHITE)
                seatViews[index] = seat
                updateSeatColor(index)
                seat.setOnClickListener {
                    if (seatStates[index] == 1) return@setOnClickListener
                    if (seatStates[index] == 0) {
                        seatStates[index] = 2
                        selectedCount++
                    } else {
                        seatStates[index] = 0
                        selectedCount--
                    }
                    updateSeatColor(index)
                    updateSummary()
                }
                tableRow.addView(seat)
            }
            table.addView(tableRow)
        }
        updateSummary()

        val backCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (selectedCount > 0) {
                    AlertDialog.Builder(this@SeatBookingActivity)
                        .setTitle("Leave Booking?")
                        .setMessage("You have $selectedCount seat(s) selected. Are you sure you want to leave?")
                        .setPositiveButton("Leave") { _, _ -> finish() }
                        .setNegativeButton("Stay", null)
                        .show()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, backCallback)

        findViewById<Button>(R.id.btnConfirmBooking).setOnClickListener {
            if (selectedCount == 0) {
                AlertDialog.Builder(this)
                    .setTitle("No Seats Selected")
                    .setMessage("Please select at least one seat.")
                    .setPositiveButton("OK", null)
                    .show()
                return@setOnClickListener
            }
            val total = (selectedCount * pricePerSeat).toInt()
            AlertDialog.Builder(this)
                .setTitle("Booking Confirmed!")
                .setMessage("$selectedCount seat(s) booked.\nTotal: BDT $total")
                .setPositiveButton("OK") { _, _ -> finish() }
                .show()
        }
    }

    fun updateSeatColor(index: Int) {
        val seat = seatViews[index] ?: return
        when (seatStates[index]) {
            0 -> seat.setBackgroundColor(0xFF4CAF50.toInt())
            1 -> seat.setBackgroundColor(0xFFF44336.toInt())
            2 -> seat.setBackgroundColor(0xFF2196F3.toInt())
        }
    }

    fun updateSummary() {
        val total = (selectedCount * pricePerSeat).toInt()
        findViewById<TextView>(R.id.tvSelectedSeats).text = "$selectedCount seats selected"
        findViewById<TextView>(R.id.tvTotalPrice).text = "Total: BDT $total"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
