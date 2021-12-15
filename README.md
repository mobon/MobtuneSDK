# Mobtune Android SDK

Mobtune SDK 를 이용하여 통해 고객 흐름 파악 및 이용자 분석, 측정 데이터 제공 합니다.

# Mobtune Android SDK Release History
 |version|Description|
|---|:---:|
|0.9.11|backup data 예외처리|
|0.9.8|결제 이벤트 추가,android ID 수집 추가|
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
  implementation 'com.android.installreferrer:installreferrer:2.2' // add library
  implementation 'io.github.mobon:MobtuneSDK:0.9.11' // add library
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
  - 사용자의 결제 이벤트를 추적합니다.
  - 아래 코드는 필수값들이니 빠짐없이 채워주시기 바랍니다.
  
 ```java
  ProductItem productItem1 = new ProductItem()
                          .setProductId("test_product_1") // 상품 아이디
                          .setProductName("뽑기 셋트") //상품명
                          .setPrice("1000000") //상품 가격
                          .setQuantity("5"); //상품 개수
                          
  ProductItem productItem2 = new ProductItem()
                          .setProductId("test_product_2")
                          .setProductName("구슬치기")
                          .setPrice("55000")
                          .setQuantity("1");

  ProductInfo product = new ProductInfo()
                        .setPaymentMethod(MobtuneParameter.PaymentType.CREDITCARD) //결제방법
                        .setTotalPrice("5550000") // 총 합계금액
                        .setProductItems(productItem1,productItem2) // 생성한 상품 데이터를(ProductItem) 넣어주세요.
                        .setOrderId("test_orderId") //order ID
                        .build();

  MobtuneSDK.setEvent(MobtuneEvent.ORDER, new EventParam().setAttribute(MobtuneParameter.PRODUCTS, product));
                
```

## 5. 그 외 설정
- 화면 수집 제외 
  - 수집하지 않을 activity 를 설정하시면 수집에서 제외처리합니다.

 ```java
 MobtuneSDK.setIgnoreActivity("MainActivity");
 MobtuneSDK.setIgnoreActivity("com.sample.MainActivity"); // 같은 이름의 activity 가 있을 경우 패키지명까지 명시...
 ```
 
 
 - android Id 전송 관련
  - 구글 개인정보 수집 방침에 따라 앱에서 사용자에게 개인정보 수집 동의를 받은 후 아래의 함수를 실행해주시기 바랍니다.

 ```java
 MobtuneSDK.sendAndroidId();
 ```
