# GH_Android_Share
###此项目功能说明：用于集成分享、第三方登录，分享主要集成音乐分享，其它文字、图片等分享暂未实现，主要用于集成自己的项目中，以后有时间继续完善，并改善文档

#### 分享
微信、朋友圈、QQ、新浪微博
#### 登录
QQ、新浪微博

###项目配置：
1.在主项目依赖中配置依赖关系示例：

``
compile 'me.rokevin.android.lib:sharesdk:0.0.2'
``

2.注册需要用到的平台

微信:

``
ShareUtil.registeWX(getApplicationContext(), "微信的APP_ID");
``

QQ:

``
ShareUtil.registeWX(getApplicationContext(), "QQ的OPEN_ID");
``

新浪微博：

``
ShareUtil.registeSina(getApplicationContext(), "新浪微博的APP_KEY");
``

可以把注册写在自定义Application的onCreate()方法中

3.AndroidManifest中的配置

微信:

```
<activity
  android:name="包名.wxapi.WXEntryActivity"
  android:configChanges="keyboardHidden|orientation|screenSize"
  android:exported="true"
  android:screenOrientation="portrait"
  android:theme="@android:style/Theme.Translucent.NoTitleBar" />
    
配置过微信的都应该知道回调是在包名下在新建一个wxapi的包，在包中创建一个Activity实现IWXAPIEventHandler接口

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
          LogUtil.e("onReq", "微信：onReq");
          finish();
      }

      @Override
      public void onResp(BaseResp resp) {
          LogUtil.e(WXEntryActivity.class, "微信：onResp xxxx");
          finish();
      }
}
```


QQ:

```
<activity
    android:name="com.tencent.tauth.AuthActivity"
    android:launchMode="singleTask"
    android:noHistory="true">
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        <data android:scheme="tencent1105515096" />
    </intent-filter>
</activity>

<activity
    android:name="com.tencent.connect.common.AssistActivity"
    android:configChanges="orientation|keyboardHidden|screenSize"
    android:theme="@android:style/Theme.Translucent.NoTitleBar" />
    
其中<data android:scheme="tencent1105515096" /> 的tencent后面的数字为申请到的openId
```

新浪微博:

```
<activity
    android:name="me.rokevin.share.sina.SinaActivity" <!--此Activity是新浪微博请求后回调的Activity-->
    android:configChanges="keyboardHidden|orientation"
    android:screenOrientation="portrait">
    <intent-filter>
        <action android:name="com.sina.weibo.sdk.action.ACTION_SDK_REQ_ACTIVITY" />
        <category android:name="android.intent.category.DEFAULT" />
    </intent-filter>
</activity>
```


#####以上配置完就可以使用了，下面是使用示例：

微信：

```
    findViewById(R.id.btn_share).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String title = "这是标题";
            String description = "这是描述";
            String musicUrl = "http://112.74.78.105:8080/HelloWorld/download/voice2.amr";
            int imageId = R.mipmap.ic_launcher;

            ShareUtil.shareToWX(title, description, musicUrl, imageId);
        }
    });

    findViewById(R.id.btn_share_circle).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String title = "这是标题";
            String description = "这是描述";
            String musicUrl = "http://112.74.78.105:8080/HelloWorld/download/voice2.amr";
            int imageId = R.mipmap.ic_launcher;

            ShareUtil.shareToCircle(title, description, musicUrl, imageId);
        }
    });
```

QQ:

