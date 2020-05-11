package com.example.webviewapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.fragment_web.*

/**
 * A simple [Fragment] subclass.
 */
@Suppress("DEPRECATION")
class WebFragment : Fragment() {

    private var sharedPreferences: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_web, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = activity?.getSharedPreferences(NAME_SHARED_PREF, Context.MODE_PRIVATE)

        val action = arguments?.let { WebFragmentArgs.fromBundle(it).account }
        action?.let { act ->
            sharedPreferences?.let {
                webView.initialize(act, it)
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun WebView.initialize(action: Int, sharedPreferences: SharedPreferences) {

        settings.apply {
            javaScriptEnabled = true
            setAppCacheEnabled(true)
        }
        isSoundEffectsEnabled = true

        val cookiesSaved = sharedPreferences.readCookie(action)
        val array = cookiesSaved?.split(";")?.toTypedArray()
        Log.d(LOG, "SAVED COOKIES: $cookiesSaved")
        val cookieSyncManager: CookieSyncManager = CookieSyncManager.createInstance(this.context)
        val cookieManager = CookieManager.getInstance().apply {
            removeAllCookies(null)
            flush()
          //  removeSessionCookies(null)

            SystemClock.sleep(1000)
            setAcceptThirdPartyCookies(this@initialize, true)
            acceptCookie()
            array?.forEach { c ->
                setCookie(URL_FACEBOOK, c)
            }
        }
        cookieSyncManager.sync();

        val ccc = cookieManager.getCookie(URL_FACEBOOK)
        Log.d(LOG, "TEST SAVED COOKIES: $ccc")


        webViewClient = object : WebViewClient() {


            override fun onPageFinished(view: WebView?, url: String?) {
                   super.onPageFinished(view, url)

                   val newCookies = url?.let { cookieManager.getCookie(it) }
                   Log.d(LOG, "NEW COOKIES: $newCookies")
                   newCookies?.let { sharedPreferences.updateCookie(action, newCookies) }
               }
           }

       // webViewClient = WebViewClient()

        loadUrl(URL_FACEBOOK)
    }

    private fun SharedPreferences.readCookie(action: Int): String? =
        if (action == ACCOUNT_1) {
            getString(KEY_COOKIE_1, null)
        } else {
            getString(KEY_COOKIE_2, null)
        }

    private fun SharedPreferences.updateCookie(action: Int, cookie: String) {
        if (action == ACCOUNT_1) {
            edit().putString(KEY_COOKIE_1, cookie).apply()
        } else {
            edit().putString(KEY_COOKIE_2, cookie).apply()
        }
    }


}
