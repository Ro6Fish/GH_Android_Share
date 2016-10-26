package me.rokevin.share.weixin;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXMusicObject;
import com.tencent.mm.sdk.platformtools.Util;

import me.rokevin.share.BaseActivity;
import me.rokevin.share.R;
import me.rokevin.share.util.LogUtil;

public class WXActivity extends BaseActivity {

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wx);

        api = WXAPIFactory.createWXAPI(this, "wxd2a8c61e599d0750", true);
        boolean isRegister = api.registerApp("wxd2a8c61e599d0750");
        LogUtil.e(TAG, "isRegister:" + isRegister);

        findViewById(R.id.btn_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareToWX();
            }
        });

        findViewById(R.id.btn_share_circle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareToCircle();
            }
        });

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                api.openWXApp();
            }
        });

        findViewById(R.id.btn_get_user_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    public void shareToWX() {

        WXMusicObject music = new WXMusicObject();
        music.musicUrl = "http://112.74.78.105:8080/HelloWorld/download/voice2.amr";

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = music;
        msg.title = "给你来个笑话";
        msg.description = "苏青上山打老虎";

        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        msg.thumbData = Util.bmpToByteArray(thumb, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("music");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        boolean isSend = api.sendReq(req);
        LogUtil.e(TAG, "isSend:" + isSend);
    }

    public void shareToCircle() {

        WXMusicObject music = new WXMusicObject();
        music.musicUrl = "http://112.74.78.105:8080/HelloWorld/download/voice2.amr";

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = music;
        msg.title = "给你来个笑话";
        msg.description = "上山打老虎";

        // 不添加此项不能分享
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        msg.thumbData = Util.bmpToByteArray(thumb, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("music");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
        boolean isSend = api.sendReq(req);
        LogUtil.e(TAG, "isSend:" + isSend);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
