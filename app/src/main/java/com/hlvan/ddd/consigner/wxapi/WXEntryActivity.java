package com.hlvan.ddd.consigner.wxapi;


import android.os.Bundle;

import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.ConstantsAPI;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendAuth;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import de.greenrobot.event.EventBus;
import me.rokevin.android.lib.sharesdk.util.LogUtil;
import me.rokevin.eventbus.WXCodeEvent;
import me.rokevin.share.BaseActivity;
import me.rokevin.share.ShareConfig;

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

        switch (type) {

            case ConstantsAPI.COMMAND_SENDAUTH: // 登录

                String code = ((SendAuth.Resp) resp).token;
                EventBus.getDefault().post(new WXCodeEvent(code));
                break;

            case ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX: // 分享

                break;
        }

        LogUtil.e(WXEntryActivity.class, "微信：onResp xxxx type:" + type);
        finish();
    }
}