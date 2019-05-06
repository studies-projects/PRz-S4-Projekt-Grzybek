package com.example.grzybekapk.view.activities

import android.content.Intent
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

private lateinit var functions: FirebaseFunctions
private lateinit var mAuth: FirebaseAuth
private lateinit var main : MainActivity
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
            "response" to "${response}"
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

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)

        if (url!!.contains("http://www.mpenar.kia.prz.edu.pl/?response=")) {
            Log.d("OVERRIDE", "TRUE")
            tryLoginUser(view, url)
        }

        Log.d("OVERRIDE", "false")
    }

    private fun checkAccess(url: String) : Boolean{
        return !url.contains("Access%20Forbidden")
    }

    private fun getResponse(url: String) : String {
        return url.substringAfter("response=")
    }

    private fun tryLoginUser(view: WebView?, url: String) {
        if (checkAccess(url)){
            main.login(getResponse(url))
            Log.d("LOGIN", getResponse(url))
            view!!.visibility = View.GONE
        }else{
            view!!.loadUrl(CASurl)
        }
    }
}