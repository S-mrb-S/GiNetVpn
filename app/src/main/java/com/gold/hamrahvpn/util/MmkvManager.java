package com.gold.hamrahvpn.util;

import static com.gold.hamrahvpn.Data.ID_PREF_USAGE;
import static com.gold.hamrahvpn.Data.ID_app_details;
import static com.gold.hamrahvpn.Data.ID_app_values;
import static com.gold.hamrahvpn.Data.ID_connection_data;
import static com.gold.hamrahvpn.Data.ID_log_data;
import static com.gold.hamrahvpn.Data.ID_settings_data;

import com.tencent.mmkv.MMKV;

public class MmkvManager {

    private static MMKV connectionStorage;
    private static MMKV settingsStorage;
    private static MMKV logStorage;
    private static MMKV appValStorage;
    private static MMKV PREFUSAGEStorage;
    private static MMKV appDetailsStorage;

    public static synchronized MMKV getConnectionStorage() {
        if (connectionStorage == null) {
            connectionStorage = MMKV.mmkvWithID(ID_connection_data, MMKV.MULTI_PROCESS_MODE);
        }
        return connectionStorage;
    }

    public static synchronized MMKV getSettingsStorage() {
        if (settingsStorage == null) {
            settingsStorage = MMKV.mmkvWithID(ID_settings_data, MMKV.MULTI_PROCESS_MODE);
        }
        return settingsStorage;
    }

    public static synchronized MMKV getLogStorage() {
        if (logStorage == null) {
            logStorage = MMKV.mmkvWithID(ID_log_data, MMKV.MULTI_PROCESS_MODE);
        }
        return logStorage;
    }

    public static synchronized MMKV getAppValStorage() {
        if (appValStorage == null) {
            appValStorage = MMKV.mmkvWithID(ID_app_values, MMKV.MULTI_PROCESS_MODE);
        }
        return appValStorage;
    }

    // Daily Usage
    public static synchronized MMKV getDUStorage() {
        if (PREFUSAGEStorage == null) {
            PREFUSAGEStorage = MMKV.mmkvWithID(ID_PREF_USAGE, MMKV.MULTI_PROCESS_MODE);
        }
        return PREFUSAGEStorage;
    }

    // App details
    public static synchronized MMKV getADStorage() {
        if (appDetailsStorage == null) {
            appDetailsStorage = MMKV.mmkvWithID(ID_app_details, MMKV.MULTI_PROCESS_MODE);
        }
        return appDetailsStorage;
    }
}
