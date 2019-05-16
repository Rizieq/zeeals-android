package com.example.user.zeeals;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.user.zeeals.util.CurvedBottomNavigationView;

public class registrasiScreen extends AppCompatActivity {

    WebView webView;
    WebSettings webSettings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_group_2);
        CurvedBottomNavigationView curvedBottomNavigationView = findViewById(R.id.customBottomBar);
        curvedBottomNavigationView.inflateMenu(R.menu.bottom_nav);

//        setContentView(R.layout.activity_registrasi_screen);
//        webView = findViewById(R.id.registrasi_webview);
//        webView.setWebViewClient(new WebViewClient());
//        webSettings = webView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setDomStorageEnabled(true);
//        webView.setWebChromeClient(new WebChromeClient());
//        webView.loadUrl("https://app.zeeals.com/#/register");



    }
}


