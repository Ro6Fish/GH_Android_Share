package me.rokevin.android.lib.sharesdk.businees.wx;

import android.content.Context;
import android.os.AsyncTask;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import me.rokevin.android.lib.sharesdk.businees.wx.model.WXAccessToken;
import me.rokevin.android.lib.sharesdk.businees.wx.model.WXUserInfo;
import me.rokevin.android.lib.sharesdk.util.LogUtil;

/**
 * Created by luokaiwen on 16/10/31.
 */
public class WXLogin {

    private static final String TAG = WXLogin.class.getSimpleName();
    private Context mContext;
    private IWXAPI mIwxapi;

    public WXLogin(Context context, IWXAPI iwxapi) {
        mContext = context;
        mIwxapi = iwxapi;
    }

    /**
     * 判断是否已登录
     *
     * @return token不为空就是登录
     */
    public boolean isLogin() {

        WXAccessToken token = WXTokenKeeper.readAccessToken(mContext);
        return token != null ? true : false;
    }

    /**
     * 判断是否已安装
     *
     * @return
     */
    public boolean isInstall() {

        return mIwxapi.isWXAppInstalled();
    }

    /**
     * 获取授权code
     *
     * @param state 随机码标识是微信回复的消息
     */
    public void getCode(String state) {

        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = state;
        mIwxapi.sendReq(req);
    }

    /**
     * 获取用户Token
     *
     * @param appId
     * @param secret
     * @param code
     */
    public void getAccessToken(String appId, String secret, String code, WXGetTokenListener listener) {

        mWXGetTokenListener = listener;

        getAccessTokenTask.execute(appId, secret, code);
    }

    /**
     * 刷新token
     *
     * @param appId
     * @param refreshToken
     * @param listener
     */
    public void refreshToken(String appId, String refreshToken, WXGetTokenListener listener) {

        mWXGetTokenListener = listener;

        refreshTokenTask.execute(appId, refreshToken);
    }

    /**
     * 获取用户信息
     *
     * @param token
     * @param openId
     */
    public void getUserInfo(String token, String openId, WXGetUserInfoListener listener) {

        mWXGetUserInfoListener = listener;

        getUserInfoTask.execute(token, openId);
    }

    /**
     * 请求token任务
     */
    private AsyncTask getAccessTokenTask = new AsyncTask() {

        @Override
        protected String doInBackground(Object[] params) {

            String appId = (String) params[0];
            String secret = (String) params[1];
            String code = (String) params[2];

            String urlstr = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId + "&secret=" + secret + "&code=" + code + "&grant_type=authorization_code";

            HttpURLConnection connection = null;

            String line = "";
            String totalLine = "";

            try {
                URL url = new URL(urlstr);
                connection = ((HttpURLConnection) url.openConnection());
                InputStream inputStream = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

                while ((line = br.readLine()) != null) {

                    totalLine += line;
                }

                LogUtil.e(TAG, "token totalLine:" + totalLine);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            WXAccessToken token = null;

            try {

                JSONObject jsonObject = new JSONObject(totalLine);

                String accessToken = jsonObject.getString("access_token");
                String expiresIn = jsonObject.getString("expires_in");
                String newRefreshToken = jsonObject.getString("refresh_token");
                String openid = jsonObject.getString("openid");
                String scope = jsonObject.getString("scope");
                String unionid = jsonObject.getString("unionid");

                token = new WXAccessToken();

                token.setOpenid(openid);
                token.setAccessToken(accessToken);
                token.setExpiresIn(expiresIn);
                token.setRefreshToken(newRefreshToken);
                token.setScope(scope);
                token.setUnionid(unionid);

                WXTokenKeeper.writeAccessToken(mContext, token);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (mWXGetTokenListener != null) {
                mWXGetTokenListener.onToken(token);
            }

            return totalLine;
        }
    };

    /**
     * 请求刷新token任务
     */
    private AsyncTask refreshTokenTask = new AsyncTask() {

        @Override
        protected String doInBackground(Object[] params) {

            String appId = (String) params[0];
            String refreshToken = (String) params[1];

            String urlstr = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=" + appId + "&grant_type=refresh_token&refresh_token=" + refreshToken;

            HttpURLConnection connection = null;

            String line = "";
            String totalLine = "";

            try {
                URL url = new URL(urlstr);
                connection = ((HttpURLConnection) url.openConnection());
                InputStream inputStream = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

                while ((line = br.readLine()) != null) {

                    totalLine += line;
                }

                LogUtil.e(TAG, "refresh token totalLine:" + totalLine);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            WXAccessToken token = null;

            try {

                JSONObject jsonObject = new JSONObject(totalLine);

                String accessToken = jsonObject.getString("access_token");
                String expiresIn = jsonObject.getString("expires_in");
                String newRefreshToken = jsonObject.getString("refresh_token");
                String openid = jsonObject.getString("openid");
                String scope = jsonObject.getString("scope");
                String unionid = jsonObject.getString("unionid");

                token = new WXAccessToken();

                token.setOpenid(accessToken);
                token.setExpiresIn(expiresIn);
                token.setRefreshToken(newRefreshToken);
                token.setOpenid(openid);
                token.setScope(scope);
                token.setUnionid(unionid);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (mWXGetTokenListener != null) {
                mWXGetTokenListener.onToken(token);
            }

            return totalLine;
        }
    };

    /**
     * 请求用户信息任务
     */
    private AsyncTask getUserInfoTask = new AsyncTask() {

        @Override
        protected String doInBackground(Object[] params) {

            String token = (String) params[0];
            String openId = (String) params[1];

            String urlstr = "https://api.weixin.qq.com/sns/userinfo?access_token=" + token + "&openid=" + openId;

            HttpURLConnection connection = null;

            String line = "";
            String totalLine = "";

            try {
                URL url = new URL(urlstr);
                connection = ((HttpURLConnection) url.openConnection());
                InputStream inputStream = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

                while ((line = br.readLine()) != null) {

                    totalLine += line;
                }

                LogUtil.e(TAG, "userInfo totalLine:" + totalLine);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            WXUserInfo user = null;

            try {

                JSONObject jsonObject = new JSONObject(totalLine);

                String openid = jsonObject.getString("openid");
                String nickname = jsonObject.getString("nickname");
                String sex = jsonObject.getString("sex");
                String province = jsonObject.getString("province");
                String city = jsonObject.getString("city");
                String country = jsonObject.getString("country");
                String headimgurl = jsonObject.getString("headimgurl");
                String unionid = jsonObject.getString("unionid");

                user = new WXUserInfo();

                user.setOpenid(openid);
                user.setNickname(nickname);
                user.setSex(sex);
                user.setProvince(province);
                user.setCity(city);
                user.setCountry(country);
                user.setHeadimgurl(headimgurl);
                user.setUnionid(unionid);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (mWXGetUserInfoListener != null) {
                mWXGetUserInfoListener.onUserInfo(user);
            }

            return totalLine;
        }
    };

    private WXGetTokenListener mWXGetTokenListener;

    private WXGetUserInfoListener mWXGetUserInfoListener;

    public interface WXGetTokenListener {

        void onToken(WXAccessToken token);
    }

    public interface WXGetUserInfoListener {

        void onUserInfo(WXUserInfo userInfo);
    }
}
