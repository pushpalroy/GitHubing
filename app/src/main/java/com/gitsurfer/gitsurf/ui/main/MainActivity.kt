package com.gitsurfer.gitsurf.ui.main

import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.gitsurfer.gitsurf.R
import com.gitsurfer.gitsurf.databinding.ActivityMainBinding
import com.gitsurfer.gitsurf.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.drawerLayout
import kotlinx.android.synthetic.main.activity_main.navigationView
import kotlinx.android.synthetic.main.activity_main.toolbar

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

  override fun getViewModelClass() = MainViewModel::class.java
  override fun getLayoutId() = R.layout.activity_main

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setSupportActionBar(toolbar)
    setUpNavigation()
  }

  override fun onSupportNavigateUp(): Boolean {
    // Ensures that the menu items in the Nav Drawer stay in sync with the navigation graph
    return navigateUp(findNavController(R.id.nav_host_fragment), drawerLayout)
  }

  private fun setUpNavigation() {
    val navController = findNavController(R.id.nav_host_fragment)

    // Ensures that the title in the action bar will automatically be updated when the
    // destination changes. In addition, the Up button will be displayed when you are on a
    // non-root destination and the hamburger icon will be displayed when on the root destination
    setupActionBarWithNavController(navController, drawerLayout)

    // Handle nav drawer item clicks
    navigationView.setNavigationItemSelectedListener { menuItem ->
      menuItem.isChecked = true
      drawerLayout.closeDrawers()
      true
    }

    // Ensures that the selected item in the NavigationView will automatically be updated
    // when the destination changes
    setupWithNavController(navigationView, navController)
  }

  override fun onBackPressed() {
    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
      drawerLayout.closeDrawer(GravityCompat.START)
    } else {
      super.onBackPressed()
    }
  }
}