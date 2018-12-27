package com.gxyj.base.utils;

import android.text.TextUtils;
import android.widget.TextView;

import com.blankj.utilcode.util.RegexUtils;

import java.text.DecimalFormat;

public class DecimalUtils {

    /**
     * 设置商品价格工具类，将价格格式化成¥2.00（保留两位小数）
     *
     * @param textView 设置显示价格的控件
     * @param price    商品的价格
     *                 备注：price要确保转换成double不会抛异常，否则会显示¥0.00
     **/
    public static void setTextFormatProductPrice(TextView textView, String price) {
        String pri = getPriceStrByFormat(price, "¥");
        textView.setText(pri);
    }

    /**
     * 格式化价格工具
     *
     * @param srcStr 需要格式化的商品的价格 eg:“2.568”
     * @return ¥2.57
     */

    public static String getPriceStrByFormat(String srcStr) {

        StringBuilder result = new StringBuilder();
        result.append("¥");
        float price;

        try {
            srcStr = srcStr.replace(",", "").replace("￥", "").replace("¥", "");
            price = Float.parseFloat(srcStr);
        } catch (Exception e) {
            price = 0f;
        }
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        result.append(decimalFormat.format(price));//format 返回的是字符串
        return result.toString();
    }

    /**
     * 格式化价格工具
     *
     * @param srcStr 需要格式化的商品的价格 eg:“2.568”
     * @return ¥2.57
     */

    public static String getPriceStrNoFormat(String srcStr) {

        float price;
        try {
            srcStr = srcStr.replace(",", "").replace("￥", "").replace("¥", "");
            price = Float.parseFloat(srcStr);
        } catch (Exception e) {
            price = 0f;
        }
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        return decimalFormat.format(price);
    }

    /**
     * 保留小数点后两位
     *
     * @param srcStr             需要保留小数的值
     * @param currencyTypeSymbol 单位符号
     */
    public static String getPriceStrByFormat(String srcStr, String currencyTypeSymbol) {
        if (TextUtils.isEmpty(srcStr)) {
            srcStr = "";
        } else {
            srcStr = srcStr.replace(",", "");
        }
        StringBuilder result = new StringBuilder();
        result.append(currencyTypeSymbol);
        float price;
        try {
            price = Float.parseFloat(srcStr);
        } catch (Exception e) {
            price = 0f;
        }
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        result.append(decimalFormat.format(price));//format 返回的是字符串
        String str1 = result.toString();
        if (str1.contains("¥.")) {
            return str1.replace("¥.", "¥0.");
        } else {
            return str1;
        }
    }

    /**
     * 返回格式为123112.00不带有¥符号
     * 格式化成带两位小数，四舍五入
     *
     * @return string
     */
    public static String getPriceStringByTwoPoint(String price) {
        double num = 0.00;
        if (TextUtils.isEmpty(price)) {
            return num + "";
        }
        price = price.replace(",", "");
        try {
            num = Double.parseDouble(price);
        } catch (Exception e) {
            num = 0.00;
        }
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(num);
    }


    /**
     * String 价格 转换成 double 类型，方便价格计算
     *
     */
    public static double getPriceStringFormatDouble(String price) {
        return getStringFormatDouble(price);
    }

    /**
     * String 转换成 double 类型
     *
     */
    public static double getStringFormatDouble(String string) {
        double num = 0.00;
        if (!TextUtils.isEmpty(string)) {
            try {
                num = Double.parseDouble(string.replace(",", "").replace(" ", ""));
            } catch (NumberFormatException e) {
                num = 0.00;
            }
        }
        return num;
    }

    /**
     * String 转化成 Int 类型
     */
    public static int getStringFormatInt(String str) {

        if (TextUtils.isEmpty(str)) {
            return 0;
        }

        int temp = 0;
        if (!TextUtils.isEmpty(str)) {
            try {
                temp = Integer.parseInt(str.replace(",", "").trim());
            } catch (NumberFormatException ignored) {

            }
        }
        return temp;
    }

    /**
     * 号码替换
     */
    public static String mobileReplace(String mobile) {
        if (!RegexUtils.isMobileExact(mobile)) {
            return mobile;
        }
        return mobile.substring(0, 3) + "****" + mobile.substring(7, 11);
    }

    //double保留小数点
    public static String doubleDigits(double number, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        StringBuilder digits = new StringBuilder("#0.");
        for (int i = 0; i < scale; i++) {
            digits.append("0");
        }
        DecimalFormat df = new DecimalFormat(digits.toString());
        return df.format(number);
    }
}
