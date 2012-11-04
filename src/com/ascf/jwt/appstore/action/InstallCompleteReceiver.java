package com.ascf.jwt.appstore.action;

import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ascf.jwt.appstore.Constant;
import com.ascf.jwt.appstore.appmng.AppInfoManager;

public class InstallCompleteReceiver extends BroadcastReceiver {

    public static final String INSTALL_INTENT = "android.intent.action.PACKAGE_ADDED";
    public static final String UNINSTALL_INTENT= "android.intent.action.PACKAGE_REMOVED";
    public static final String REPLACED_INTENT = "android.intent.action.PACKAGE_REPLACED";
    private StatusObserver mStatusObserver= null;
    private String mPkgNm = "";

    public InstallCompleteReceiver(String pkgname, StatusObserver stsObs){
        this.mStatusObserver = stsObs;
        this.mPkgNm = pkgname;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // 接收安装广播
        if (intent.getAction().equals(INSTALL_INTENT) || intent.getAction().equals(REPLACED_INTENT)) {
            String packageName = intent.getDataString();
            packageName = packageName == null ? "" : packageName;
            if (packageName.contains(mPkgNm)){
                Map<String, String> data = mStatusObserver.getData();
                int version_code = Integer.parseInt(data.get(Constant.COLUMNS_VERSION));
                String appname = data.get(Constant.COLUMNS_APPNAME);
                int type = AppInfoManager.getInstance().queryAppStatus(appname, mPkgNm, version_code);
                mStatusObserver.setBtnStatus(type);
            }
            Log.i("receive broadcast : install app ", packageName);
        }
        // 接收卸载广播
        if (intent.getAction().equals(UNINSTALL_INTENT)) {
            String packageName = intent.getDataString();
            packageName = packageName == null ? "" : packageName;
            if (packageName.contains(mPkgNm)){
                mStatusObserver.setBtnStatus(Constant.STATUS_UNINSTALLED);
            }
            Log.i("receive broadcast : uninstall app ", packageName);
        }
    }
}
