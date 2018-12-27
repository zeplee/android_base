package com.gxyj.base.widget.toast;

import android.content.Context;
import android.graphics.Color;
import android.widget.Toast;

import com.blankj.utilcode.util.ConvertUtils;


/**
 * 对toast进行处理优化，避免点击多次弹出多次Toast的问题
 **/
public class SealingToast {
    private static String oldMsg;
    protected static StyleableToast toast = null;
    public static final String toast_bg = "#BB000000";

    public static void showToast(Context context, int resId) {
        showToastIcon(context, context.getString(resId),0);
    }

    public static void showToast(Context context, String s) {
        showToastIcon(context, s,0);
    }

    public static void showToast(Context context, int resId, int gravity) {
        showToastIcon(context, context.getString(resId), gravity, 0, 0,0,2);
    }

    public static void showToast(Context context, String s, int gravity) {
        showToastIcon(context, s, gravity, 0, 0,0,2);
    }

    public static void showToast(Context context, String s, int gravity, int icon) {
        showToastIcon(context, s, gravity, 0, 0,icon,3);
    }

    public static void showToast(Context context, int resId, int gravity, int offX, int offY) {
        showToastIcon(context, context.getString(resId), gravity, offX, offY,0,2);
    }

    public static void showLongToast(Context context, int duration, String msg) {
        if (toast == null) {
            toast = new StyleableToast(context, msg, duration);
            //改动toast样式在下面代码设置就好
            toast.setBackgroundColor(Color.parseColor(toast_bg));
            toast.setTextColor(Color.WHITE);
            toast.setCornerRadius(ConvertUtils.dp2px( 2));
            toast.setMaxAlpha();
            toast.show();
        } else {
            toast.setToastMsg(msg);
            toast.setDuration(duration);
            toast.show();
        }
    }

    /**
     * 这个方法是显示在底部的
     * @param context
     * @param s
     */
    public static void showToastIcon(Context context, String s, int icon) {
        if (toast == null) {
            toast = new StyleableToast(context, s, Toast.LENGTH_SHORT);
            toast.setIcon(icon);
            //改动toast样式在下面代码设置就好
            toast.setBackgroundColor(Color.parseColor(toast_bg));
            toast.setTextColor(Color.WHITE);
            toast.setMaxAlpha();
            toast.setCornerRadius(-1);
            toast.show();
        } else {
            toast.setIcon(icon);
            toast.setToastMsg(s);
            toast.setCornerRadius(-1);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * 这个方法可以设置显示位置
     * 设置之后,吐司的圆角变为2dp----一般显示在中间的吐司需要是略微方形的
     * @param context
     * @param s
     */
    public static void showToastIcon(Context context, String s, int gravity, int offX, int offY, int icon, int radus) {
        if (toast == null) {
            toast = new StyleableToast(context, s, Toast.LENGTH_SHORT);
            //改动toast样式在下面代码设置就好
            toast.setBackgroundColor(Color.parseColor(toast_bg));
            toast.setTextColor(Color.WHITE);
            toast.setIcon(icon);
            toast.setMaxAlpha();
            //控制显示位置的
            toast.setCornerRadius(ConvertUtils.dp2px( radus));
            toast.show(gravity, offX, offY);
        } else {
            toast.setToastMsg(s);
            toast.setIcon(icon);
            toast.setCornerRadius(ConvertUtils.dp2px( radus));
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show(gravity, offX, offY);
        }
    }



}
