package com.bogsnebes.weareknow.ui.main_menu.icon_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bogsnebes.weareknow.R

class IconAdapter(private val icons: List<Icon>) : RecyclerView.Adapter<IconAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val iconImageView: ImageView = view.findViewById(R.id.icon_image_view)
        private val iconNameTextView: TextView = view.findViewById(R.id.icon_name_text_view)

        fun bind(icon: Icon) {
            if (icon != null) {
                iconImageView.load(icon)
            } else {
                iconImageView.load(R.drawable.ic_launcher_foreground)
            }

            iconNameTextView.text = icon.nameApp
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_main_menu, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(icons[position])
    }

    override fun getItemCount(): Int = icons.size
}