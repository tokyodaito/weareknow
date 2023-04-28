package com.bogsnebes.weareknow.ui.actions.actions_adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.bogsnebes.weareknow.R
import com.bogsnebes.weareknow.ui.actions.actions_adapter.dialog.ZoomableImageDialog

class ActionAdapter(private val context: Context, private val actions: List<Action>) :
    RecyclerView.Adapter<ActionAdapter.ActionViewHolder>() {

    inner class ActionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val aynami_rei = listOf<Int>(
            R.drawable.aynami_rei,
            R.drawable.aynami_rei_1,
            R.drawable.aynami_rei_2,
            R.drawable.aynami_rei_3
        )

        private val textAction: TextView = view.findViewById(R.id.text_action)
        private val date: TextView = view.findViewById(R.id.date)
        private val actionImage: ImageView = view.findViewById(R.id.screenshot_image)

        fun bind(action: Action) {
            textAction.text =
                context.getString(R.string.action_text_format, action.id, action.action)
            date.text = action.date
            actionImage.load(action.imageResource) {
                crossfade(true)
                transformations(RoundedCornersTransformation(radius = 16f))
                listener(
                    onError = { _, _ ->
                        actionImage.load(aynami_rei.random()) {
                            crossfade(true)
                            transformations(RoundedCornersTransformation(radius = 16f))
                        }
                    }, onSuccess = { _, _ ->
                        actionImage.setOnClickListener {
                            val dialog = ZoomableImageDialog.newInstance(action.imageResource!!)
                            dialog.show(
                                (context as AppCompatActivity).supportFragmentManager,
                                "zoomable_image_dialog"
                            )
                        }
                    }
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.list_item_actions, parent, false)
        return ActionViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActionViewHolder, position: Int) {
        holder.bind(actions[position])
    }

    override fun getItemCount(): Int {
        return actions.size
    }
}