package com.example.contactbookapp

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class ContactAdapter(context: Context, contacts: MutableList<Contact>) :
    ArrayAdapter<Contact>(context, 0, contacts) {

    val avatarColors = mapOf(
        'A' to "#F44336", 'B' to "#E91E63", 'C' to "#9C27B0", 'D' to "#673AB7",
        'E' to "#3F51B5", 'F' to "#2196F3", 'G' to "#03A9F4", 'H' to "#00BCD4",
        'I' to "#009688", 'J' to "#4CAF50", 'K' to "#8BC34A", 'L' to "#CDDC39",
        'M' to "#FFC107", 'N' to "#FF9800", 'O' to "#FF5722", 'P' to "#795548",
        'Q' to "#9E9E9E", 'R' to "#607D8B", 'S' to "#F44336", 'T' to "#E91E63",
        'U' to "#9C27B0", 'V' to "#3F51B5", 'W' to "#2196F3", 'X' to "#009688",
        'Y' to "#4CAF50", 'Z' to "#FF9800"
    )

    class ViewHolder {
        lateinit var tvAvatar: TextView
        lateinit var tvName: TextView
        lateinit var tvPhone: TextView
        lateinit var ivCall: ImageView
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder: ViewHolder
        val view: View
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false)
            holder = ViewHolder()
            holder.tvAvatar = view.findViewById(R.id.tvAvatar)
            holder.tvName = view.findViewById(R.id.tvName)
            holder.tvPhone = view.findViewById(R.id.tvPhone)
            holder.ivCall = view.findViewById(R.id.ivCall)
            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }
        val contact = getItem(position)!!
        holder.tvAvatar.text = contact.initial
        holder.tvName.text = contact.name
        holder.tvPhone.text = contact.phone
        val colorHex = avatarColors[contact.initial.uppercase().firstOrNull()] ?: "#607D8B"
        val drawable = GradientDrawable()
        drawable.shape = GradientDrawable.OVAL
        drawable.setColor(Color.parseColor(colorHex))
        holder.tvAvatar.background = drawable
        return view
    }
}
