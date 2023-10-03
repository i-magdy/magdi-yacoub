package org.myf.demo.feature.home.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.window.Dialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.myf.demo.feature.home.R
import org.myf.demo.feature.home.adapter.StoriesAdapter
import org.myf.demo.feature.home.databinding.ScreenHomeBinding
import org.myf.demo.feature.home.ui.settings.SettingsDialog
import org.myf.demo.ui.theme.AppTheme

@AndroidEntryPoint
class HomeScreen : Fragment() {

    private lateinit var binding: ScreenHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ScreenHomeBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel by viewModels<HomeViewModel>()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.getData()
        val adapter = StoriesAdapter()
        binding.homeDialog.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AppTheme {
                    AlertDialog(
                        modifier = Modifier,
                        onDismissRequest = { },
                        confirmButton = {},
                        title = { Text(text = "Settings")},
                        text = { Text(text = "change language")}
                    )
                }
            }
        }

        binding.homeRoot.viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener{
                override fun onGlobalLayout() {
                    binding.homeRoot.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val params = binding.mainHomeLayout.layoutParams
                    params.height = binding.homeRoot.height
                    binding.mainHomeLayout.layoutParams = params
                }
            })
        binding.homeStoriesRv.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        }
        lifecycleScope.launch {
            viewModel.uiState.map {
                it.stories
            }.collect(adapter::setStories)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_home_settings) {
            val dialog = SettingsDialog()
            dialog.show(childFragmentManager,"")
            dialog.isAdded
        }else{
            super.onOptionsItemSelected(item)
        }
    }
}

