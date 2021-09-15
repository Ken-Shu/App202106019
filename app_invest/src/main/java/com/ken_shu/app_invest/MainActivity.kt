package com.ken_shu.app_invest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val url = "http://10.0.2.2:8080/AppWebBackend/investor_transactionlog.jsp"
        web_view.settings.javaScriptEnabled = true
        web_view.settings.domStorageEnabled = true
        web_view.webChromeClient = WebChromeClient()
        WebView.setWebContentsDebuggingEnabled(true)
        web_view.loadUrl(url)
    }
}