package org.myf.ahc

import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import org.myf.ahc.ui.BaseActivity

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}