package me.rokevin.share.test;

import me.rokevin.share.R;

/**
 * Created by luokaiwen on 16/11/2.
 */
public class TestData {

    public static ShareData getShareData() {

        ShareData shareData = new ShareData();

        shareData.setTitle("这是分享标题");
        shareData.setDescription("这是分享内容");
        shareData.setMusicUrl("http://112.74.78.105:8080/HelloWorld/download/voice2.amr");
        shareData.setDuration(30);
        shareData.setWebUrl("http://www.5ijoke.com");
        shareData.setDefaultText("应用名称");
        shareData.setImageId(R.mipmap.ic_launcher);

        return shareData;
    }
}
