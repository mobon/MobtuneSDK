package sample.sdk.mobtune;

import android.app.Application;
import android.os.Build;

import com.mobon.tune.sdk.MobtuneSDK;
import com.mobon.tune.sdk.MobonLifecycleCallbacks;

public class GlobalApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();

        MobtuneSDK.init(this,"testSDK");
        if (Build.VERSION.SDK_INT >= 14) {
            registerActivityLifecycleCallbacks(new MobonLifecycleCallbacks(this));
        }
    }
}
