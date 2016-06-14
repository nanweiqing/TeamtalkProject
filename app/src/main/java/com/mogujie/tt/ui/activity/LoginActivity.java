package com.mogujie.tt.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.mogujie.tt.DB.sp.LoginSp;
import com.mogujie.tt.DB.sp.SystemConfigSp;
import com.mogujie.tt.R;
import com.mogujie.tt.app.IMApplication;
import com.mogujie.tt.config.IntentConstant;
import com.mogujie.tt.config.SysConstant;
import com.mogujie.tt.config.UrlConstant;
import com.mogujie.tt.net.request.GetFriendListReqBody;
import com.mogujie.tt.net.request.MsgHeader;
import com.mogujie.tt.net.response.FriendDetailInfo;
import com.mogujie.tt.net.response.GetFriendListResp;
import com.mogujie.tt.net.response.GetFriendListRespBody;
import com.mogujie.tt.utils.EncryUtil;
import com.mogujie.tt.utils.IMUIHelper;
import com.mogujie.tt.imservice.event.LoginEvent;
import com.mogujie.tt.imservice.event.SocketEvent;
import com.mogujie.tt.imservice.manager.IMLoginManager;
import com.mogujie.tt.imservice.service.IMService;
import com.mogujie.tt.ui.base.TTBaseActivity;
import com.mogujie.tt.imservice.support.IMServiceConnector;
import com.mogujie.tt.utils.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.UnsupportedEncodingException;
import java.util.List;

import de.greenrobot.event.EventBus;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Request;


/**
 * @YM 1. 链接成功之后，直接判断是否loginSp是否可以直接登陆
 * true: 1.可以登陆，从DB中获取历史的状态
 * 2.建立长连接，请求最新的数据状态 【网络断开没有这个状态】
 * 3.完成
 * <p/>
 * false:1. 不能直接登陆，跳转到登陆页面
 * 2. 请求消息服务器地址，链接，验证，触发loginSuccess
 * 3. 保存登陆状态
 */
public class LoginActivity extends TTBaseActivity {

    private Logger logger = Logger.getLogger(LoginActivity.class);
    private Handler uiHandler = new Handler();
    private EditText mNameView;
    private EditText mPasswordView;
    private View loginPage;
    private View splashPage;
    private View mLoginStatusView;
    private TextView mSwitchLoginServer;
    private InputMethodManager intputManager;


    private IMService imService;
    private boolean autoLogin = true;
    private boolean loginSuccess = false;

    private IMServiceConnector imServiceConnector = new IMServiceConnector() {
        @Override
        public void onServiceDisconnected() {
        }

        @Override
        public void onIMServiceConnected() {
            logger.d("login#onIMServiceConnected");
            imService = imServiceConnector.getIMService();
            try {
                do {
                    if (imService == null) {
                        //后台服务启动链接失败
                        break;
                    }
                    IMLoginManager loginManager = imService.getLoginManager();
                    LoginSp loginSp = imService.getLoginSp();
                    if (loginManager == null || loginSp == null) {
                        // 无法获取登陆控制器
                        break;
                    }

                    LoginSp.SpLoginIdentity loginIdentity = loginSp.getLoginIdentity();
                    if (loginIdentity == null) {
                        // 之前没有保存任何登陆相关的，跳转到登陆页面
                        break;
                    }

                    mNameView.setText(loginIdentity.getLoginName());
                    if (TextUtils.isEmpty(loginIdentity.getPwd())) {
                        // 密码为空，可能是loginOut
                        break;
                    }
                    mPasswordView.setText(loginIdentity.getPwd());

                    if (autoLogin == false) {
                        break;
                    }

                    handleGotLoginIdentity(loginIdentity);
                    return;
                } while (false);

                // 异常分支都会执行这个
                handleNoLoginIdentity();
            } catch (Exception e) {
                // 任何未知的异常
                logger.w("loadIdentity failed");
                handleNoLoginIdentity();
            }
        }
    };


