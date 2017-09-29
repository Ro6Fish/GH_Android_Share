package com.lzqz.idouxiao.wxapi;


import android.os.Bundle;

import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import me.rokevin.share.BaseActivity;
import me.rokevin.share.ShareConfig;
import me.rokevin.android.lib.sharesdk.util.LogUtil;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IWXAPI api = WXAPIFactory.createWXAPI(this, ShareConfig.WX_APP_ID, true);
        api.handleIntent(getIntent(), this);

        LogUtil.e(WXEntryActivity.class, "进入微信回调页面");
    }

    @Override
    public void onReq(BaseReq reg) {

        int type = reg.getType();

        LogUtil.e("onReq", "微信：onReq type:" + type);
        finish();
    }

    @Override
    public void onResp(BaseResp resp) {

        int type = resp.getType();

        LogUtil.e(WXEntryActivity.class, "微信：onResp xxxx type:" + type);
        finish();
    }
}