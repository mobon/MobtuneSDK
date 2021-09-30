# Mobtune Android SDK

Mobtune SDK 를 이용하여 통해 고객 흐름 파악 및 이용자 분석, 측정 데이터 제공 합니다.

# Mobtune Android SDK Release History
 |version|Description|
|---|:---:|
|0.9.1|결제 이벤트 추가|
|0.8.9|deeplink 이벤트 추가|
|0.8.0|최초 라이브러리 버전|

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
        mavenCentral() //add
    }
}
```

- app build.gradle 에 MobtuneSDK, Installreferrer 라이브러리를 추가합니다.
```XML
dependencies {
  implementation fileTree(dir: 'libs', include: ['*.jar'])
  implementation 'com.android.installreferrer:installreferrer:1.1' // add library
  implementation 'io.github.mobon:MobtuneSDK:0.8.9' // add library
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
            registerActivityLifecycleCallbacks(new MobtuneLifecycleCallbacks(this));
        }
    }
}

```

## 3.Hybrid App
 - Hybrid 앱의 경우 생성한 WebView 객체를 SDK 에 넘겨주셔야 더욱 정확한 사용자 추적을 할 수 있습니다.
 
 ```java
 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webview = findViewById(R.id.webview);
        MobtuneSDK.setWebView(webview); // add code
```

## 4.사용자 이벤트 

- 결제 이벤트
   사용자의 결제 이벤트를 추적합니다.
   아래 코드는 필수값들이니 빠짐없이 채워주시기 바랍니다.
  
 ```java
  ProductItem product_1 = new ProductItem().setProductId("test_product_1").setProductName("뽑기 셋트").setPrice(1000000).setQuantity(5);
  ProductItem product_2 = new ProductItem().setProductId("test_product_2").setProductName("구슬치기").setPrice(55000).setQuantity(1);

                ProductInfo product = new ProductInfo()
                        .setPaymentMethod(MobtuneParameter.PaymentType.CREDITCARD) //결제방법
                        .setTotalPrice(5550000) // 총 합계금액
                        .setProductItems(product_1,product_2) // 생성한 상품 데이터를(ProductItem) 넣어주세요.
                        .setOrderId("test_orderId") //order ID
                        .build();

                EventParam param = new EventParam()
                        .setAttribute(MobtuneParameter.PRODUCTS, product);
                MobtuneSDK.setEvent(MobtuneEvent.ORDER, param);
```
