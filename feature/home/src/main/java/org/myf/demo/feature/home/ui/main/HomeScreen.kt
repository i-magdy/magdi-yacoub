package org.myf.demo.feature.home.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.myf.demo.feature.home.R
import org.myf.demo.feature.home.adapter.StoriesAdapter
import org.myf.demo.feature.home.databinding.ScreenHomeBinding
import org.myf.demo.feature.home.ui.settings.HomeSettingDialog
import org.myf.demo.ui.theme.AppTheme

@AndroidEntryPoint
class HomeScreen : Fragment() {

    private lateinit var binding: ScreenHomeBinding

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
        attachMenu(
            viewModel = viewModel
        )
        binding.apply {
            this.viewModel = viewModel
            this.lifecycleOwner = this@HomeScreen.viewLifecycleOwner
        }
        viewModel.getData()
        val adapter = StoriesAdapter()
        binding.homeDialog.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AppTheme {
                    HomeSettingDialog(
                        viewModel = viewModel
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
                it.model.stories
            }.collect(adapter::setStories)
        }
    }

    private fun attachMenu(
        viewModel: HomeViewModel
    ){
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_home,menu)
                menu.findItem(R.id.action_home_settings)
                    .setOnMenuItemClickListener {
                        viewModel.openHomeSettingDialog()
                        true
                    }
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean = false
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

}