    /**
     * 跳转到登陆的页面
     */
    private void handleNoLoginIdentity() {
        logger.i("login#handleNoLoginIdentity");
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showLoginPage();
            }
        }, 1000);
    }

    /**
     * 自动登陆
     */
    private void handleGotLoginIdentity(final LoginSp.SpLoginIdentity loginIdentity) {
        logger.i("login#handleGotLoginIdentity");

        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                logger.d("login#start auto login");
                if (imService == null || imService.getLoginManager() == null) {
                    Toast.makeText(LoginActivity.this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                    showLoginPage();
                }
                imService.getLoginManager().login(loginIdentity);
            }
        }, 500);
    }


    private void showLoginPage() {
        splashPage.setVisibility(View.GONE);
        loginPage.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        intputManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        logger.d("login#onCreate");

        SystemConfigSp.instance().init(getApplicationContext());
        if (TextUtils.isEmpty(SystemConfigSp.instance().getStrConfig(SystemConfigSp.SysCfgDimension.LOGINSERVER))) {
            SystemConfigSp.instance().setStrConfig(SystemConfigSp.SysCfgDimension.LOGINSERVER, UrlConstant.ACCESS_MSG_ADDRESS);
        }

        imServiceConnector.connect(LoginActivity.this);
        EventBus.getDefault().register(this);

        setContentView(R.layout.tt_activity_login);
        mSwitchLoginServer = (TextView)findViewById(R.id.sign_switch_login_server);
        mSwitchLoginServer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(LoginActivity.this, android.R.style.Theme_Holo_Light_Dialog));
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialog_view = inflater.inflate(R.layout.tt_custom_dialog, null);
                final EditText editText = (EditText)dialog_view.findViewById(R.id.dialog_edit_content);
                editText.setText(SystemConfigSp.instance().getStrConfig(SystemConfigSp.SysCfgDimension.LOGINSERVER));
                TextView textText = (TextView)dialog_view.findViewById(R.id.dialog_title);
                textText.setText(R.string.switch_login_server_title);
                builder.setView(dialog_view);
                builder.setPositiveButton(getString(R.string.tt_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(!TextUtils.isEmpty(editText.getText().toString().trim()))
                        {
                            SystemConfigSp.instance().setStrConfig(SystemConfigSp.SysCfgDimension.LOGINSERVER,editText.getText().toString().trim());
                            dialog.dismiss();
                        }
                    }
                });
                builder.setNegativeButton(getString(R.string.tt_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });

        mNameView = (EditText) findViewById(R.id.name);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {

                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        mLoginStatusView = findViewById(R.id.login_status);
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intputManager.hideSoftInputFromWindow(mPasswordView.getWindowToken(), 0);
                attemptLogin();

//                getTest();



                String jsonTest = "{\"msgHeader\":{\"serviceCode\":\"IM003\",\"version\":\"1.0\",\"sysPlatCode\":\"1\",\"sentTime\":\"2016-05-29 09:54:02\",\"expTime\":\"\",\"sMessageNo\":\"BBS000201605290954020214\"},\"msgBody\":{\"userId\":\"2\",\"countNum\":\"4\",\"startNum\":\"1\",\"endNum\":\"49\",\"Datas\":[{\"refId\":\"2\",\"fUserId\":\"3\",\"fUserName\":\"w\",\"fNickName\":\"W\",\"fSignature\":\"null\",\"fUserIcon\":\"null\",\"fIntro\":\"null\",\"fCreateTime\":\"null\"},{\"refId\":\"3\",\"fUserId\":\"4\",\"fUserName\":\"e\",\"fNickName\":\"E\",\"fSignature\":\"null\",\"fUserIcon\":\"null\",\"fIntro\":\"null\",\"fCreateTime\":\"null\"},{\"refId\":\"4\",\"fUserId\":\"5\",\"fUserName\":\"r\",\"fNickName\":\"R\",\"fSignature\":\"null\",\"fUserIcon\":\"null\",\"fIntro\":\"null\",\"fCreateTime\":\"null\"},{\"refId\":\"5\",\"fUserId\":\"6\",\"fUserName\":\"t\",\"fNickName\":\"T\",\"fSignature\":\"null\",\"fUserIcon\":\"null\",\"fIntro\":\"null\",\"fCreateTime\":\"null\"},{\"refId\":\"6\",\"fUserId\":\"7\",\"fUserName\":\"y\",\"fNickName\":\"Y\",\"fSignature\":\"null\",\"fUserIcon\":\"null\",\"fIntro\":\"null\",\"fCreateTime\":\"null\"},{\"refId\":\"7\",\"fUserId\":\"8\",\"fUserName\":\"u\",\"fNickName\":\"U\",\"fSignature\":\"null\",\"fUserIcon\":\"null\",\"fIntro\":\"null\",\"fCreateTime\":\"null\"},{\"refId\":\"8\",\"fUserId\":\"9\",\"fUserName\":\"i\",\"fNickName\":\"I\",\"fSignature\":\"null\",\"fUserIcon\":\"null\",\"fIntro\":\"null\",\"fCreateTime\":\"null\"},{\"refId\":\"9\",\"fUserId\":\"10\",\"fUserName\":\"o\",\"fNickName\":\"O\",\"fSignature\":\"null\",\"fUserIcon\":\"null\",\"fIntro\":\"null\",\"fCreateTime\":\"null\"},{\"refId\":\"10\",\"fUserId\":\"11\",\"fUserName\":\"p\",\"fNickName\":\"P\",\"fSignature\":\"null\",\"fUserIcon\":\"null\",\"fIntro\":\"null\",\"fCreateTime\":\"null\"},{\"refId\":\"11\",\"fUserId\":\"12\",\"fUserName\":\"l\",\"fNickName\":\"L\",\"fSignature\":\"null\",\"fUserIcon\":\"null\",\"fIntro\":\"null\",\"fCreateTime\":\"null\"},{\"refId\":\"12\",\"fUserId\":\"13\",\"fUserName\":\"k\",\"fNickName\":\"K\",\"fSignature\":\"null\",\"fUserIcon\":\"null\",\"fIntro\":\"null\",\"fCreateTime\":\"null\"},{\"refId\":\"13\",\"fUserId\":\"14\",\"fUserName\":\"j\",\"fNickName\":\"J\",\"fSignature\":\"null\",\"fUserIcon\":\"null\",\"fIntro\":\"null\",\"fCreateTime\":\"null\"},{\"refId\":\"14\",\"fUserId\":\"15\",\"fUserName\":\"h\",\"fNickName\":\"H\",\"fSignature\":\"null\",\"fUserIcon\":\"null\",\"fIntro\":\"null\",\"fCreateTime\":\"null\"},{\"refId\":\"15\",\"fUserId\":\"16\",\"fUserName\":\"g\",\"fNickName\":\"G\",\"fSignature\":\"null\",\"fUserIcon\":\"null\",\"fIntro\":\"null\",\"fCreateTime\":\"null\"},{\"refId\":\"16\",\"fUserId\":\"17\",\"fUserName\":\"f\",\"fNickName\":\"F\",\"fSignature\":\"null\",\"fUserIcon\":\"null\",\"fIntro\":\"null\",\"fCreateTime\":\"null\"},{\"refId\":\"17\",\"fUserId\":\"18\",\"fUserName\":\"d\",\"fNickName\":\"D\",\"fSignature\":\"null\",\"fUserIcon\":\"null\",\"fIntro\":\"null\",\"fCreateTime\":\"null\"},{\"refId\":\"18\",\"fUserId\":\"19\",\"fUserName\":\"s\",\"fNickName\":\"S\",\"fSignature\":\"null\",\"fUserIcon\":\"null\",\"fIntro\":\"null\",\"fCreateTime\":\"null\"}]}}";
//                GetFriendListResp getFriendListResp = new Gson().fromJson(jsonTest, GetFriendListResp.class);
//                GetFriendListRespBody getFriendListRespBody = getFriendListResp.getGetFriendListRespBody();


                /*int i = getFriendListRespBody.getEndNum();
                logger.d("响应成功,end值为:"+ i);
                List<FriendDetailInfo> friendDetailInfoList = getFriendListRespBody.getDatas();
                logger.d("响应成功,list为:"+ friendDetailInfoList.get(0).getfNickName());*/

                /*MsgHeader msgHeader = new MsgHeader("IM003","1.0","1","2016-05-29 09:54:02","",
                        "BBS000201605290954020214");
                GetFriendListReqBody getFriendListReqBody = new GetFriendListReqBody("1","1","50");
                String body = new Gson().toJson(getFriendListReqBody).toString();
                logger.d("body为:"+body);

                String header = new Gson().toJson(msgHeader).toString();
                logger.d("header为:"+header);

                String exmMingwen = "{\"msgHeader\":"+header+",\"msgBody\":"+body+"}";
                logger.d("加密前的明文为:"+exmMingwen);

                String b = EncryUtil.encrypt(body, SysConstant.key);

                String exm = "{\"msgHeader\":"+header+",\"msgBody\":\""+b+"\"}";
                logger.d("加密后的暗文为:"+exm);


                String aaa = "BvinNq5QJVcX+/2AzuwUePiX7Q8xvEIjj5vcMW9ciH4cxmpy4o4l7MBftBcjUIObv2ph2cnCjdcqETAyTeNghfj+gXeJEoRTnJuEUp92kgozjDbW6mMJl0972QWsWlTSwOMoDBZCFM/NYZPvOgzQq05y63ERxaaV3PxV9A3FVyv+jAS/5SEmbwN4fmJprij9EqUJhjJT2i6q6smISXbqLIw0zU+3JrNtDMedj0Qu4fB3x0FJtnvHzfdgFy/3XUy9bfXvZ1Sis7qw2/4sMzqYMtJaivGYsdddv6cqiH8e9H9knsqVfPiUsorHqMc1osF0mcqGIxQ5mRlnvpCsrqX/3404LlS3ei9QLeUczoMzcpp/wU+o2pZ+H2NOtLqCrpFKAcueMlpWvqjzWF+Wre8t4x9ZtriHGnfeH05O6BJbJKz0wNheysRYJV5RntsDlvEsJQ9TU/Rzyhmr+Rj4HYUQnUDU7RwEXPd+YmBaBEtUSvBjPW34V/jcwYBMEZSvlck=";
                String a = EncryUtil.decrypt(aaa, SysConstant.key);
                logger.d("解密后的明文为:"+ a);

                try {
                    OkHttpUtils.postString().url(UrlConstant.URL_HOST_ADDRESS)
                            .content(exm).build().execute(new MyStringCallback());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }*/
            }
        });
        initAutoLogin();
    }

    public void getTest(){
        TelephonyManager tm = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();
        logger.i("DEVICE_ID ", deviceId + " ");


        String androidId = android.provider.Settings.System.getString(getContentResolver(), android.provider.Settings.System.ANDROID_ID);
        logger.i("ANDROID_ID", androidId + " ");

        mNameView.setText("deviceID:"+deviceId+",androidId:"+androidId);

    }


    public class MyStringCallback extends StringCallback
    {
        @Override
        public void onBefore(Request request)
        {
            super.onBefore(request);
            logger.d("响应before:"+request.toString());
        }

        @Override
        public void onAfter()
        {
            super.onAfter();
            logger.d("响应after");
        }

        @Override
        public void onError(Call call, Exception e)
        {
            e.printStackTrace();
            logger.d("响应失败:"+e.getMessage());
        }

        @Override
        public void onResponse(String response)
        {
            logger.d("响应成功:"+response);
//            String a = {"msgHeader":{"serviceCode":"IM003","version":"1.0","sysPlatCode":"BBS","sentTime":"2016-05-2909:54:02","expTime":"","sMessageNo":"BBS000201605290954020214"},"msgBody":{"userId":"IM0032016052415501119422","countNum":"2","startNum":"1","endNum":"50","Datas":[{"refId":"000000111","fUserId":"122222","fUserName":"1","fNickName":"成功","fSignature":"0000001","fUserIcon":"1","fIntro":"123成功","fCreateTime":"2016-05-2410:33:45"},{"refId":"000000112","fUserId":"2222212","fUserName":"1","fNickName":"aa","fSignature":"0000001","fUserIcon":"1","fIntro":"a","fCreateTime":"2016-05-2410:33:45"}]}}

            GetFriendListResp getFriendListResp = new Gson().fromJson(response, GetFriendListResp.class);
//            GetFriendListRespBody getFriendListRespBody = getFriendListResp.getGetFriendListRespBody();
            String encryedBody = getFriendListResp.getGetFriendListRespBody();
            String decryBody = EncryUtil.decrypt(encryedBody, SysConstant.key);
            GetFriendListRespBody getFriendListRespBody = new Gson().fromJson(decryBody, GetFriendListRespBody.class);
            int i = getFriendListRespBody.getEndNum();
            logger.d("响应成功,end值为:"+ i);
            List<FriendDetailInfo> friendDetailInfoList = getFriendListRespBody.getDatas();
            logger.d("响应成功,list为:"+ friendDetailInfoList.get(0).getfNickName());
        }

        @Override
        public void inProgress(float progress)
        {
        }
    }


    private void initAutoLogin() {
        logger.i("login#initAutoLogin");

        splashPage = findViewById(R.id.splash_page);
        loginPage = findViewById(R.id.login_page);
        autoLogin = shouldAutoLogin();

        splashPage.setVisibility(autoLogin ? View.VISIBLE : View.GONE);
        loginPage.setVisibility(autoLogin ? View.GONE : View.VISIBLE);

        loginPage.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (mPasswordView != null) {
                    intputManager.hideSoftInputFromWindow(mPasswordView.getWindowToken(), 0);
                }

                if (mNameView != null) {
                    intputManager.hideSoftInputFromWindow(mNameView.getWindowToken(), 0);
                }

                return false;
            }
        });

        if (autoLogin) {
            Animation splashAnimation = AnimationUtils.loadAnimation(this, R.anim.login_splash);
            if (splashAnimation == null) {
                logger.e("login#loadAnimation login_splash failed");
                return;
            }

            splashPage.startAnimation(splashAnimation);
        }
    }

    // 主动退出的时候， 这个地方会有值,更具pwd来判断
    private boolean shouldAutoLogin() {
        Intent intent = getIntent();
        if (intent != null) {
            boolean notAutoLogin = intent.getBooleanExtra(IntentConstant.KEY_LOGIN_NOT_AUTO, false);
            logger.d("login#notAutoLogin:%s", notAutoLogin);
            if (notAutoLogin) {
                return false;
            }
        }
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        imServiceConnector.disconnect(LoginActivity.this);
        EventBus.getDefault().unregister(this);
        splashPage = null;
        loginPage = null;
    }


    public void attemptLogin() {

        String loginName = mNameView.getText().toString();
        String mPassword = mPasswordView.getText().toString();
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(mPassword)) {
            Toast.makeText(this, getString(R.string.error_pwd_required), Toast.LENGTH_SHORT).show();
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(loginName)) {
            Toast.makeText(this, getString(R.string.error_name_required), Toast.LENGTH_SHORT).show();
            focusView = mNameView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            if (imService != null) {
//				boolean userNameChanged = true;
//				boolean pwdChanged = true;
                loginName = loginName.trim();
                mPassword = mPassword.trim();
                imService.getLoginManager().login(loginName, mPassword);
            }
        }
    }

    private void showProgress(final boolean show) {
        if (show) {
            mLoginStatusView.setVisibility(View.VISIBLE);
        } else {
            mLoginStatusView.setVisibility(View.GONE);
        }
    }

    // 为什么会有两个这个
    // 可能是 兼容性的问题 导致两种方法onBackPressed
    @Override
    public void onBackPressed() {
        logger.d("login#onBackPressed");
        //imLoginMgr.cancel();
        // TODO Auto-generated method stub
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//            LoginActivity.this.finish();
//            return true;
//        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    /**
     * ----------------------------event 事件驱动----------------------------
     */
    public void onEventMainThread(LoginEvent event) {
        switch (event) {
            case LOCAL_LOGIN_SUCCESS:
            case LOGIN_OK:
                onLoginSuccess();
                break;
            case LOGIN_AUTH_FAILED:
            case LOGIN_INNER_FAILED:
                if (!loginSuccess)
                    onLoginFailure(event);
                break;
        }
    }


    public void onEventMainThread(SocketEvent event) {
        switch (event) {
            case CONNECT_MSG_SERVER_FAILED:
            case REQ_MSG_SERVER_ADDRS_FAILED:
                if (!loginSuccess)
                    onSocketFailure(event);
                break;
        }
    }

    private void onLoginSuccess() {
        logger.i("login#onLoginSuccess");

        Tracker t = ((IMApplication)getApplication())
                .getDefaultTracker();
        t.send(new HitBuilders.EventBuilder().setCategory("Login")
                .setAction("loginSuccess").setLabel("登陆成功").build());

        loginSuccess = true;
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();
    }

    private void onLoginFailure(LoginEvent event) {
        logger.e("login#onLoginError -> errorCode:%s", event.name());
        Tracker t = ((IMApplication)getApplication())
                .getDefaultTracker();
        t.send(new HitBuilders.EventBuilder().setCategory("Login")
                .setAction("loginFailue").setLabel("登陆失败").build());
        showLoginPage();
        String errorTip = getString(IMUIHelper.getLoginErrorTip(event));
        logger.d("login#errorTip:%s", errorTip);
        mLoginStatusView.setVisibility(View.GONE);
        Toast.makeText(this, errorTip, Toast.LENGTH_SHORT).show();
    }

    private void onSocketFailure(SocketEvent event) {
        logger.e("login#onLoginError -> errorCode:%s,", event.name());
        showLoginPage();
        String errorTip = getString(IMUIHelper.getSocketErrorTip(event));
        logger.d("login#errorTip:%s", errorTip);
        mLoginStatusView.setVisibility(View.GONE);
        Toast.makeText(this, errorTip, Toast.LENGTH_SHORT).show();
    }
}
