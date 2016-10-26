package me.rokevin.share;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by luokaiwen on 16/10/26.
 */
public class BaseActivity extends Activity {

    protected String TAG = BaseActivity.class.getSimpleName();
    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }
}
