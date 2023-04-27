package com.bogsnebes.weareknow.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bogsnebes.weareknow.R
import com.bogsnebes.weareknow.ui.main_menu.MainMenuFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val mainMenuFragment by lazy { MainMenuFragment.newInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, mainMenuFragment)
                .commit()
        }
    }
}