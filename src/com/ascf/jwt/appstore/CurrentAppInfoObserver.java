package com.ascf.jwt.appstore;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascf.jwt.appstore.action.InstallCompleteReceiver;
import com.ascf.jwt.appstore.appmng.AppInfoManager;

/** 监听当前系统里安装的app version\icon 的变化
 * 
 * */

public class CurrentAppInfoObserver {
    private BroadcastReceiver mBroadcastReveiver = null;
    private Context mCtx;
    private String mPkg;
    private String mAppNm;
    private List<View> mViews;

    public CurrentAppInfoObserver (Context ctx, String appname, String pkg, List<View> views){
        this.mCtx = ctx;
        this.mPkg = pkg;
        this.mAppNm = appname;
        this.mViews = views;
        init();
    }

    private void init(){
        mBroadcastReveiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO Auto-generated method stub
                Log.i("AppStoreMainActivity", " RECEIVER:" + intent.getDataString());
                String action = intent.getAction();
                String dataStr = intent.getDataString() == null ? "" : intent.getDataString();
                if (!dataStr.contains(mPkg)){
                    return;
                }
                if (InstallCompleteReceiver.INSTALL_INTENT.equals(action)
                        || InstallCompleteReceiver.REPLACED_INTENT.equals(action)) {
                    if (null != mViews && mViews.size() > 0) {
                        TextView textView = (TextView) mViews.get(0);
                        ImageView imageView = (ImageView) mViews.get(1);
                        if (textView != null)
                            textView.setText(AppInfoManager.getInstance().getAppVersionName(mPkg));
                        if (imageView != null) {
                            Drawable draw = AppInfoManager.getInstance().getAppIcon(mPkg);
                            if (null != draw) {
                                imageView.setImageDrawable(draw);
                            }
                        }
                    }
                } else if (InstallCompleteReceiver.UNINSTALL_INTENT.equals(action)) {
                    if (null != mViews && mViews.size() > 0) {
                        TextView textView = (TextView) mViews.get(0);
                        ImageView imageView = (ImageView) mViews.get(1);
                        if (textView != null)
                            textView.setText(AppInfoManager.getInstance().getAppVersionName(mPkg));

                        if (imageView != null) {
                            PackageInfo packInfo = AppInfoManager.getPackageInfoByApkFile(mCtx, mAppNm);
                            if (null != packInfo) {
                                ApplicationInfo appInfo = packInfo.applicationInfo;     
                                Drawable icon = mCtx.getPackageManager().getApplicationIcon(appInfo);  
                                imageView.setImageDrawable(icon);
                            }
                        }
                    }
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(InstallCompleteReceiver.INSTALL_INTENT);
        filter.addAction(InstallCompleteReceiver.REPLACED_INTENT);
        filter.addAction(InstallCompleteReceiver.UNINSTALL_INTENT);
        filter.addDataScheme("package");
        mCtx.registerReceiver(mBroadcastReveiver, filter);
        AppInfoManager.getInstance().setContext(this.mCtx);
        View view = mViews.get(0);
        if (view instanceof TextView){
            ((TextView) view).setText(AppInfoManager.getInstance().getAppVersionName(mPkg));
        }
    }
}
