package com.CuisineQuest.edu.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.google.android.material.appbar.MaterialToolbar
import com.CuisineQuest.edu.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var appBarConfig: AppBarConfiguration


    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        Thread.sleep(3000)
        installSplashScreen()
        setContentView(R.layout.activity_main)

        val swipeRefreshLayout: SwipeRefreshLayout = findViewById(R.id.srl)

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment
        ) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfig = AppBarConfiguration(navController.graph)

        val appBar: MaterialToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(appBar)
        setupActionBarWithNavController(navController, appBarConfig)

        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNav.setupWithNavController(navController)
        bottomNav.setOnApplyWindowInsetsListener { view, insets ->
            view.updatePadding(bottom = 0)
            insets
        }


        addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.activity_main_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.settings -> {
                            findNavController(R.id.nav_host_fragment).navigate(R.id.navigate_to_settingsFragment)
                            Log.d("Prints", "Clicked on settings")

                            true
                        }
                        else -> false
                    }
                }

            },
            this,
            Lifecycle.State.STARTED
        )


    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfig) || super.onSupportNavigateUp()
    }


}