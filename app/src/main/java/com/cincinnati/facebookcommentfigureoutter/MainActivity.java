package com.cincinnati.facebookcommentfigureoutter;

import android.content.Context;
import android.os.Build;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MainActivity extends ActionBarActivity {

    private WebView childView;
    private Context mContext = this;
    private LinearLayout containerLayout;
    private WebView webView;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        containerLayout = (LinearLayout) findViewById(R.id.container_layout);
        editText = (EditText) findViewById(R.id.edit_text);
        Button button = (Button) findViewById(R.id.button);
        webView = (WebView) findViewById(R.id.web_view);

        if (Build.VERSION.SDK_INT >= 19) {
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        }

        containerLayout.setBackgroundColor(getResources().getColor(android.R.color.background_dark));

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDisplayZoomControls(true);
        webView.getSettings().setBuiltInZoomControls(true);

        CookieManager.getInstance().setAcceptCookie(true);

        webView.setLayoutParams(getLayoutParams());

        webView.setWebChromeClient(new DollyChromeClient());

        // i use a much more complicated client here in Dolly but
        // i didn't think it was relevant to what you're doing so left it out
        // if you want to see it in here just let me know
        webView.setWebViewClient(new WebClient());

    }

    public void onButtonClick(View view) {
        String url = editText.getText().toString();
        webView.loadUrl(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private LinearLayout.LayoutParams getLayoutParams(){
        return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    final class DollyChromeClient extends WebChromeClient {
        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            Log.d("snw", "resultMsg: " + resultMsg.toString());
            childView = new WebView(mContext);

            childView.getSettings().setJavaScriptEnabled(true);
            childView.getSettings().setAppCacheEnabled(true);
            childView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            childView.getSettings().setDomStorageEnabled(true);
            childView.getSettings().setSupportMultipleWindows(true);
            childView.getSettings().setSupportZoom(true);
            childView.getSettings().setDisplayZoomControls(true);
            childView.getSettings().setBuiltInZoomControls(true);

            CookieManager.getInstance().setAcceptCookie(true);

            childView.setWebViewClient(new FaceBookClient());
            childView.setWebChromeClient(this);
            childView.setLayoutParams(getLayoutParams());

            containerLayout.addView(childView);
            childView.requestFocus();
            webView.setVisibility(View.GONE);

            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            Log.d("snw", "transport: " + transport.toString());
            transport.setWebView(childView);
            resultMsg.sendToTarget();
            return true;
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage cm) {
            Log.d("WebViewDebug", cm.message() + " -- From line "
                    + cm.lineNumber() + " of "
                    + cm.sourceId());
            return true;
        }

        @Override
        public void onCloseWindow(WebView window) {
            containerLayout.removeViewAt(containerLayout.getChildCount() -1);
            childView = null;
            webView.setVisibility(View.VISIBLE);
            webView.requestFocus();
        }
    }

    private class FaceBookClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i("REQUEST URL", url);
            return false;
        }
    }

    private class WebClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {

            // TODO Auto-generated method stub

            view.loadUrl(url);
            return true;
        }
    }
}
