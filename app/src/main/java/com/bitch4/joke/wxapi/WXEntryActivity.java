package com.bitch4.joke.wxapi;


import android.os.Bundle;

import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import me.rokevin.share.BaseActivity;
import me.rokevin.share.util.LogUtil;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IWXAPI api = WXAPIFactory.createWXAPI(this, "wxd2a8c61e599d0750", true);
        api.handleIntent(getIntent(), this);

        LogUtil.e(WXEntryActivity.class, "进入微信回调页面");
    }

    @Override
    public void onReq(BaseReq reg) {
        LogUtil.e("onReq", "微信：onReq");
        finish();
    }

    @Override
    public void onResp(BaseResp resp) {
        LogUtil.e(WXEntryActivity.class, "微信：onResp xxxx");
        finish();
    }
}