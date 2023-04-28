package com.bogsnebes.weareknow.ui.main_menu

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bogsnebes.weareknow.R
import com.bogsnebes.weareknow.ui.actions.ActionsFragment
import com.bogsnebes.weareknow.ui.main_menu.icon_adapter.IconAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class MainMenuFragment : Fragment() {
    private val mainMenuViewModel by lazy {
        ViewModelProvider(this)[MainMenuViewModel::class.java]
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var iconAdapter: IconAdapter
    private lateinit var loadListProgressBar: ProgressBar
    private lateinit var dataIsEmptyLinearLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_menu, container, false)
        dataIsEmptyLinearLayout = view.findViewById<LinearLayout?>(R.id.data_is_empty).apply {
            this.visibility = View.GONE
        }
        loadListProgressBar = view.findViewById(R.id.load_list_progressBar)
        recyclerView = view.findViewById<RecyclerView?>(R.id.main_menu_recycler).apply {
            this.layoutManager = FlexboxLayoutManager(context).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.SPACE_EVENLY
            }
            updateUI(view.context)
        }

        iconAdapter = IconAdapter() {
            (context as AppCompatActivity).supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, ActionsFragment.newInstance(it))
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        view?.context?.let { mainMenuViewModel.getItems(it) }
    }

    private fun updateUI(context: Context) {
        mainMenuViewModel.iconsScreenState.observe(viewLifecycleOwner) { iconsScreenState ->
            when (iconsScreenState) {
                is IconsScreenState.Result -> {
                    loadListProgressBar.visibility = View.GONE
                    if (iconsScreenState.items.isNotEmpty()) {
                        dataIsEmptyLinearLayout.visibility = View.GONE
                        recyclerView.adapter = iconAdapter
                        iconAdapter.setIcons(iconsScreenState.items)
                    } else {
                        recyclerView.visibility = View.GONE
                        dataIsEmptyLinearLayout.visibility = View.VISIBLE
                    }
                }
                IconsScreenState.Loading -> loadListProgressBar.visibility = View.VISIBLE
                is IconsScreenState.Error -> Toast.makeText(
                    context,
                    view?.resources?.getString(R.string.error_of_load_data),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        const val TAG = "MainMenuFragment"

        fun newInstance(): MainMenuFragment {
            return MainMenuFragment()
        }
    }
}