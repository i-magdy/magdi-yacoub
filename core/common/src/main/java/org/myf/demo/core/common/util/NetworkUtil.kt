package org.myf.demo.core.common.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.content.getSystemService

object NetworkUtil {

    @Suppress("DEPRECATION")
    fun isMobileConnectedToInternet(
        context: Context
    ): Boolean = when (
        val connectivityManager = context.getSystemService<ConnectivityManager>()
    ) {
            null -> false
            else -> when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ->
                    connectivityManager.activeNetwork
                        ?.let (connectivityManager::getNetworkCapabilities)
                        ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                        ?: false
                else -> connectivityManager.activeNetworkInfo?.isConnected ?: false
            }
        }
}