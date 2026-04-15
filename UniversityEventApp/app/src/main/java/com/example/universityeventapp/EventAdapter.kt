package com.example.universityeventapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EventAdapter(
    private var events: List<Event>,
    private val onClick: (Event) -> Unit
) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivBanner: ImageView = view.findViewById(R.id.ivEventBanner)
        val tvTitle: TextView = view.findViewById(R.id.tvEventTitle)
        val tvDate: TextView = view.findViewById(R.id.tvEventDate)
        val tvVenue: TextView = view.findViewById(R.id.tvEventVenue)
        val tvSeats: TextView = view.findViewById(R.id.tvEventSeats)
        val tvPrice: TextView = view.findViewById(R.id.tvEventPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = events[position]
        holder.ivBanner.setBackgroundResource(event.imageRes)
        holder.tvTitle.text = event.title
        holder.tvDate.text = event.date
        holder.tvVenue.text = event.venue
        holder.tvSeats.text = "Seats: ${event.availableSeats}"
        holder.tvPrice.text = if (event.price == 0.0) "Free" else "BDT ${event.price.toInt()}"
        holder.itemView.setOnClickListener { onClick(event) }
    }

    override fun getItemCount() = events.size

    fun updateList(newList: List<Event>) {
        events = newList
        notifyDataSetChanged()
    }
}
