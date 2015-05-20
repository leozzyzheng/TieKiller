package com.leozzyzheng.tiekiller.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DecimalFormat;

/**
 * 常用接口的封装
 * <p>目录</p>
 * <p>dp转px {@link #getPixFromDip}</p>
 * <p>px转dp {@link #getDipFromPix}</p>
 * <p>获取cpu频率 {@link #getMaxCpuFreq}</p>
 * <p>获取内存容量 {@link #getTotalRam}</p>
 * <p>判断是否有网络 {@link #isNetworkConnected}</p>
 * <p>判断是否是wifi {@link #isWifiMode}</p>
 * <p>字符串转整数 {@link #parseInt}</p>
 * <p>获取屏幕宽度 {@link #getScreenWidth}</p>
 * <p>在UI线程中运行 {@link #runInUiThread}</p>
 * <p>字节容量转字符串 {@link #byteToString}</p>
 * <p>字节速度转字符串 {@link #byteToString4Speed}</p>
 * <p>毫秒时间转字符串 {@link #millisecondsToString}</p>
 * <p>md5转字符串 {@link #md5ByteToString}</p>
 *
 * @author leozzyzheng
 */
@SuppressWarnings("unused")
public class Tools {

    /**
     * 像素转DIP,
     *
     * @param aDipValue dp值
     * @param context   context
     * @return px值
     */
    public static int getPixFromDip(float aDipValue, final Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wMgr = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wMgr.getDefaultDisplay().getMetrics(dm);
        return (int) (aDipValue * dm.density);
    }

    /**
     * 根据像素的值获得DIP
     *
     * @return dp值
     */
    public static int getDipFromPix(int aPixValue, final Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wMgr = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wMgr.getDefaultDisplay().getMetrics(dm);
        return (int) (aPixValue / dm.density);
    }

