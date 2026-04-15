package com.example.ecommerceapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    lateinit var adapter: ProductAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var toolbar: MaterialToolbar
    lateinit var skeletonView: LinearLayout
    lateinit var emptyState: LinearLayout
    lateinit var cartBadge: BadgeDrawable

    val allProducts = mutableListOf<Product>()
    var currentCategory = "All"
    var currentQuery = ""
    var isGridMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        recyclerView = findViewById(R.id.recyclerView)
        skeletonView = findViewById(R.id.skeletonView)
        emptyState = findViewById(R.id.emptyState)

        setupProducts()
        setupCategoryFilter()
        showSkeleton()

        Handler(Looper.getMainLooper()).postDelayed({
            hideSkeleton()
            setupRecyclerView()
            setupItemTouchHelper()
        }, 1500)
    }

    fun setupProducts() {
        allProducts.addAll(listOf(
            Product(1, "Smartphone X", 499.99, 4.5f, "Electronics", R.drawable.ic_launcher_foreground),
            Product(2, "Wireless Headphones", 79.99, 4.2f, "Electronics", R.drawable.ic_launcher_foreground),
            Product(3, "T-Shirt Classic", 19.99, 3.8f, "Clothing", R.drawable.ic_launcher_foreground),
            Product(4, "Denim Jeans", 49.99, 4.0f, "Clothing", R.drawable.ic_launcher_foreground),
            Product(5, "Android Programming", 29.99, 4.7f, "Books", R.drawable.ic_launcher_foreground),
            Product(6, "Kotlin in Action", 34.99, 4.9f, "Books", R.drawable.ic_launcher_foreground),
            Product(7, "Organic Almonds", 12.99, 4.3f, "Food", R.drawable.ic_launcher_foreground),
            Product(8, "Green Tea Pack", 8.99, 4.1f, "Food", R.drawable.ic_launcher_foreground),
            Product(9, "LEGO Set 500", 59.99, 4.6f, "Toys", R.drawable.ic_launcher_foreground),
            Product(10, "Remote Car", 39.99, 4.4f, "Toys", R.drawable.ic_launcher_foreground)
        ))
    }

    fun setupCategoryFilter() {
        val chipGroup = findViewById<ChipGroup>(R.id.chipGroupCategory)
        listOf("All", "Electronics", "Clothing", "Books", "Food", "Toys").forEach { category ->
            val chip = Chip(this)
            chip.text = category
            chip.isCheckable = true
            chip.isChecked = category == "All"
            chip.setOnClickListener {
                currentCategory = category
                applyFilter()
            }
            chipGroup.addView(chip)
        }
    }

    fun setupRecyclerView() {
        val filtered = getFilteredProducts().toMutableList()
        adapter = ProductAdapter(filtered, isGridMode) { product, _ -> toggleCart(product) }
        recyclerView.layoutManager = if (isGridMode) GridLayoutManager(this, 2) else LinearLayoutManager(this)
        recyclerView.adapter = adapter
        updateEmptyState()
    }

    fun getFilteredProducts(): List<Product> {
        return allProducts.filter { product ->
            (currentCategory == "All" || product.category == currentCategory) &&
            (currentQuery.isEmpty() || product.name.contains(currentQuery, ignoreCase = true))
        }
    }

    fun applyFilter() {
        if (!::adapter.isInitialized) return
        adapter.updateProducts(getFilteredProducts())
        updateEmptyState()
    }

    fun updateEmptyState() {
        if (!::adapter.isInitialized) return
        if (adapter.itemCount == 0) {
            emptyState.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            emptyState.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

    fun showSkeleton() {
        skeletonView.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    fun hideSkeleton() {
        skeletonView.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    fun toggleCart(product: Product) {
        val idx = allProducts.indexOfFirst { it.id == product.id }
        if (idx == -1) return
        allProducts[idx].inCart = !allProducts[idx].inCart
        if (allProducts[idx].inCart) {
            CartManager.cartItems.add(allProducts[idx])
        } else {
            CartManager.cartItems.removeAll { it.id == product.id }
        }
        applyFilter()
        updateCartBadge()
    }

    fun updateCartBadge() {
        if (!::cartBadge.isInitialized) return
        val count = CartManager.cartItems.size
        cartBadge.isVisible = count > 0
        if (count > 0) cartBadge.number = count
    }

    fun setupItemTouchHelper() {
        val callback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                rv: RecyclerView,
                vh: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                adapter.moveItem(vh.adapterPosition, target.adapterPosition)
                return true
            }

            override fun onSwiped(vh: RecyclerView.ViewHolder, direction: Int) {
                val position = vh.adapterPosition
                val removed = adapter.removeAt(position)
                val allIdx = allProducts.indexOfFirst { it.id == removed.id }
                if (allIdx != -1) allProducts.removeAt(allIdx)
                if (removed.inCart) {
                    CartManager.cartItems.removeAll { it.id == removed.id }
                    updateCartBadge()
                }
                updateEmptyState()
                Snackbar.make(recyclerView, "${removed.name} removed", Snackbar.LENGTH_LONG)
                    .setAction("UNDO") {
                        val insertIdx = if (allIdx != -1) allIdx.coerceAtMost(allProducts.size) else allProducts.size
                        allProducts.add(insertIdx, removed)
                        if (removed.inCart) {
                            CartManager.cartItems.add(removed)
                            updateCartBadge()
                        }
                        applyFilter()
                        updateEmptyState()
                    }
                    .show()
            }
        }
        ItemTouchHelper(callback).attachToRecyclerView(recyclerView)
    }

    @SuppressLint("UnsafeOptInUsageError")
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                currentQuery = newText ?: ""
                applyFilter()
                return true
            }
        })

        cartBadge = BadgeDrawable.create(this)
        cartBadge.isVisible = false
        toolbar.post {
            BadgeUtils.attachBadgeDrawable(cartBadge, toolbar, R.id.action_cart)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_cart -> {
                startActivity(Intent(this, CartActivity::class.java))
                true
            }
            R.id.action_toggle_view -> {
                isGridMode = !isGridMode
                item.setIcon(if (isGridMode) R.drawable.ic_view_grid else R.drawable.ic_view_list)
                if (::adapter.isInitialized) {
                    adapter.isGridMode = isGridMode
                    recyclerView.layoutManager = if (isGridMode) GridLayoutManager(this, 2) else LinearLayoutManager(this)
                    adapter.notifyDataSetChanged()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        updateCartBadge()
    }
}
