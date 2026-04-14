package com.example.learningportalapp

import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var webView: WebView
    lateinit var etUrl: EditText
    lateinit var progressBar: ProgressBar

    val homeUrl = "https://www.google.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)
        etUrl = findViewById(R.id.etUrl)
        progressBar = findViewById(R.id.progressBar)

        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: android.graphics.Bitmap?) {
                progressBar.visibility = ProgressBar.VISIBLE
                etUrl.setText(url)
            }

            override fun onPageFinished(view: WebView, url: String) {
                progressBar.visibility = ProgressBar.GONE
                etUrl.setText(url)
            }

            override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                if (request.isForMainFrame) {
                    view.loadUrl("file:///android_asset/offline.html")
                }
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                progressBar.progress = newProgress
            }
        }

        webView.loadUrl(homeUrl)

        findViewById<Button>(R.id.btnBack).setOnClickListener {
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                Toast.makeText(this, "No more history", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.btnForward).setOnClickListener {
            if (webView.canGoForward()) {
                webView.goForward()
            }
        }

        findViewById<Button>(R.id.btnRefresh).setOnClickListener {
            webView.reload()
        }

        findViewById<Button>(R.id.btnHome).setOnClickListener {
            webView.loadUrl(homeUrl)
        }

        findViewById<Button>(R.id.btnGo).setOnClickListener {
            loadUrlFromBar()
        }

        etUrl.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                loadUrlFromBar()
                true
            } else {
                false
            }
        }

        findViewById<Button>(R.id.btnGoogle).setOnClickListener {
            webView.loadUrl("https://www.google.com")
        }

        findViewById<Button>(R.id.btnYouTube).setOnClickListener {
            webView.loadUrl("https://www.youtube.com")
        }

        findViewById<Button>(R.id.btnWikipedia).setOnClickListener {
            webView.loadUrl("https://www.wikipedia.org")
        }

        findViewById<Button>(R.id.btnKhanAcademy).setOnClickListener {
            webView.loadUrl("https://www.khanacademy.org")
        }

        findViewById<Button>(R.id.btnUniversity).setOnClickListener {
            webView.loadUrl("https://www.aiub.edu")
        }
    }

    fun loadUrlFromBar() {
        var url = etUrl.text.toString().trim()
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://$url"
        }
        webView.loadUrl(url)
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
