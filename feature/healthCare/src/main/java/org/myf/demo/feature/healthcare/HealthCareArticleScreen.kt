package org.myf.demo.feature.healthcare

import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs

class HealthCareArticleScreen : Fragment(
    R.layout.screen_health_care_article
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val webView =  view.findViewById<WebView>(R.id.health_care_web_view)
        val args by navArgs<HealthCareArticleScreenArgs>()
        webView.webChromeClient = WebChromeClient()
        webView.settings.builtInZoomControls = true
        webView.settings.displayZoomControls = false
        try {
            args.url.let {
                webView.loadUrl(it)
            }
        }catch (e: Exception){
            Log.e("web_view",e.message.toString())
        }
    }
}