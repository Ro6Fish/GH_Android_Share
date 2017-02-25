package me.rokevin.android.lib.sharesdk.listener;

import com.tencent.tauth.UiError;

/**
 * Created by luokaiwen on 17/2/25.
 */

public interface IShare {

    void onSucc(Object o);

    void onError(UiError uiError);

    void onCancel();
}
