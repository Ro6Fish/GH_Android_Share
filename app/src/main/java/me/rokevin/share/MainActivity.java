package me.rokevin.share;

import android.content.Intent;
import android.os.Bundle;

import butterknife.OnClick;
import me.rokevin.share.douban.DouBanActivity;
import me.rokevin.share.qq.QQActivity;
import me.rokevin.share.sina.SinaActivity;
import me.rokevin.share.weixin.WXActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @OnClick(R.id.btn_qq)
    void doQQ() {
        startActivity(new Intent(mContext, QQActivity.class));
    }

    @OnClick(R.id.btn_weixin)
    void doWX() {
        startActivity(new Intent(mContext, WXActivity.class));
    }

    @OnClick(R.id.btn_sina)
    void doSina() {
        startActivity(new Intent(mContext, SinaActivity.class));
    }

    @OnClick(R.id.btn_douban)
    void doDouban() {
        startActivity(new Intent(mContext, DouBanActivity.class));
    }
}
