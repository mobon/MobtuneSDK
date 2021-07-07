package sample.sdk.mobtune;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.mobon.tune.sdk.MobtuneSDK;

/**
 * @author Enliple - MobonSDKActivity 샘플 코드 (자동) - 반드시 앱 메인 액티비티에서 구현을 한다.
 */
public class MainActivity extends FragmentActivity {

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
        //settings.setDefaultTextEncodingName("EUC-KR");
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSupportMultipleWindows(false);

        // 웹뷰 - HTML5 창 속성 추가
        String path = getDir("database", Context.MODE_PRIVATE).getPath();
        settings.setDatabaseEnabled(true);
        settings.setDatabasePath(path);
        settings.setDomStorageEnabled(true);
        settings.setBlockNetworkLoads(false);


        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//            cookieManager.setAcceptThirdPartyCookies(webview, true);
//        }


//        MHSDK.init(this, "trudygirl7").setLog(true);
//
//        MHSDK.handleGooglePlayReferrer(this, new iInstallReferrerCallback() {
//            @Override
//            public void onNeededReferrer() {
//                final InstallReferrerClient referrerClient = InstallReferrerClient.newBuilder(MainActivity.this).build();
//                referrerClient.startConnection(new InstallReferrerStateListener() {
//                    @Override
//                    public void onInstallReferrerSetupFinished(int responseCode) {
//                        switch (responseCode) {
//                            case InstallReferrerClient.InstallReferrerResponse.OK:
//                                // Connection established.
//                                // 구글 플레이 앱과 연결이 성공했을 때, 리퍼러 데이터를 얻기 위한 작업을 수행합니다.
//                                ReferrerDetails response = null;
//                                try {
//                                    response = referrerClient.getInstallReferrer();
//                                    String referrerUrl = response.getInstallReferrer();
//                                    long referrerClickTime = response.getReferrerClickTimestampSeconds();
//                                    long appInstallTime = response.getInstallBeginTimestampSeconds();
//                                    MHSDK.setInstallReferrerData(MainActivity.this, referrerUrl, referrerClickTime, appInstallTime);
//                                } catch (RemoteException e) {
//                                    e.printStackTrace();
//                                } finally {
//                                    referrerClient.endConnection();
//                                }
//
//                                break;
//                            case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
//                                // API not available on the current Play Store app.
//                                break;
//                            case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
//                                // Connection couldn't be established.
//                                break;
//                        }
//                    }
//
//                    @Override
//                    public void onInstallReferrerServiceDisconnected() {
//                        // Try to restart the connection on the next request to
//                        // Google Play by calling the startConnection() method.
//                    }
//                });
//            }
//
//            @Override
//            public void done() {
//            }
//        });


        Button btnNextSub1 = (Button) findViewById(R.id.btnNextSub1);
        btnNextSub1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainActivity2.class));
//                MHSDK.setInstallReferrerData(MainActivity.this, "", 0, 0);
//                MHSDK.getLandingUrl(MainActivity.this, new iMobonMCoverCallback() {
//                    @Override
//                    public void onLoadedAdInfo(boolean result, String adData) {
//                        if (result) {
//                            Intent intent = new Intent(Intent.ACTION_VIEW);
//                            intent.setData(Uri.parse(adData));
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//
//                        } else {
//                            Toast.makeText(MainActivity.this, "no data", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });

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

                newWebView = new WebView(MainActivity.this);
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

        webview.loadUrl(TextUtils.isEmpty(deeplink_url) ? "http://gerbuk.godohosting.com/app_test.html" : deeplink_url);



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
