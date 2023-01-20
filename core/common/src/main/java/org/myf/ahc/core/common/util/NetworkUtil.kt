package org.myf.ahc.core.common.util

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import androidx.annotation.RequiresApi

object NetworkUtil {
    fun isMobileConnectedToInternet(context: Context): Boolean {
        val connectivityManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        } else {
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val currentNetwork = connectivityManager.activeNetwork
            val caps = connectivityManager.getNetworkCapabilities(currentNetwork)
            val linkProperties = connectivityManager.getLinkProperties(currentNetwork)
            linkProperties != null && caps != null
        } else {
            connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo?.isConnected ?: false
        }
    }
}