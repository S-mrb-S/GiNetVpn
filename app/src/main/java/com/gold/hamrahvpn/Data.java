package com.gold.hamrahvpn;

import android.graphics.Typeface;
import com.gold.hamrahvpn.util.MmkvManager;

import com.tencent.mmkv.MMKV;

/**
 * @MehraB832 --> github
 */
public class Data {
    static String FileUsername;
    static String FilePassword;
    // default text
    static final String default_usage_permissions_txt = "اجازه دهید برنامه همیشه در پس‌زمینه اجرا شود؟";
    static final String default_usage_permissions_backg_txt = "اجازه دادن به Hamrah VPN برای اجرای همیشه در برنامه پس‌زمینه ممکن است مصرف حافظه را کاهش دهد";
    static final String NA = "خالی";
    // splash screen (loading)
    static final String get_info_from_app = "دریافت اطلاعات برنامه";
    static final String get_details_from_file = "در حال دریافت اطلاعات اتصال";
    // (main) set connection button
    static final String disconnected_btn = "روشن شدن";
    static final String connecting_btn = "درحال اتصال";
    static final String connecting_cancel_btn = "لغو";
    static final String connected_btn = "قطع اتصال";
    static final String connected_error_btn = "برنامه های VPN را حذف کنید";
    // (main) set message text
    static final String disconnected = "اتصال قطع شد";
    static final String disconnected_txt = "اتصال اماده است";
    static final String disconnected_txt2 = "برای روشن شدن ضربه بزنید !";
    static final String connecting_txt = "در حال اتصال به";
    static final String connected_txt = "اتصال برقرار شد";
    static final String connected_error_danger_vpn_txt = "برنامه های خطرناک VPN پیدا شد";
    static final String connected_error_long_txt = "دستگاه شما در معرض خطر است، سایر برنامه های VPN را حذف کنید! برنامه های خطرناک VPN بالقوه اتصال اینترنت را مسدود می کنند";
    static final String connected_catch_txt = "اتصال امکان پذیر نیست";
    static final String connected_catch_check_internet_txt = "اینترنت خود را بررسی کنید";
    static final String update_count_txt = "استفاده شده";
    static final String default_ziro_txt = "صفر";
    // usage
    static final String KB = "کیلوبایت";
    static final String MB = "مگابایت";
    static final String default_byte_txt = default_ziro_txt + ' ' + KB;
    // usage
    static final String Version_txt = "نسخه برنامه";
    static final String device_time_txt = "نصب شده در";
    static final String minute_ago = "دقیقه اخیر";
    static final String minutes_ago = "دقیقه پیش";
    static final String hour_ago = "ساعت اخیر";
    static final String hours_ago = "ساعت های اخیر";
    static final String day_ago = "روز اخیر";
    static final String days_ago = "روز های اخیر";
    static final String seconds_ago = "ثانیه پیش";
    // mmkv id
    public static final String ID_settings_data = "settings_data";
    public static final String ID_connection_data = "connection_data";
    public static final String ID_log_data = "log_data";
    public static final String ID_app_values = "app_values";
    public static final String ID_PREF_USAGE = "daily_usage";
    public static String TODAY, WEEK, MONTH, YEAR;
    public static final String ID_app_details = "app_details";
    // key
    static final String KEY_app_details = "app_details";
    // recyclerview
    static final String KEY_GRID = "GRID";
    static boolean isAppDetails = false, isConnectionDetails = false;
    public static String StringCountDown;
    public static long LongDataUsage;

    // font
    public static Typeface RobotoMedium;
    public static Typeface RobotoRegular;
    public static Typeface RobotoBold;
    public static Typeface RobotoLight;

    // function

    // mmkv
    public static MMKV connectionStorage = MmkvManager.getConnectionStorage(),
            settingsStorage = MmkvManager.getSettingsStorage(),
            appValStorage = MmkvManager.getAppValStorage(),
            PREFUSAGEStorage = MmkvManager.getDUStorage();
}
