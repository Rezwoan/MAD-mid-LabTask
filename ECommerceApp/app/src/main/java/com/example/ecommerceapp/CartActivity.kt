package com.example.ecommerceapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar

class CartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewCart)
        val tvTotal = findViewById<TextView>(R.id.tvTotal)
        val btnCheckout = findViewById<Button>(R.id.btnCheckout)

        @Suppress("UNCHECKED_CAST")
        val cartItems = intent.getSerializableExtra("CART_ITEMS") as? ArrayList<Product> ?: arrayListOf()
        
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CartAdapter(cartItems)

        val total = cartItems.sumOf { it.price }
        tvTotal.text = String.format("Total: $%.2f", total)

        btnCheckout.setOnClickListener {
            Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show()
        }
    }
}
