package org.myf.ahc.util

import android.content.Context
import android.net.ConnectivityManager

object NetworkUtil {

    fun isMobileConnectedToInternet(context: Context):Boolean{
        val connectivityManager = context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        val currentNetwork = connectivityManager.activeNetwork
        val caps = connectivityManager.getNetworkCapabilities(currentNetwork)
        val linkProperties = connectivityManager.getLinkProperties(currentNetwork)
        return linkProperties != null && caps != null
    }
}