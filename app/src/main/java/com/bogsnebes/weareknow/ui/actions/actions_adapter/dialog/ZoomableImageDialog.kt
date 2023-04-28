package com.bogsnebes.weareknow.ui.actions.actions_adapter.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.updatePadding
import androidx.fragment.app.DialogFragment
import coil.load
import com.bogsnebes.weareknow.R

class ZoomableImageDialog : DialogFragment() {

    private var imagePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imagePath = arguments?.getString(ARG_IMAGE_PATH)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = ImageView(requireContext())
        imagePath?.let { path ->
            view.load(path) {
                crossfade(true)
                listener(
                    onError = { _, _ ->
                        view.load(R.drawable.aynami_rei) {
                            crossfade(true)
                        }
                    }
                )
            }
        }
        view.setOnTouchListener(ImageZoomTouchListener(requireContext()))
        view.updatePadding(16, 16, 16, 16)
        return view
    }

    companion object {
        private const val ARG_IMAGE_PATH = "image_path"

        fun newInstance(imagePath: String): ZoomableImageDialog {
            val args = Bundle()
            args.putString(ARG_IMAGE_PATH, imagePath)
            val fragment = ZoomableImageDialog()
            fragment.arguments = args
            return fragment
        }
    }
}