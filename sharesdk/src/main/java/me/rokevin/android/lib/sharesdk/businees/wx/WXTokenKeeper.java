/*
 * Copyright (C) 2010-2013 The SINA WEIBO Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.rokevin.android.lib.sharesdk.businees.wx;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import me.rokevin.android.lib.sharesdk.businees.wx.model.WXAccessToken;

/**
 * WX Token存储
 */
public class WXTokenKeeper {

    private static final String PREFERENCES_NAME = "share_sdk_wx";

    private static final String KEY_OPEN_ID = "openid";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_EXPIRES_IN = "expires_in";
    private static final String KEY_RECORD_TIME = "record_time";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    private static final String KEY_UNIONID = "unionid";
    private static final String KEY_SCOPE = "scope";

    /**
     * 保存 Token 对象到 SharedPreferences。
     *
     * @param context 应用程序上下文环境
     * @param token   Token 对象
     */
    public static void writeAccessToken(Context context, WXAccessToken token) {
        if (null == context || null == token) {
            return;
        }

        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
        Editor editor = pref.edit();
        editor.putString(KEY_OPEN_ID, token.getOpenid());
        editor.putString(KEY_ACCESS_TOKEN, token.getAccessToken());
        editor.putLong(KEY_EXPIRES_IN, Long.parseLong(token.getExpiresIn()));
        editor.putLong(KEY_RECORD_TIME, System.currentTimeMillis() / 1000);
        editor.putString(KEY_REFRESH_TOKEN, token.getRefreshToken());
        editor.putString(KEY_UNIONID, token.getUnionid());
        editor.putString(KEY_SCOPE, token.getScope());
        editor.commit();
    }

    /**
     * 从 SharedPreferences 读取 Token 信息。
     *
     * @param context 应用程序上下文环境
     * @return 返回 Token 对象
     */
    public static WXAccessToken readAccessToken(Context context) {

        if (null == context) {
            return null;
        }

        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
        String openId = pref.getString(KEY_OPEN_ID, "");

        if (TextUtils.isEmpty(openId)) {
            return null;
        }

        WXAccessToken token = new WXAccessToken();
        token.setOpenid(openId);
        token.setAccessToken(pref.getString(KEY_ACCESS_TOKEN, ""));
        token.setExpiresIn(pref.getLong(KEY_EXPIRES_IN, 0) + "");
        token.setRecordTime(pref.getLong(KEY_RECORD_TIME, 0) + "");
        token.setRefreshToken(pref.getString(KEY_REFRESH_TOKEN, ""));
        token.setUnionid(pref.getString(KEY_UNIONID, ""));
        token.setScope(pref.getString(KEY_SCOPE, ""));

        return token;
    }

    /**
     * 清空 SharedPreferences 中 Token信息。
     *
     * @param context 应用程序上下文环境
     */
    public static void clear(Context context) {
        if (null == context) {
            return;
        }

        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
        Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
}
