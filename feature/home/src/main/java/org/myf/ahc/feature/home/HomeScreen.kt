package org.myf.ahc.feature.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment

class HomeScreen : Fragment(
    R.layout.screen_home
) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId ==  R.id.action_home_settings) {
            val dialog = SettingsDialog()
            dialog.show(childFragmentManager,"")
            dialog.isAdded
        }else{
            super.onOptionsItemSelected(item)
        }
    }
}