package com.example.ecommerceapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter(
    private val products: MutableList<Product>,
    var isGridMode: Boolean,
    val onCartToggle: (Product, Int) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    companion object {
        const val VIEW_TYPE_LIST = 0
        const val VIEW_TYPE_GRID = 1
    }

    abstract class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(product: Product)
    }

    inner class ListViewHolder(view: View) : ProductViewHolder(view) {
        val ivProduct: ImageView = view.findViewById(R.id.ivProduct)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvCategory: TextView = view.findViewById(R.id.tvCategory)
        val ratingBar: RatingBar = view.findViewById(R.id.ratingBar)
        val tvPrice: TextView = view.findViewById(R.id.tvPrice)
        val btnAddToCart: Button = view.findViewById(R.id.btnAddToCart)

        override fun bind(product: Product) {
            ivProduct.setImageResource(product.imageRes)
            tvName.text = product.name
            tvCategory.text = product.category
            ratingBar.rating = product.rating
            tvPrice.text = String.format("$%.2f", product.price)
            btnAddToCart.text = if (product.inCart) "Remove" else "Add to Cart"
            btnAddToCart.setOnClickListener { onCartToggle(product, adapterPosition) }
        }
    }

    inner class GridViewHolder(view: View) : ProductViewHolder(view) {
        val ivProduct: ImageView = view.findViewById(R.id.ivProduct)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvPrice: TextView = view.findViewById(R.id.tvPrice)
        val ibCart: ImageButton = view.findViewById(R.id.ibCart)

        override fun bind(product: Product) {
            ivProduct.setImageResource(product.imageRes)
            tvName.text = product.name
            tvPrice.text = String.format("$%.2f", product.price)
            ibCart.setImageResource(
                if (product.inCart) android.R.drawable.ic_menu_close_clear_cancel
                else android.R.drawable.ic_input_add
            )
            ibCart.setOnClickListener { onCartToggle(product, adapterPosition) }
        }
    }

    override fun getItemViewType(position: Int) = if (isGridMode) VIEW_TYPE_GRID else VIEW_TYPE_LIST

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_GRID) {
            GridViewHolder(inflater.inflate(R.layout.item_product_grid, parent, false))
        } else {
            ListViewHolder(inflater.inflate(R.layout.item_product_list, parent, false))
        }
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount() = products.size

    fun updateProducts(newList: List<Product>) {
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize() = products.size
            override fun getNewListSize() = newList.size
            override fun areItemsTheSame(oldPos: Int, newPos: Int) =
                products[oldPos].id == newList[newPos].id
            override fun areContentsTheSame(oldPos: Int, newPos: Int) =
                products[oldPos] == newList[newPos]
        })
        products.clear()
        products.addAll(newList)
        diff.dispatchUpdatesTo(this)
    }

    fun removeAt(position: Int): Product {
        val removed = products.removeAt(position)
        notifyItemRemoved(position)
        return removed
    }

    fun insertAt(position: Int, product: Product) {
        products.add(position, product)
        notifyItemInserted(position)
    }

    fun moveItem(from: Int, to: Int) {
        val item = products.removeAt(from)
        products.add(to, item)
        notifyItemMoved(from, to)
    }

    fun getCurrentList(): List<Product> = products.toList()
}
