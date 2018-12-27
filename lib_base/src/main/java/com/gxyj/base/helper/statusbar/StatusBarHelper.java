package com.gxyj.base.helper.statusbar;

import android.app.Activity;
import android.view.WindowManager;

import com.gyf.barlibrary.ImmersionBar;

/**
 * @author lizepeng
 * @time 2017/12/22 1:56
 * @desc 状态栏封装
 */

public class StatusBarHelper {
    private ImmersionBar mImmersionBar;

    public StatusBarHelper(Activity activity) {
        mImmersionBar = ImmersionBar.with(activity);
    }

//    //使用白色时必须使用android.R.color.white，防止resInt不同
//    public void setBarStyle(@ColorRes int color) {
//        mImmersionBar.fitsSystemWindows(true)
//                .statusBarColor(color)
//                .navigationBarAlpha(0.05f)  //导航栏透明度，不写默认0.0F
//                .navigationBarColor("#F2F2F2");
//        if (color == android.R.color.white) {
//            mImmersionBar.statusBarDarkFont(true, 0.2f);
//        } else {
//            mImmersionBar.statusBarDarkFont(false);
//        }
//        mImmersionBar.init();
//    }
//
//    @SuppressLint("ResourceAsColor")
//    public void setBarStyle(String color) {
//        mImmersionBar.fitsSystemWindows(true)
//                .statusBarColor(color)
//                .navigationBarAlpha(0.05f)  //导航栏透明度，不写默认0.0F
//                .navigationBarColor("#F2F2F2")
//                .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//        if (android.R.color.white == Color.parseColor(color)) {
//            mImmersionBar.statusBarDarkFont(true, 0.2f);
//        } else {
//            mImmersionBar.statusBarDarkFont(false);
//        }
//        mImmersionBar.init();
//    }

    public void setBarStyleFullScreenWithStatus(boolean isStatusDarkFont) {
        mImmersionBar.fitsSystemWindows(false)
                .transparentStatusBar()
                .navigationBarAlpha(0.05f)  //导航栏透明度，不写默认0.0F
                .navigationBarColor("#F2F2F2")
                .statusBarDarkFont(isStatusDarkFont)
                .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
                .init();
    }

    public void setStatuBarFontColor(boolean isDark) {
        mImmersionBar.statusBarDarkFont(isDark)
                .init();
    }

    public void destroy() {
        mImmersionBar.destroy();
    }
}
