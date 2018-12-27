package com.gxyj.base.helper.network;

import android.app.Application;
import android.text.TextUtils;

import com.blankj.utilcode.util.AppUtils;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.gxyj.base.ConstantsKt;
import com.gxyj.base.helper.network.callback.DataCallback;
import com.gxyj.base.helper.network.callback.FileDownCallback;
import com.gxyj.base.helper.network.callback.FileUpCallback;
import com.gxyj.base.helper.urlmanager.data.UrlConfigBean;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.SPCookieStore;
import com.lzy.okgo.db.CacheManager;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okgo.request.base.Request;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadTask;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

public class NetRequestHelper {
    public static void init(Application context) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (ConstantsKt.IS_DEBUG_MODE) {
            HttpLoggingInterceptor logIntercept = new HttpLoggingInterceptor(AppUtils.getAppName());
            logIntercept.setPrintLevel(HttpLoggingInterceptor.Level.BODY);//log显示的详细程度
            logIntercept.setColorLevel(Level.INFO);//log在控制台显示的颜色
            builder.addInterceptor(logIntercept).addNetworkInterceptor(new StethoInterceptor());
        }
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS / 4, TimeUnit.MILLISECONDS)
                .writeTimeout(OkGo.DEFAULT_MILLISECONDS / 4, TimeUnit.MILLISECONDS)
                .connectTimeout(OkGo.DEFAULT_MILLISECONDS / 4, TimeUnit.MILLISECONDS)
                //sp保持cookie，如果cookie不过期，则一直有效; 数据库保持cookie，如果cookie不过期，则一直有效 new DBCookieStore(Utils.getApp());内存保持cookie，app退出后，cookie消失 new MemoryCookieStore()
                .cookieJar(new CookieJarImpl(new SPCookieStore(context)))
                .followRedirects(true); //自动处理重定向
        OkGo.getInstance().init(context)
                .setOkHttpClient(builder.build());//建议设置OkHttpClient，不设置将使用默认的
        Stetho.initializeWithDefaults(context);
    }

    // 清除缓存
    public static Boolean clearCache() {
        return CacheManager.getInstance().clear();
    }

    public static void cancleRequest(String tag) {
        OkGo.getInstance().cancelTag(tag);
    }

    public static void doCommonRequest(String tag,
                                       UrlConfigBean.UrlBean urlBean,
                                       Map<String, String> params,
                                       DataCallback callback) {
        getRequest(tag, urlBean, params, null, null).execute(callback);
    }

    public static void doUpRequest(String tag,
                                   UrlConfigBean.UrlBean urlBean,
                                   Map<String, String> params,
                                   String upFileName, List<File> upFiles,
                                   FileUpCallback callback) {
        getRequest(tag, urlBean, params, upFileName, upFiles).execute(callback);
    }

    public static void doDownRequest(String tag,
                                     UrlConfigBean.UrlBean urlBean,
                                     Map<String, String> params,
                                     String downPath, String downFileName,
                                     FileDownCallback callback) {
        OkDownload.request(tag, getRequest(tag, urlBean, params, null, null))
                .folder(downPath)
                .fileName(downFileName)
                .save()
                .register(callback)
                .restart();
    }

    public static DownloadTask getDownTask(String tag) {
        return OkDownload.getInstance().getTask(tag);
    }

    private static Request getRequest(String tag,
                                      UrlConfigBean.UrlBean urlBean,
                                      Map<String, String> params,
                                      String upFileName,
                                      List<File> upFiles) {
        Request request;
        switch (urlBean.getMethod()) {
            case "post":
                request = OkGo.post(urlBean.getUrl());
                if (upFiles != null) {
                    ((PostRequest) request)
                            .addFileParams(upFileName, upFiles)
                            //不加这行的话当files的size为0时提交不为表单类型，后台不好处理，所有强制
                            .isMultipart(true);
                }
            default:
                request = OkGo.get(urlBean.getUrl());
        }
        request.tag(tag).params(params);
        if (!TextUtils.isEmpty(urlBean.getCacheMode())) {
            request.cacheMode(getCacheMode(urlBean.getCacheMode()));
        }
        if (urlBean.getCacheTime() > 0L) {
            request.cacheTime(urlBean.getCacheTime());
        }
        return request;
    }

    private static CacheMode getCacheMode(String cacheMode) {
        switch (cacheMode) {
            case "DEFAULT":
                return CacheMode.DEFAULT;
            case "NO_CACHE":
                return CacheMode.NO_CACHE;
            case "REQUEST_FAILED_READ_CACHE":
                return CacheMode.REQUEST_FAILED_READ_CACHE;
            case "IF_NONE_CACHE_REQUEST":
                return CacheMode.IF_NONE_CACHE_REQUEST;
            case "FIRST_CACHE_THEN_REQUEST":
                return CacheMode.FIRST_CACHE_THEN_REQUEST;
            default:
                return CacheMode.REQUEST_FAILED_READ_CACHE; //默认的缓存模式
        }
    }
}
