package com.bogsnebes.weareknow.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bogsnebes.weareknow.R
import com.bogsnebes.weareknow.ui.main_menu.MainMenuFragment
import com.bogsnebes.weareknow.ui.settings.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val mainMenuFragment by lazy { MainMenuFragment.newInstance() }
    private val settingsFragment by lazy { SettingsFragment.newInstance() }
    private lateinit var bottomNavigationView: BottomNavigationView

    private val bottomNavigationListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.firstFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, mainMenuFragment).commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.secondFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, settingsFragment).commit()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, mainMenuFragment)
                .commit()
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavigationListener)
    }


}