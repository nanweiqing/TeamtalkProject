package com.mogujie.tt.app;

import android.app.Application;
import android.content.Intent;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.mogujie.tt.R;
import com.mogujie.tt.imservice.service.IMService;
import com.mogujie.tt.utils.ImageLoaderUtil;
import com.mogujie.tt.utils.Logger;
/*import com.squareup.leakcanary.LeakCanary;*/
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;


public class IMApplication extends Application {

	private Logger logger = Logger.getLogger(IMApplication.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	@Override
	public void onCreate() {
		super.onCreate();
		logger.i("Application starts");
		startIMService();
		ImageLoaderUtil.initImageLoaderConfig(getApplicationContext());

//		LeakCanary.install(this);

		OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
				.connectTimeout(10000L, TimeUnit.MILLISECONDS)
				.readTimeout(10000L, TimeUnit.MILLISECONDS)
				//其他配置
				.build();

		OkHttpUtils.initClient(okHttpClient);

	}

	private void startIMService() {
		logger.i("start IMService");
		Intent intent = new Intent();
		intent.setClass(this, IMService.class);
		startService(intent);
	}

    public static boolean gifRunning = true;//gif是否运行


	private Tracker mTracker;
	synchronized public Tracker getDefaultTracker() {
		if (mTracker == null) {
			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);

			mTracker = analytics.newTracker(R.xml.analytics);
		}
		return mTracker;
	}
}
