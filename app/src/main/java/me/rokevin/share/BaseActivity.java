package me.rokevin.share;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import butterknife.ButterKnife;

/**
 * Created by luokaiwen on 16/10/26.
 */
public class BaseActivity extends Activity {

    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        ButterKnife.bind(this);
    }
}