    // 获取cpu频率
    public static String getMaxCpuFreq() {
        String result = "0";
        FileReader localFileReader;
        BufferedReader localBufferedReader = null;
        try {
            String str1 = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq";
            localFileReader = new FileReader(str1);
            localBufferedReader = new BufferedReader(localFileReader, 8192);
            result = localBufferedReader.readLine();
            localBufferedReader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            result = "0";
        } finally {
            if (localBufferedReader != null) {
                try {
                    localBufferedReader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result.trim();
    }

    /**
     * 获取总内存数量
     *
     * @return 如果发生错误则返回null
     */
    public static String getTotalRam() {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String result = "0";
        String memStr = null;
        String[] arrayOfString;
        FileReader localFileReader = null;
        BufferedReader localBufferedReader = null;
        try {
            localFileReader = new FileReader(str1);
            localBufferedReader = new BufferedReader(localFileReader, 8192);
            memStr = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
            localBufferedReader.close();
            localFileReader.close();
        } catch (Exception e) {
            e.printStackTrace();
            memStr = null;
        } finally {
            if (localBufferedReader != null) {
                try {
                    localBufferedReader.close();
                    localFileReader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (memStr == null) {
            return null;
        }

        arrayOfString = memStr.split("\\s+");
        if (arrayOfString.length > 1) {
            result = arrayOfString[1];
        }
        return result.trim();
    }

    /**
     * 判断是否有网络
     *
     * @return true connected
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null)
            return false;

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null && (networkInfo.isConnected() || networkInfo.isAvailable());
    }

    /**
     * 获取当前接入点是否wifi
     *
     * @return 是否是wifi
     */
    public static boolean isWifiMode(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();

            if (networkInfo != null) {
                int type = networkInfo.getType();

                if (type == ConnectivityManager.TYPE_WIFI) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 字符串转为整数
     *
     * @param str          字符串
     * @param defaultValue 默认值
     * @return 成功转换则返回转换的结果，否则返回默认值
     */
    public static int parseInt(String str, int defaultValue) {
        int value = defaultValue;
        try {
            value = Integer.parseInt(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 获取屏幕宽度
     *
     * @return px宽度
     */
    public static int getScreenWidth(Activity context) {
        if (context == null) {
            return 0;
        }
        DisplayMetrics md = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(md);
        return md.widthPixels;
    }

    /**
     * 放在UI线程里面跑
     *
     * @param action 动作
     */
    public static void runInUiThread(Runnable action) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            action.run();
        } else {
            new Handler(Looper.getMainLooper()).post(action);
        }
    }


    private static final DecimalFormat mDf = new DecimalFormat("##.##");
    private static final DecimalFormat mDf3 = new DecimalFormat("##");
    private static final DecimalFormat mDf2 = new DecimalFormat("##.00");
    private static final String[] UNITS = new String[]{"G", "M", "K", "B"};
    private static final long[] DIVIDERS = new long[]{1024 * 1024 * 1024, 1024 * 1024, 1024, 1};
    private static final String[] COUNT = new String[]{"", "", "K", "B"};
    private static final String[] SPEED_UNITS = new String[]{"GB/s", "MB/s", "KB/s", "B/s"};

    /**
     * 小数点最后面出现0，不显示
     *
     * @param value
     * @return
     */
    public static String byteToString(final long value) {
        if (value < 1)
            return "0B";
        String result = null;
        for (int i = 0; i < DIVIDERS.length; i++) {
            final long divider = DIVIDERS[i];
            if (value >= divider) {

                if (i == 2) {
                    result = format(value, DIVIDERS[1], UNITS[1]);
                } else {
                    result = format(value, divider, UNITS[i]);
                }
                break;
            }
        }
        return result;
    }

    private static String format(final long value, final long divider, final String unit) {
        double result = divider > 1 ? (double) value / (double) divider : (double) value;
        // 特殊处理下 保留2位 太小的则规避为2位
        if (result <= 0.01d) {
            result = 0.01d;
        }
        return mDf.format(result) + unit;
    }

    public static String byteToString4Speed(long value) {
        if (value < 1)
            return "0B/s";
        String result = null;
        for (int i = 0; i < DIVIDERS.length; i++) {
            final long divider = DIVIDERS[i];
            if (value >= divider) {
                result = format(value, DIVIDERS[1], SPEED_UNITS[1]);
                break;
            }
        }
        return result;
    }

    private static final long[] TIME_DIVIDERS = new long[]{1000, 60, 60, 60, 24};
    private static final String[] TIME_UNITS = new String[]{"毫秒", "秒", "分钟", "小时", "天"};

    /**
     * 毫秒变成时间描述语句
     *
     * @param milliseconds 毫秒
     * @return 6秒，700分钟等这种语句
     */
    public static String millisecondsToString(long milliseconds) {

        if (milliseconds <= 0) {
            return "未知";
        }

        for (int i = 0; i < TIME_DIVIDERS.length; ++i) {
            if (milliseconds < TIME_DIVIDERS[i]) {
                return String.valueOf(milliseconds) + TIME_UNITS[i];
            }

            milliseconds /= TIME_DIVIDERS[i];
        }

        return "未知";
    }

    /**
     * 将彩色图转换为灰度图
     *
     * @param img 位图
     * @return 返回转换好的位图
     */
    public static Bitmap convertGreyImg(Bitmap img) {
        int width = img.getWidth();         //获取位图的宽
        int height = img.getHeight();       //获取位图的高

        int[] pixels = new int[width * height]; //通过位图的大小创建像素点数组

        img.getPixels(pixels, 0, width, 0, 0, width, height);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];

                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);
                int alpha = grey >>> 24 == 0 ? 0 : 0xFF << 24;//这里如果是透明的图片，需要仍然保持透明

                grey = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
        }
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        result.setPixels(pixels, 0, width, 0, 0, width, height);
        return result;
    }

    // 用来将字节转换成 16 进制表示的字符
    static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 将Md5二进制数据变成16进制字符串
     *
     * @param md5 md5的二进制数据
     * @return 字符串
     */
    public static String md5ByteToString(byte[] md5) {
        String s;
        char str[] = new char[16 * 2];
        int k = 0;
        for (int i = 0; i < 16; i++) {
            byte byte0 = md5[i];
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(str);
    }
}
