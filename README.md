# Mobtune Android SDK

Mobtune SDK 를 이용하여 통해 고객 흐름 파악 및 이용자 분석, 측정 데이터 제공 합니다.

# Mobtune Android SDK Release History
 |version|Description|
|---|:---:|
|0.9.0|최초 라이브러리 버전|

## 개발환경
- 최소 SDK Version : Android 14
- Compile SDK : Android 28 이상
- Build Tool : Android Studio 
- androidX 권장

## 1. Mobtune SDK 기본설정

- project build.gradle 에 mavenCentral() 을 추가합니다.

```XML
allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
}
```

- app build.gradle 에 mobonSDK 라이브러리를 추가합니다.
```XML
dependencies {
  implementation fileTree(dir: 'libs', include: ['*.jar'])
  implementation 'com.android.installreferrer:installreferrer:1.1' // add library
  implementation 'io.github.mobon:mobtuneSDK:0.9.9' // add library
}
```


## 2. Mobtune SDK 선언
 - Application 에 sdk 초기화와 ActivityLifecycleCallback을 등록합니다.

```java
public class MyApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();

        MobtuneSDK.init(this,"YOUR_APP_KEY"); // 등록한 app key를 넣어주세요.
        if (Build.VERSION.SDK_INT >= 14) {
            registerActivityLifecycleCallbacks(new MobtuneLifecycleCallbacks());
        }
    }
}

```

## 3.Hybrid 
 - Hybrid 앱의 경우 생성한 WebView 객체를 SDK 에 넘겨주셔야 더욱 정확한 사용자 추적을 할 수 있습니다.
 
 ```java
 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webview = findViewById(R.id.webview);
        MobtuneSDK.setWebView(webview); // hybrid 앱에서 setWebview() 호출은 필수 입니다.
```
