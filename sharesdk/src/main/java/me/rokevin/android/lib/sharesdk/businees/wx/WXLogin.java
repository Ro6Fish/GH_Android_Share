package me.rokevin.android.lib.sharesdk.businees.wx;

import android.content.Context;

import com.tencent.mm.sdk.openapi.IWXAPI;

/**
 * Created by luokaiwen on 16/10/31.
 */
public class WXLogin {

    private Context mContext;
    private IWXAPI mIwxapi;

    public WXLogin(Context context, IWXAPI iwxapi) {
        mContext = context;
        mIwxapi = iwxapi;
    }
}
