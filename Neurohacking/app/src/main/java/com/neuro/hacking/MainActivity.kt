package com.neuro.hacking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.neuro.hacking.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //set support action bar
        //setSupportActionBar(binding.toolbarMain.root)

        //get link on the nav host fragment and get controller for it
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        //get NavController
        navController = navHost.navController
        val builder = AppBarConfiguration.Builder(navController.graph)
        val appBarConfiguration = builder.build()
        //binding.toolbarMain.root.setupWithNavController(navController, appBarConfiguration)
        //set navigation between bottom bar icons
        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_bar)
        bottomNavView.setupWithNavController(navController)
    }
}