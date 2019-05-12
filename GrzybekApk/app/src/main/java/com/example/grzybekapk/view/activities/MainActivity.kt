package com.example.grzybekapk.view.activities

import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import com.example.grzybekapk.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.FirebaseFunctions
import com.crashlytics.android.Crashlytics
import com.google.firebase.auth.FirebaseUser
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Toast
import android.os.Handler
import android.webkit.WebView
import android.webkit.JavascriptInterface
import org.jsoup.Jsoup

private lateinit var functions: FirebaseFunctions
private lateinit var mAuth: FirebaseAuth
private lateinit var main : MainActivity
private var webview: WebView? = null
private const val CASurl = "https://cas.prz.edu.pl/cas-server/login?service=http%3A%2F%2Fwww.mpenar.kia.prz.edu.pl%2Fcasproxy.php%3Fredirect%3Dhttp%3A%2F%2Fwww.mpenar.kia.prz.edu.pl%26key%3Ded5fddea-9be9-4955-9718-fb429fed17f9"
private var currentUser : FirebaseUser? = null

class MainActivity : AppCompatActivity() {

    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()

        main = this

        webview = web_view

        // Get the web view settings instance
        val settings = web_view.settings

        // Enable java script in web view
        settings.javaScriptEnabled = true

        // Enable and setup web view cache
        settings.setAppCacheEnabled(true)
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.setAppCachePath(cacheDir.path)

        // Enable zooming in web view
        settings.setSupportZoom(false)
        settings.builtInZoomControls = false
        settings.displayZoomControls = false

        // Zoom web view text
        settings.textZoom = 125

        // Enable disable images in web view
        settings.blockNetworkImage = false
        // Whether the WebView should load image resources
        settings.loadsImagesAutomatically = true

        val client = URLInterceptor()
        web_view.webViewClient = client

        web_view.addJavascriptInterface( MyJavaScriptInterface(), "HTMLOUT");

        web_view.visibility = View.GONE
    }

    public override fun onStart() {
        super.onStart()
        currentUser = mAuth.currentUser
        //Check if user is logged in
        if (currentUser != null) {
            val intent = Intent(this, MainScreen::class.java)
            startActivity(intent)
        }else{
            logIn.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity()
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Wciśnij jeszcze raz żeby wyjść.", Toast.LENGTH_SHORT).show()
        Handler().postDelayed( {
            doubleBackToExitPressedOnce = false
        }, 2000)
    }

    fun onClickLogIn(v: View) {
        logIn.visibility = View.GONE
        web_view.loadUrl(CASurl)
        web_view.visibility = View.VISIBLE
    }

    fun login(response: String) {
        val data = hashMapOf(
            "response" to response
        )

        functions = FirebaseFunctions.getInstance()
        functions
            .getHttpsCallable("getCustomToken")
            .call(data)
            .continueWith { task ->
                if (task.isSuccessful) {
                    val result = task.result?.data as String
                    mAuth.signInWithCustomToken(result)
                        .addOnCompleteListener() { t ->
                            if (t.isSuccessful) {
                                Log.d("SignIN", "Signed")
                                val intent = Intent(this, MainScreen::class.java)
                                startActivity(intent)
                            } else {
                                Log.d("SignIN", "Failed")
                            }
                        }
                } else {
                    Log.d("FAILED", task.exception.toString())
                }
            }
    }
}

class URLInterceptor : WebViewClient() {

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        Log.d("onPageStarted", url)
    }

    override fun onPageFinished(view: WebView, url: String) {
        /* This call inject JavaScript into the page which just finished loading. */
        Log.d("onPageFinished", url)
        if (url!!.contains("www.mpenar.kia.prz.edu.pl/casproxy.php?redirect=http://www.mpenar.kia.prz.edu.pl&key=ed5fddea-9be9-4955-9718-fb429fed17f9&ticket=")){
            view.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');")
            view.visibility = View.GONE
        }
    }

    @Suppress("OverridingDeprecatedMember")
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        Log.d("shouldOverrideLoadDepre", url)
        return !url.contains("mpenar")
    }

    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        val url = request.url.toString()
        Log.d("shouldOverrideLoading", url)
        return !url.contains("mpenar")
    }
}

internal class MyJavaScriptInterface {

    @JavascriptInterface
    fun processHTML(html: String) {
       var document = Jsoup.parse(html)
        val divs = document.select("div")
        Log.d("processHTML", divs[0].ownText())
        tryLoginUser(webview,divs[0].ownText())
    }

    private fun checkAccess(div: String) : Boolean{
        return !div.contains("Access%20Forbidden")
    }

    private fun getResponse(div: String) : String {
        return div.substringAfter("response=")
    }

    private fun tryLoginUser(view: WebView?, div: String) {
        if (checkAccess(div)){
            main.login(getResponse(div))
            Log.d("LOGIN_JS", getResponse(div))
        }else{
            view!!.visibility = View.VISIBLE
            view!!.loadUrl(CASurl)
        }
    }
}