```
public class QQActivity extends BaseActivity {

    private String TAG = QQActivity.class.getSimpleName();

    private Button btnShare;
    private Button btnLogin;
    private Button btnLogout;
    private Button btnGetUserInfo;
    private TextView tvToken;
    private TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qq);

        btnShare = (Button) findViewById(R.id.btn_share);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogout = (Button) findViewById(R.id.btn_logout);
        btnGetUserInfo = (Button) findViewById(R.id.btn_get_user_info);
        tvToken = (TextView) findViewById(R.id.tv_token);
        tvInfo = (TextView) findViewById(R.id.tv_info);

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = "这是分享到QQ的标题";
                String summary = "这是分享到QQ的内容";
                String audioUrl = "http://112.74.78.105:8080/HelloWorld/download/voice2.amr";
                String imageUrl = "http://img3.cache.netease.com/photo/0005/2013-03-07/8PBKS8G400BV0005.jpg";
                String targetUrl = "http://www.5ijoke.com";
                String appName = "就是笑话";
                String appSource = "就是笑话" + ShareConfig.QQ_OPEN_ID;

                ShareUtil.shareToQQ(QQActivity.this, title, summary, audioUrl, imageUrl, targetUrl, appName, appSource);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // scope 请求范围 all全部
                ShareUtil.loginQQ(QQActivity.this, "all", null);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareUtil.logoutQQ();
            }
        });

        btnGetUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!ShareUtil.isLoginQQ()) {

                    ShareUtil.loginQQ(QQActivity.this, "get_user_info", null);

                } else {

                    ShareUtil.getUserInfoQQ(new QQLogin.UserInfoQQListener() {
                        @Override
                        public void userInfo(QQUserInfo qqUserInfo) {

                            LogUtil.e(TAG, "获取到的用户信息:" + qqUserInfo.toString());
                            tvInfo.setText(qqUserInfo.toString());
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        ShareUtil.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
```

新浪微博：

 ```
 public class SinaActivity extends BaseActivity implements IWeiboHandler.Response {

    private String TAG = SinaActivity.class.getSimpleName();

    private TextView tvToken;
    private TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sina);

        tvToken = (TextView) findViewById(R.id.tv_token);
        tvInfo = (TextView) findViewById(R.id.tv_info);

        findViewById(R.id.btn_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ShareData shareData = TestData.getShareData();

                String title = shareData.getTitle();
                String description = shareData.getDescription();
                String musicUrl = shareData.getMusicUrl();
                int duration = shareData.getDuration();
                String webUrl = shareData.getWebUrl();
                String defaultText = shareData.getDefaultText();
                int imageId = shareData.getImageId();

                ShareUtil.shareToSina(SinaActivity.this, title, description, musicUrl, duration, webUrl, defaultText, imageId);
            }
        });

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ShareUtil.loginSina(SinaActivity.this);
            }
        });

        findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ShareUtil.logoutSina();
            }
        });

        findViewById(R.id.btn_refresh_token).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ShareUtil.refreshToken();
            }
        });

        /**
         * 获取用户信息
         */
        findViewById(R.id.btn_get_user_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ShareUtil.getUserInfoSina();
            }
        });
    }

    /**
     * @see {@link Activity#onNewIntent}
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        ShareUtil.onNewIntent(intent, this);
    }

    /**
     * 接收微客户端博请求的数据。
     * 当微博客户端唤起当前应用并进行分享时，该方法被调用。
     *
     * @see {@link IWeiboShareAPI#handleWeiboRequest}
     */
    @Override
    public void onResponse(BaseResponse baseResp) {

        if (baseResp != null) {

            switch (baseResp.errCode) {
                case WBConstants.ErrorCode.ERR_OK:
                    Toast.makeText(this, R.string.weibosdk_demo_toast_share_success, Toast.LENGTH_LONG).show();
                    break;
                case WBConstants.ErrorCode.ERR_CANCEL:
                    Toast.makeText(this, R.string.weibosdk_demo_toast_share_canceled, Toast.LENGTH_LONG).show();
                    break;
                case WBConstants.ErrorCode.ERR_FAIL:
                    Toast.makeText(this, getString(R.string.weibosdk_demo_toast_share_failed) + "Error Message: " + baseResp.errMsg, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        ShareUtil.onActivityResult(requestCode, resultCode, data);
    }
}
 ```


