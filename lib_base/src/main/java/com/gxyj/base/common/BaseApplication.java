//package com.gxyj.base.common;
//
//import android.annotation.SuppressLint;
//import android.app.ActivityManager;
//import android.app.ActivityManager.RunningTaskInfo;
//import android.app.Application;
//import android.app.Notification;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.media.AudioManager;
//import android.os.Handler;
//import android.support.v4.app.NotificationCompat;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.blankj.utilcode.util.Utils;
//import com.gxyj.base.R;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.List;
//import java.util.Properties;
//
//public class BaseApplication extends Application {
//
//    private static final String TAG = BaseApplication.class.getName();
//    public static final String UPDATE_STATUS_ACTION = "com.umeng.message.example.action.UPDATE_STATUS";
//    private static BaseApplication instance;
//    private Handler handler;
//    public static PushAgent mPushAgent;
//    public static UMShareAPI umShareAPI;
//    private Notification notification = null;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        UMConfigure.init(this, "5aefda2cf29d984dd50000b7", "GxsUmeng", UMConfigure.DEVICE_TYPE_PHONE,
//                "18374b12876089331f70790de54927cc");
//        instance = this;
//        Utils.init(this);
//        initUpush();
//        initShare();
//    }
//
//    private void initShare() {
//        umShareAPI = UMShareAPI.get(this);
//        // UMShareConfig config = new UMShareConfig();
//        // config.isNeedAuthOnGetUserInfo(true);
//        // umShareAPI.setShareConfig(config);
//        // 微信分享
//        PlatformConfig.setWeixin("wx9d0ba6047055a8a5", "a5a8f7ffe18b1c71f5e84db0898c1394");
//        // QQ分享
//        PlatformConfig.setQQZone("1106692078", "K8Bt6gC8ATspOcDM");
//        // 新浪分享
//        PlatformConfig.setSinaWeibo("863296385", "f2b483a8b8d18e18566c8d8f0de449fe", "http://sns.whalecloud.com");
//    }
//
//    private void initUpush() {
//        mPushAgent = PushAgent.getInstance(this);
//        handler = new Handler(getMainLooper());
//        SetRingAndShock(getApplicationContext());
//        mPushAgent.setNotificationPlayLights(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
//        UmengMessageHandler messageHandler = new UmengMessageHandler() {
//            /**
//             * 自定义消息的回调方法
//             */
//            @Override
//            public void dealWithCustomMessage(final Context context, final UMessage msg) {
//
//                handler.post(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        // TODO Auto-generated method stub
//                        // 对自定义消息的处理方式，点击或者忽略
//                        boolean isClickOrDismissed = true;
//                        if (isClickOrDismissed) {
//                            // 自定义消息的点击统计
//                            UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
//                        } else {
//                            // 自定义消息的忽略统计
//                            UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
//                        }
//                        Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
//
//            /**
//             * 自定义通知栏样式的回调方法
//             */
//            @SuppressWarnings("unchecked")
//            @SuppressLint("NewApi")
//            @Override
//            public Notification getNotification(Context context, UMessage msg) {
//                // NotificationCompat
//                NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(context);
//                SharedPreferences settings = context.getSharedPreferences("com.forlink.gxs_pro", Context.MODE_PRIVATE);
//                boolean isDetails = settings.getBoolean("detail_key", true);// 响铃
//                mNotifyBuilder.setContentTitle(msg.title);
//                if (msg.title != null && !msg.title.equals("")) {
//                    mNotifyBuilder.setContentText(!isDetails ? "" : msg.text);
//                }
//
//                mNotifyBuilder.setSmallIcon(R.drawable.ic_launcher);
//
//                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
//                mNotifyBuilder.setLargeIcon(bitmap);
//                mNotifyBuilder.setAutoCancel(true);
//                notification = mNotifyBuilder.build();
//                return notification;
//
//            }
//        };
//        mPushAgent.setMessageHandler(messageHandler);
//
//        /**
//         * 自定义行为的回调处理，参考文档：高级功能-通知的展示及提醒-自定义通知打开动作
//         * UmengNotificationClickHandler是在BroadcastReceiver中被调用，故
//         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
//         */
//        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
//            @Override
//            public void dealWithCustomAction(Context context, UMessage msg) {
//
//                if (getAppSatus(instance) == 1 || getAppSatus(instance) == 2) {
//                    Log.i("NotificationReceiver", "the app process is alive");
//                    Intent mainIntent = new Intent(context, IndexActivity.class);
//                    // 将MainAtivity的launchMode设置成SingleTask,
//                    // 或者在下面flag中加上Intent.FLAG_CLEAR_TOP,
//                    // 如果Task栈中有MainActivity的实例，就会把它移到栈顶，把在它之上的Activity都清理出栈，
//                    // 如果Task栈不存在MainActivity实例，则在栈顶创建
//                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    Intent detailIntent = new Intent(context, Message_Url_Load.class);
//                    detailIntent.putExtra("messageurl", msg.extra.get("url"));
//                    Intent[] intents = {mainIntent, detailIntent};
//                    context.startActivities(intents);
//
////                    mainIntent.putExtra("messageurl", msg.extra.get("url"));
////                    context.startActivity(mainIntent);
//                } else {
//                    Log.i("NotificationReceiver", "the app process is dead");
//                    Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage("com.forlink.gxsxm");
//                    launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//                    launchIntent.putExtra("messageurl", msg.extra.get("url"));
//                    context.startActivity(launchIntent);
//                }
//
//            }
//        };
//        // 使用自定义的NotificationHandler，来结合友盟统计处理消息通知，参考http://bbs.umeng.com/thread-11112-1-1.html
//        // CustomNotificationHandler notificationClickHandler = new
//        // CustomNotificationHandler();
//        mPushAgent.setNotificationClickHandler(notificationClickHandler);
//
//        // 注册推送服务 每次调用register都会回调该接口
//        mPushAgent.register(new IUmengRegisterCallback() {
//            @Override
//            public void onSuccess(String deviceToken) {
//                Log.i("flag", deviceToken);
//                sendBroadcast(new Intent(UPDATE_STATUS_ACTION));
//            }
//
//            @Override
//            public void onFailure(String s, String s1) {
//                sendBroadcast(new Intent(UPDATE_STATUS_ACTION));
//            }
//        });
//
//    }
//
//    public static BaseApplication getInstance() {
//        return instance;
//    }
//
//    public String getproperties(String name) {// 获取配置文件信息
//        String url = "";
//        Properties properties = new Properties();
//        try {
//            InputStream in = getResources().getAssets().open("ConfigurePaths.properties");
//            properties.load(in);
//            in.close();
//        } catch (IOException e) {
//            return e.toString();
//        }
//        try {
//            url = properties.getProperty(name);
//            url = url.trim();
//        } catch (Exception e) {
//            url = "";
//        }
//        return url;
//    }
//
//    public static void SetRingAndShock(Context context) {
//        AudioManager volMgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
//        SharedPreferences settings = context.getSharedPreferences("com.forlink.gxs_pro", Context.MODE_PRIVATE);
//        boolean allowRing = settings.getBoolean("sound_key", true);// 响铃
//        boolean allowVibrator = settings.getBoolean("vibrate_key", true);// 震动
//
//        switch (volMgr.getRingerMode()) {// 获取系统设置的铃声模式
//            case AudioManager.RINGER_MODE_SILENT:// 静音模式，值为0，这时候不震动，不响铃
//                mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
//                mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
//                break;
//            case AudioManager.RINGER_MODE_VIBRATE:// 震动模式，值为1，这时候震动，不响铃
//                mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
//                if (allowVibrator) {
//                    mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
//                } else {
//                    mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
//                }
//                break;
//            case AudioManager.RINGER_MODE_NORMAL:// 常规模式，值为2，分两种情况：1_响铃但不震动，2_响铃+震动
//                if (allowRing) {
//                    mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
//                } else {
//                    mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
//                }
//                if (allowVibrator) {
//                    mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
//                } else {
//                    mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
//                }
//                break;
//        }
//    }
//
//    @SuppressWarnings("unused")
//    private boolean isApplicationBroughtToBackground(final Context context) {
//        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
//        if (!tasks.isEmpty()) {
//            ComponentName topActivity = tasks.get(0).topActivity;
//            if (!topActivity.getPackageName().equals(context.getPackageName())) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public int getAppSatus(Context context) {
//
//        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        List<RunningTaskInfo> list = am.getRunningTasks(20);
//
//        // 判断程序是否在栈顶
//        if (list.get(0).topActivity.getPackageName().equals(context.getPackageName())) {
//            return 1;
//        } else {
//            // 判断程序是否在栈里
//            for (RunningTaskInfo info : list) {
//                if (info.topActivity.getPackageName().equals(context.getPackageName())) {
//                    return 2;
//                }
//            }
//
//            return 3;// 栈里找不到，返回3
//        }
//    }
//
//}
