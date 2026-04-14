package com.example.photogalleryapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView

class PhotoAdapter(
    val context: Context,
    val photos: MutableList<Photo>,
    var selectionMode: Boolean
) : BaseAdapter() {

    override fun getCount() = photos.size

    override fun getItem(position: Int) = photos[position]

    override fun getItemId(position: Int) = photos[position].id.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false)
        val photo = photos[position]
        val imgPhoto = view.findViewById<ImageView>(R.id.imgPhoto)
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val checkBox = view.findViewById<CheckBox>(R.id.checkBox)
        imgPhoto.setImageResource(photo.resourceId)
        tvTitle.text = photo.title
        checkBox.visibility = if (selectionMode) View.VISIBLE else View.GONE
        checkBox.isChecked = photo.isSelected
        return view
    }
}
