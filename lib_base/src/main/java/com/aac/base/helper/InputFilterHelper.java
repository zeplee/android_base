package com.aac.base.helper;

import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lizepeng
 * @time 2017/09/01 16:34
 * @desc EditText输入框输入正则限制帮助类 参考  https://github.com/srcoding/inputFilter
 * @use editText.setFilters(new InputFilterHelper.Builder ()
 * .allow(InputFilterHelper.RegexConstants.REGEX_USERNAME)//允许用户名(包含数字手机号)
 * .allow(InputFilterHelper.RegexConstants.REGEX_EMAIL)//允许邮箱
 * .limitLength(11)//设置最大字符数
 * .build().genFilters());
 * <p>
 * 注意：使用时xml中的maxlength会失效,使用limitLength代替；maxline=1使用singLine代替
 * @updateAuthor $Author$
 * @updateData $Date$
 */

public class InputFilterHelper {
    /**
     * 单个char中可以出现的字符，并不是整体匹配
     * 只需要正则内部的内容就可
     */
    //正则：用户名 取值范围为 字母a-z,A-Z, 数字0-9,汉字,"-", 用户名必须是6-20位  (还要兼容pc的_)
    public static final String REGEX_USERNAME = "a-zA-Z\\d-_\\u4e00-\\u9fa5";
    //正则：邮箱
    public static final String REGEX_EMAIL = "a-zA-Z\\d_.@";
    //正则：汉字
    public static final String REGEX_CHZ = "\\u4e00-\\u9fa5";
    //正则：字母
    public static final String REGEX_LETTER = "a-zA-Z";
    //正则：数字
    public static final String REGEX_NUMBER = "\\d";
    //正则：字母数字下划线
    public static final String REGEX_W = "\\da-zA-Z_";
    //正则：标点
    public static final String REGEX_POINT = "\\.!@#$，。,！¥}{][;？=+-——|/%^&*():?><~";

    private Pattern mWhitePattern;
    private Pattern mBlackPattern;

    /**
     * 限制输入框输入的字符数
     */
    private InputFilter.LengthFilter mLengthFilter = null;

    private InputFilterHelper(Builder builder) {
        List<String> whiteRegexList = new ArrayList<>();
        List<String> blackRegexList = new ArrayList<>();
        if (builder != null && builder.whiteRegexList.size() > 0) {
            whiteRegexList.addAll(builder.whiteRegexList);
            mWhitePattern = genWhitePattern(whiteRegexList);
        }
        if (builder != null && builder.blackRegexList.size() > 0) {
            blackRegexList.addAll(builder.blackRegexList);
            mBlackPattern = genBlackPattern(blackRegexList);
        }
        mLengthFilter = builder.lengthFilter;

    }


    /**
     * 生成EditText的filters
     *
     * @return 返回InputFilter数组，供EditText使用个{@link android.widget.EditText#setFilters(InputFilter[])}
     */
    public InputFilter[] genFilters() {
        List<InputFilter> inputFilterList = new ArrayList<>();
        if (mLengthFilter != null) {
            inputFilterList.add(mLengthFilter);
        }
        inputFilterList.add(new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String s = innerFilter(source.toString());
                //输入汉字时，拼音、笔画等属于Spanned，需要返回SpannedString，当输入完成后，才能生成一个汉字
                //否则，会将拼音也展示在EditText中
                if (source instanceof Spanned) {
                    SpannableString sp = new SpannableString(s);
                    //参考 http://blog.csdn.net/kennethyo/article/details/51852451
                    TextUtils.copySpansFrom((Spanned) source, start, sp.length(), null, sp, 0);
                    return sp;
                } else {
                    return s;
                }
            }
        });
        return inputFilterList.toArray(new InputFilter[inputFilterList.size()]);
    }

    @NonNull
    private String innerFilter(String source) {
        Matcher matcher;
        if (mBlackPattern != null) {
            matcher = mBlackPattern.matcher(source);
            source = matcher.replaceAll("").trim();
        }
        if (mWhitePattern != null) {
            matcher = mWhitePattern.matcher(source);
            source = matcher.replaceAll("").trim();
        }
        return source;
    }

    /**
     * 生成Pattern对象
     * 该Pattern对象支持的正则与whiteRegexList支持的正则相反，
     * 目的是可以通过{@link Matcher#replaceAll(String)}方法将不在whiteRegexList支持范围的字符都过滤调
     *
     * @param whiteRegexList 所有支持的正则List
     * @return Pattern对象
     */
    private Pattern genWhitePattern(List<String> whiteRegexList) {
        StringBuilder whiteRegexb = new StringBuilder("[^");
        for (String whiteRegex : whiteRegexList) {
            whiteRegexb.append(whiteRegex);
        }
        whiteRegexb.append("]");
        return Pattern.compile(whiteRegexb.toString());
    }

    /**
     * 生成Pattern对象
     * 该Pattern对象支持的正则与filterHandlerList支持的正则相反，
     * 目的是可以通过{@link Matcher#replaceAll(String)}方法将不在blackRegexList支持范围的字符都过滤调
     *
     * @param blackRegexList 所有不支持的正则List
     * @return Pattern对象
     */
    private Pattern genBlackPattern(List<String> blackRegexList) {
        StringBuilder blackRegexSb = new StringBuilder("[");
        for (String blackRegex : blackRegexList) {
            blackRegexSb.append(blackRegex);
        }
        blackRegexSb.append("]");
        return Pattern.compile(blackRegexSb.toString());
    }


    public static class Builder {
        List<String> whiteRegexList = new ArrayList<>();
        List<String> blackRegexList = new ArrayList<>();
        private InputFilter.LengthFilter lengthFilter;

        /**
         * 添加白名单
         *
         * @param whiteRegex
         * @return
         */
        public Builder allow(String whiteRegex) {
            if (!TextUtils.isEmpty(whiteRegex)) {
                this.whiteRegexList.add(whiteRegex);
            }
            return this;
        }

        /**
         * 添加黑名单
         *
         * @param blackRegex
         * @return
         */
        public Builder refuse(String blackRegex) {
            if (!TextUtils.isEmpty(blackRegex)) {
                this.blackRegexList.add(blackRegex);
            }
            return this;
        }

        /**
         * 输入框文字长度限制
         *
         * @param limitLength 输入最大字符数
         * @return
         */
        public Builder limitLength(int limitLength) {
            if (limitLength > 0) {
                lengthFilter = new InputFilter.LengthFilter(limitLength);
            }
            return this;
        }

        public InputFilterHelper build() {
            return new InputFilterHelper(this);
        }
    }

}
