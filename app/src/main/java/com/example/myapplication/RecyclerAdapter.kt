package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private var titles = arrayOf("First heading", "Second heading", "Third heading", "Fourth heading", "Fifth heading",
            "Sixth heading", "Seventh heading", "Eighth heading", "Ninth heading", "Tenth heading")
    private val details = arrayOf("First description", "Second description", "The third description", "Fourth description",
            "Fifth description", "Sixth description", "Seventh description", "Eighth description", "Ninth description", "Tenth description")
    private var images = intArrayOf(R.drawable.icon, R.drawable.icon, R.drawable.icon, R.drawable.icon,
            R.drawable.icon, R.drawable.icon, R.drawable.icon, R.drawable.icon, R.drawable.icon, R.drawable.icon)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return titles.size
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        holder.itemTitle.text = titles[position]
        holder.itemDetail.text = details[position]
        holder.itemImage.setImageResource(images[position])
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView
        var itemTitle: TextView
        var itemDetail: TextView

        init {
            itemImage = itemView.findViewById(R.id.cardIcon)
            itemTitle = itemView.findViewById(R.id.text_title)
            itemDetail = itemView.findViewById(R.id.text_description)
        }
    }
}