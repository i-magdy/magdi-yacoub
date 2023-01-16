package org.myf.ahc.feature.registration.util

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import androidx.annotation.RequiresApi

object NetworkUtil {
    fun isMobileConnectedToInternet(context: Context):Boolean{
        val connectivityManager = context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        val currentNetwork = connectivityManager.activeNetwork
        val caps = connectivityManager.getNetworkCapabilities(currentNetwork)
        val linkProperties = connectivityManager.getLinkProperties(currentNetwork)
        return linkProperties != null && caps != null
    }
}