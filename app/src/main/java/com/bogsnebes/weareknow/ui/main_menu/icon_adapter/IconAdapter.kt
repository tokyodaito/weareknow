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
import com.bogsnebes.weareknow.ui.actions.actions_adapter.Action

class IconAdapter(private val icons: List<Icon>, val openActionsFragment: (List<Action>) -> Unit) :
    RecyclerView.Adapter<IconAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val iconImageView: ImageView = view.findViewById(R.id.icon_image_view)
        private val iconNameTextView: TextView = view.findViewById(R.id.icon_name_text_view)

        fun bind(icon: Icon) {
            iconImageView.load(icon.iconImage) {
                transformations(CircleCropTransformation())
                listener(
                    onError = { _, _ ->
                        iconImageView.load(R.drawable.aynami_rei) {
                            transformations(CircleCropTransformation())
                        }
                    }
                )
            }
                iconNameTextView.text = icon.nameApp

            itemView.setOnClickListener {
                openActionsFragment(icon.actions)
            }
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