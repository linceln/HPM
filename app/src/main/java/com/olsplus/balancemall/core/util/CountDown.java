package com.olsplus.balancemall.core.util;

import java.util.concurrent.TimeUnit;

import android.text.Html;
import android.text.TextUtils;
import android.widget.TextView;

public class CountDown {

    private TextView tv;

    private Callback callback;

    private String frontText = "";

    private String rearText = "";

    private String finishText = null;

    private long second = 0;

    private Runnable rTimerCountDown = new Runnable() {

        @Override
        public void run() {

            second--;
            if (second <= 0) {
                // 倒计时结束
                tv.removeCallbacks(this);
                if (finishText != null) {
                    tv.setText(finishText);
                }
                if (callback != null) {
                    tv.setEnabled(true);
                    callback.onFinish();
                }
                return;
            } else {

//                tv.setText(Html.fromHtml(frontText + "<font color='#ff0000'>" + secToTime(second) + "</font>" + rearText));
                tv.setText(frontText + secToTime(second) + rearText);
            }

            tv.postDelayed(this, TimeUnit.SECONDS.toMillis(1));
        }
    };

    /**
     * 倒计时
     *
     * @param tv       需要更新的控件instance of TextView(Button没有颜色效果)
     * @param duration 倒计时时长(单位：秒)
     * @param callback 倒计时监听
     */
    public void execute(final TextView tv, long duration, Callback callback) {

        // 倒计时进行中不再生效
        if (this.second != 0) {
            return;
        }

        this.tv = tv;
        this.tv.setEnabled(false);
        this.second = duration;
        if (callback != null) {
            this.callback = callback;
            this.callback.onStart();
        }
        this.tv.removeCallbacks(rTimerCountDown);
        this.tv.post(rTimerCountDown);
    }

    /**
     * 格式化时间
     *
     * @param time 单位：秒
     * @return
     */
    private String secToTime(long time) {

        String timeStr;
        long day;
        long hour;
        long minute;
        long second;
        if (time < 60) {
            timeStr = unitFormat(time) + "秒";
        } else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + "分" + unitFormat(second) + "秒";
            } else {
                hour = minute / 60;
                if (hour < 24) {
                    minute = minute % 60;
                    second = time - hour * 3600 - minute * 60;
                    timeStr = unitFormat(hour) + "时" + unitFormat(minute) + "分" + unitFormat(second) + "秒";
                } else {
                    day = hour / 24;
                    hour = hour % 24;
                    minute = minute - hour * 60 - day * 24 * 60;
                    second = time - minute * 60 - hour * 3600 - day * 24 * 3600;
                    timeStr = day + "天" + unitFormat(hour) + "时" + unitFormat(minute) + "分" + unitFormat(second) + "秒";

                }
            }
        }
        return timeStr;
    }

    private String unitFormat(long i) {

        String retStr;
        if (i >= 0 && i < 10)
            retStr = "0" + Long.toString(i);
        else retStr = "" + i;
        return retStr;
    }

    public interface Callback {

        /**
         * 倒计时开始前，可以做一些初始化操作
         * 例如点击倒计时后使按钮不可点击
         * 如果按钮和倒计时文本在同一个TextView上，默认已经做过不可点击处理，如一般的获取验证码过程
         * 如果按钮和倒计时文本不在同一个TextView上，则需要在倒计时开始前将按钮设置为不可点击，这样不会产生新对象，使倒计时错乱
         */
        void onStart();

        /**
         * 倒计时结束
         */
        void onFinish();

    }

    /**
     * 倒计时进行中的文案（默认为空）
     *
     * @param frontText
     * @param rearText
     */
    public void setTextOnCounting(String frontText, String rearText) {

        this.frontText = frontText;
        this.rearText = rearText;
    }

    /**
     * 倒计时结束时的文案（默认无）
     *
     * @param text text != null 时才显示
     */
    public void setTextOnFinished(String text) {
        this.finishText = text;
    }

    /**
     * 离开页面时记得停止倒计时，在onDestroy()方法中调用
     */
    public void cancel() {

        if (rTimerCountDown != null) {
            tv.removeCallbacks(rTimerCountDown);
        }
    }
}