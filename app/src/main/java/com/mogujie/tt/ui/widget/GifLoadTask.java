package com.mogujie.tt.ui.widget;

import android.os.AsyncTask;

import com.mogujie.tt.utils.CommonUtil;
import com.zhy.http.okhttp.OkHttpUtils;
/*import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.intenal.rDiskLruCache;
import com.squareup.okhttp.internal.Util;
import com.zhy.http.okhttp.OkHttpUtils;*/

import org.apache.commons.io.IOUtils;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.DiskLruCache;
import okhttp3.internal.Util;
import okhttp3.internal.io.FileSystem;
import okio.BufferedSource;
import okio.Okio;

/**
 * Created by zhujian on 15/3/26.
 */
public class GifLoadTask extends AsyncTask<String, Void, byte[]> {

    public GifLoadTask() {
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected byte[] doInBackground(final String... params) {
        final String gifUrl = params[0];
        if (gifUrl == null)
            return null;
        byte[] gif = null;
        try {
            gif = byteArrayHttpClient(gifUrl);
        } catch (OutOfMemoryError e) {
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gif;
    }

    private FilterInputStream getFromCache(String url) throws Exception {
        DiskLruCache cache = DiskLruCache.create(FileSystem.SYSTEM,CommonUtil.getImageSavePath(),1, 2, 2*1024*1024);
        cache.flush();
        String key = Util.md5Hex(url);
        final DiskLruCache.Snapshot snapshot;
        try {
            snapshot = cache.get(key);
            if (snapshot == null) {
                return null;
            }
        } catch (IOException e) {
            return null;
        }
        okio.Source source = snapshot.getSource(1) ;
        BufferedSource metadata = Okio.buffer(source);
        FilterInputStream bodyIn = new FilterInputStream(metadata.inputStream()) {
            @Override
            public void close() throws IOException {
                snapshot.close();
                super.close();
            }
        };
        return bodyIn;
    }

    public byte[] byteArrayHttpClient(final String urlString) throws Exception {
        OkHttpClient client = null;
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        if (client == null) {
//            client = new OkHttpClient();
            Cache responseCache = new Cache(CommonUtil.getImageSavePath(), 2*1024*1024);
            builder.cache(responseCache);
            builder.readTimeout(30, java.util.concurrent.TimeUnit.SECONDS);
            builder.connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS);
            client = builder.build();

//            client.setCache(responseCache);
//            client.setReadTimeout(30, java.util.concurrent.TimeUnit.SECONDS);
//            client.setConnectTimeout(30, java.util.concurrent.TimeUnit.SECONDS);
        }
        FilterInputStream inputStream = getFromCache(urlString);
        if (inputStream != null) {
            return IOUtils.toByteArray(inputStream);
        }
        InputStream in = null;
        try {
            final String decodedUrl = URLDecoder.decode(urlString, "UTF-8");
            final URL url = new URL(decodedUrl);
            final Request request = new Request.Builder().url(url).build();
//            final Response response = client.newCall(request).execute();
            final Response response = OkHttpUtils.get().url(decodedUrl).build().execute();
            in = response.body().byteStream();
            return IOUtils.toByteArray(in);
        } catch (final MalformedURLException e) {
        } catch (final OutOfMemoryError e) {
        } catch (final UnsupportedEncodingException e) {
        } catch (final IOException e) {
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (final IOException ignored) {
                }
            }
        }
        return null;
    }
}

