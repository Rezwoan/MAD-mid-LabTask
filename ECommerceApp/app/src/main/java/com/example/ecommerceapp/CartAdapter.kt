package com.example.ecommerceapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CartAdapter(private val items: List<Product>) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivProduct: ImageView = view.findViewById(R.id.ivProduct)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvCategory: TextView = view.findViewById(R.id.tvCategory)
        val tvPrice: TextView = view.findViewById(R.id.tvPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CartViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false))

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = items[position]
        holder.ivProduct.setImageResource(product.imageRes)
        holder.tvName.text = product.name
        holder.tvCategory.text = product.category
        holder.tvPrice.text = String.format("$%.2f", product.price)
    }

    override fun getItemCount() = items.size
}
