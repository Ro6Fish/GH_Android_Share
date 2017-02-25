package me.rokevin.android.lib.sharesdk.listener;

import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;

/**
 * Created by luokaiwen on 17/2/25.
 * <p>
 * sina weibo 分享回到
 */

public interface IShareSina extends IWeiboHandler.Response {

    @Override
    void onResponse(BaseResponse baseResponse);
}
