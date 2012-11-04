package com.ascf.jwt.appstore.action;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DownloadSaver {

    private static SharedPreferences mSharedPre = null;
    private static DownloadSaver service = new DownloadSaver();

    public static DownloadSaver getIntance(){
        return service;
    }

    public void setContext(Context ctx){
        if (mSharedPre == null){
            mSharedPre = PreferenceManager.getDefaultSharedPreferences(ctx);
        }
    }

    public boolean putAppIsDownload(String appname, boolean down){
        SharedPreferences.Editor editor = mSharedPre.edit();
        editor.putBoolean(appname, down);
        return editor.commit();
    }

    public boolean getAppIsDownload (String appname){
        return mSharedPre.getBoolean(appname, false);
    }

    private DownloadSaver() {
        
    }

}
