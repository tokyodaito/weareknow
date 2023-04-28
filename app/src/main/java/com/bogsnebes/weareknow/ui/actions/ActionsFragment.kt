package com.bogsnebes.weareknow.ui.actions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bogsnebes.weareknow.R
import com.bogsnebes.weareknow.ui.actions.actions_adapter.Action
import com.bogsnebes.weareknow.ui.actions.actions_adapter.ActionAdapter

class ActionsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val actions: List<Action> = arguments?.getParcelableArrayList(ARG_ACTIONS) ?: emptyList()

        val view = inflater.inflate(R.layout.fragment_action, container, false)
        recyclerView = view.findViewById<RecyclerView?>(R.id.actions_recycler).apply {
            this.layoutManager = LinearLayoutManager(view.context)
            this.adapter = ActionAdapter(context, actions)
        }
        return view
    }

    companion object {
        const val TAG = "ActionsFragment"
        private const val ARG_ACTIONS = "actions"

        fun newInstance(actions: List<Action>): ActionsFragment {
            val fragment = ActionsFragment()
            val args = Bundle()
            args.putParcelableArrayList(ARG_ACTIONS, ArrayList(actions))
            fragment.arguments = args
            return fragment
        }
    }
}