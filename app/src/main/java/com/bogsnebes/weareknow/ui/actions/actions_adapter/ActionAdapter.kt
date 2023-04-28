package com.bogsnebes.weareknow.ui.actions.actions_adapter

import android.app.Activity
import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.bogsnebes.weareknow.R

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
                        actionImage.setOnClickListener { view ->
                            val context = view.context
                            val zoomedImageView = ImageView(context).apply {
                                layoutParams = FrameLayout.LayoutParams(
                                    FrameLayout.LayoutParams.MATCH_PARENT,
                                    FrameLayout.LayoutParams.MATCH_PARENT
                                )
                                scaleType = ImageView.ScaleType.FIT_CENTER
                                setBackgroundColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.transparent_black
                                    )
                                )

                                action.imageResource?.let { path ->
                                    load(path) {
                                        crossfade(true)
                                        listener(
                                            onError = { _, _ ->
                                                load(R.drawable.aynami_rei) {
                                                    crossfade(true)
                                                }
                                            }
                                        )
                                    }
                                }
                                setOnClickListener {
                                    (parent as? ViewGroup)?.removeView(this)
                                }
                            }

                            val parentView =
                                (context as? Activity)?.findViewById<ViewGroup>(android.R.id.content)
                            parentView?.addView(zoomedImageView)

                            zoomedImageView.elevation = 10f
                            val paddingInPixels = TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, 32f,
                                context.resources.displayMetrics
                            ).toInt()
                            zoomedImageView.setPadding(paddingInPixels, paddingInPixels, paddingInPixels, paddingInPixels)

                            val zoomInAnimation = ScaleAnimation(
                                0.5f, 1f, 0.5f, 1f,
                                Animation.RELATIVE_TO_SELF, 0.5f,
                                Animation.RELATIVE_TO_SELF, 0.5f
                            ).apply {
                                duration = 300
                                interpolator = AccelerateDecelerateInterpolator()
                            }

                            zoomedImageView.startAnimation(zoomInAnimation)
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