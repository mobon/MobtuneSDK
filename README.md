# Mobtune Android SDK

Mobtune SDK 를 이용하여 통해 고객 흐름 파악 및 이용자 분석, 측정 데이터 제공 합니다.

# Mobtune Android SDK Release History
 |version|Description|
|---|:---:|
|0.9.12|backup data 예외처리|
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
  implementation 'io.github.mobon:MobtuneSDK:0.9.12' // add library
}
```

```

** Android 버전에 따른 추가설정

- targetSdkVersion 28 부터 네트워크 통신 시 암호화 되지 않은 HTTP통신이 차단되도록 기본설정이
변경되었습니다. SDK 내 모든 미디에이션 광고가 정상동작하기 위해서는 HTTP 통신을 허용해주셔야 하며, 방법은 아래와 같습니다.

AndroidManifest.xml 파일에서 application 항목의 속성값으로
usesCleartextTraffic을 true로 설정해야 합니다.
(Android 9 부터 해당값이 default로 false 설정되어 HTTP 통신이 제한됩니다.)

```
<manifest ...>
<application
...
android:usesCleartextTraffic="true"
...>
</application>
</manifest>
```
- targetSdkVersion 31(Android 12)로 업데이트하는 앱은 다음과 같이 AndroidManifest.xml 에서 Google Play 서비스 일반 권한을 선언해야 합니다.
 
```
<uses-permission android:name="com.google.android.gms.permission.AD_ID"/>
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

- 결제 취소 이벤트
  - 사용자의 결제 취소 이벤트를 추적합니다.
  - 아래 코드는 필수값들이니 빠짐없이 채워주시기 바랍니다.
  
 ```java
  ProductItem productItem1 = new ProductItem()
                          .setProductId("test_product_1") // 상품 아이디
                          .setProductName("뽑기 셋트") //상품명
                          .setPrice("1000000") //상품 가격
                          .setQuantity("5"); //상품 개수

  ProductInfo product = new ProductInfo()
                        .setPaymentMethod(MobtuneParameter.PaymentType.CREDITCARD) //결제방법
                        .setTotalPrice("5550000") // 총 합계금액
                        .setProductItems(productItem1) // 생성한 상품 데이터를(ProductItem) 넣어주세요.
                        .setOrderId("test_orderId") //order ID
                        .build();

  MobtuneSDK.setEvent(MobtuneEvent.ORDER_CANCLE, new EventParam().setAttribute(MobtuneParameter.PRODUCTS, product));
                
```

- 장바구니 담기 이벤트
  - 사용자의 장바구니 이벤트를 추적합니다.
  - 아래 코드는 필수값들이니 빠짐없이 채워주시기 바랍니다.
  
 ```java
  ProductItem productItem1 = new ProductItem()
                          .setProductId("test_product_1") // 상품 아이디
                          .setProductName("뽑기 셋트") //상품명
                          .setPrice("1000000") //상품 가격
                          .setQuantity("5") //상품 개수
                          .setOption("option"); //상품 옵션 [필수 아님]

  CartInfo cart = new CartInfo()                        
                        .setProductItems(productItem1) // 생성한 상품 데이터를(ProductItem) 넣어주세요.
                        .setCartId("test_cartId") // 장바구니 ID
                        .build();

  MobtuneSDK.setEvent(MobtuneEvent.ADD_CART, new EventParam().setAttribute(MobtuneParameter.PRODUCTS, cart));
                
```

- 회원가입 이벤트
  - 사용자의 회원가입 이벤트를 추적합니다.
  
 ```java

  SignUpInfo siginUpInfo = new SignUpInfo()                        
                        .setUserId("user_id") // 유저 ID    
                        .setName("홍길동") // 유저 이름 
                        .setNickName("의적") // 유저 닉네임    
                        .setGender("M") // 유저 성별 (M[남],W[여])
                        .setBirthDay("20001010") // 유저 생년월일(8자리)     
                        .setPhoneNumber("01044556677") // 핸드폰번호 
                        .setEmail("test@test.com") // 유저 이메일    
                        .setAddress("서울시 여의도구 여의도동 123-12") // 유저 주소    
                        .setMarryYn("N") // 결혼여부(Y,N)    
                        .build();

  MobtuneSDK.setEvent(MobtuneEvent.SIGN_UP, new EventParam().setAttribute(MobtuneParameter.PRODUCTS, siginUpInfo));
                
```

- 회원수정 이벤트
  - 사용자의 회원수정 이벤트를 추적합니다.
  
 ```java

  SignUpInfo siginUpInfo = new SignUpInfo()                        
                        .setUserId("user_id") // 유저 ID    
                        .setName("홍길동") // 유저 이름 
                        .setNickName("의적") // 유저 닉네임    
                        .setGender("M") // 유저 성별 (M[남],W[여])
                        .setBirthDay("20001010") // 유저 생년월일(8자리)     
                        .setPhoneNumber("01044556677") // 핸드폰번호 
                        .setEmail("test@test.com") // 유저 이메일    
                        .setAddress("서울시 여의도구 여의도동 123-12") // 유저 주소    
                        .setMarryYn("N") // 결혼여부(Y,N)    
                        .build();

  MobtuneSDK.setEvent(MobtuneEvent.USER_MODIFY, new EventParam().setAttribute(MobtuneParameter.PRODUCTS, siginUpInfo));
                
```


- 회원탈퇴 이벤트
  - 사용자의 회원탈퇴 이벤트를 추적합니다.
  
 ```java

  UserInfo userInfo = new UserInfo()                        
                        .setUserId("user_id") // 유저 ID 
                        .build();

  MobtuneSDK.setEvent(MobtuneEvent.SIGN_OUT, new EventParam().setAttribute(MobtuneParameter.PRODUCTS, userInfo));
                
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
