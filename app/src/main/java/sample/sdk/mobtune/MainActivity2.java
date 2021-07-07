package sample.sdk.mobtune;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.mobon.tune.sdk.MobtuneSDK;

public class MainActivity2 extends FragmentActivity {

    private WebView webview;
    private WebView newWebView;

    @Override
    public void supportFinishAfterTransition() {
        super.supportFinishAfterTransition();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String deeplink_url = "";

        Intent deepLinkIntent = getIntent();
        if (deepLinkIntent != null) {
            deeplink_url = deepLinkIntent.getStringExtra("sdk_deeplink");
        }

        webview = findViewById(R.id.webview);
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSupportMultipleWindows(false);

        // 웹뷰 - HTML5 창 속성 추가
        String path = getDir("database", Context.MODE_PRIVATE).getPath();
        settings.setDatabaseEnabled(true);
        settings.setDatabasePath(path);
        settings.setDomStorageEnabled(true);
        settings.setBlockNetworkLoads(false);

        MobtuneSDK.setWebView(webview);

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);

        Button btnNextSub1 = (Button) findViewById(R.id.btnNextSub1);
        btnNextSub1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MobtuneSDK.setInstallReferrerData(MainActivity2.this, "", 0, 0);
//                MHSDK.getLandingUrl(MainActivity.this, new iMobonMCoverCallback() {

            }
        });

        Button btnNextSub2 = (Button) findViewById(R.id.btnNextSub2);
        btnNextSub2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                webview.loadUrl("https://www.mediacategory.com/test/cookie.jsp");

            }
        });

        Button btnNextSub3 = (Button) findViewById(R.id.btnNextSub3);
        btnNextSub3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // shownoti();
                webview.reload();


            }
        });

        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setSupportMultipleWindows(true);


        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {

                newWebView = new WebView(MainActivity2.this);
                view.addView(newWebView);
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(newWebView);
                resultMsg.sendToTarget();

                newWebView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                        browserIntent.setData(Uri.parse(url));
                        startActivity(browserIntent);
                        return true;
                    }
                });
                return true;

            }
        });

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // .... 내용 처리....
                System.out.println("!!!!!!!!!!!!!!!!!!!!!  " + url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

//        webview.loadUrl(TextUtils.isEmpty(deeplink_url) ? "http://gerbuk.godohosting.com/app_test.html" : deeplink_url);


        webview.loadUrl("http://m.avalon.co.kr/check.html");





    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {


        if (newWebView != null) {
            if (newWebView.canGoBack())
                newWebView.goBack();
            else {
                webview.removeView(newWebView);
                newWebView.destroy();
                newWebView = null;
            }
            return;
        }

        if (webview.canGoBack()) {
            webview.goBack();
            return;
        }

        super.onBackPressed();
    }

    private void shownoti() {
        MobtuneSDK.get(this).sendNotification(this);
    }
}
