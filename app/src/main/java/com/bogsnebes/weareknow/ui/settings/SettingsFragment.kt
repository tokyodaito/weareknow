package com.bogsnebes.weareknow.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bogsnebes.weareknow.R

class SettingsFragment : Fragment() {
    private val settingsViewModel by lazy {
        ViewModelProvider(this)[SettingsViewModel::class.java]
    }

    private lateinit var followAllAppsSwitch: SwitchCompat
    private lateinit var deleteAllLogsButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        followAllAppsSwitch = view.findViewById<SwitchCompat?>(R.id.switch_follow_all_apps).apply {
            this.setOnClickListener {
                settingsViewModel.onSwitchStateChanged(requireContext(), isChecked) {
                    Toast.makeText(
                        view.context,
                        resources.getString(R.string.enable_instruction),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            changeSwitchMode()
        }

        deleteAllLogsButton = view.findViewById<Button?>(R.id.delete_all_logs_button).apply {
            this.setOnClickListener {
                settingsViewModel.deleteAllData()
                showResultDeleteData(view.context)
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        view?.let {
            settingsViewModel.checkSwitchState(it.context)
        }
    }

    private fun showResultDeleteData(context: Context) {
        settingsViewModel.deleteDataScreenState.observe(viewLifecycleOwner) { DeleteDataScreenState ->
            when (DeleteDataScreenState) {
                is DeleteDataScreenState.Result -> Toast.makeText(
                    context,
                    resources.getString(R.string.data_deleted),
                    Toast.LENGTH_SHORT
                ).show()
                is DeleteDataScreenState.Loading -> TODO()
                is DeleteDataScreenState.Error -> Toast.makeText(
                    context,
                    resources.getString(R.string.error_delete_of_data),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun changeSwitchMode() {
        settingsViewModel.switchState.observe(viewLifecycleOwner) { isChecked ->
            followAllAppsSwitch.isChecked = isChecked
        }
    }

    companion object {
        const val TAG = "SettingsFragment"

        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }
}