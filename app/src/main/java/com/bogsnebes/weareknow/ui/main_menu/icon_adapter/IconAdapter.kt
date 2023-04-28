package com.bogsnebes.weareknow.ui.main_menu.icon_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.bogsnebes.weareknow.R

class IconAdapter(private val icons: List<Icon>) : RecyclerView.Adapter<IconAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val iconImageView: ImageView = view.findViewById(R.id.icon_image_view)
        private val iconNameTextView: TextView = view.findViewById(R.id.icon_name_text_view)

        fun bind(icon: Icon) {
            if (icon.iconImage != null) {
                iconImageView.load(icon.iconImage) {
                    transformations(CircleCropTransformation())
                }
            } else {
                iconImageView.load(R.drawable.ic_android_green_40dp) {
                    transformations(CircleCropTransformation())
                }
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