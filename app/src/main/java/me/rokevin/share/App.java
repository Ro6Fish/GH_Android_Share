package me.rokevin.share;

import android.app.Application;

import me.rokevin.android.lib.sharesdk.util.ShareUtil;

/**
 * Created by luokaiwen on 16/10/26.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        
        ShareUtil.registeWX(getApplicationContext(), ShareConfig.WX_APP_ID);
        ShareUtil.registeQQ(getApplicationContext(), ShareConfig.QQ_OPEN_ID);
    }
}
