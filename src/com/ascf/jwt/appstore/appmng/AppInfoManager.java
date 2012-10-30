package com.ascf.jwt.appstore.appmng;

/** 提供app查询服务，根据PackageName查询ApplicationInfo */
import java.io.File;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.ascf.jwt.appstore.Constant;
import com.ascf.jwt.appstore.R;
import com.ascf.jwt.appstore.action.DownloadFileSizeSaver;

public class AppInfoManager {

    private final static String TAG = "AppInfoManager";
    private PackageManager mPm = null;
    private Context mContext = null;
    private static AppInfoManager mng = new AppInfoManager();

    public static AppInfoManager getInstance (){
        return mng;
    }

    public void setContext(Context ctx){
        mPm = ctx.getPackageManager();
        this.mContext = ctx;
    }

    public boolean apkisInstall(String pkg){
        try {
            PackageInfo info = mPm.getPackageInfo(pkg, PackageManager.GET_UNINSTALLED_PACKAGES);
            if (info != null){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    /**
     * 查询app是否安装 当前版本
     * @param appname
     * @param pkg
     * @return
     */
    public int queryAppStatus(String appname, String pkg, int version_code) {
        int result = Constant.STATUS_UNKNOWN;
        if (null == appname || appname.equals("")) {
            return result;
        }
        boolean isInstall = false;

        try {
            PackageInfo info = mPm.getPackageInfo(pkg, PackageManager.GET_UNINSTALLED_PACKAGES);
            if (info != null){
                isInstall = true;
                Log.i(TAG, "installed app " + info.packageName
                        + ", version_code: " + info.versionCode
                        + ", version_name:" + info.versionName);
                if (info.versionCode < version_code){
                    result = Constant.STATUS_NEEDUPDATE;
                }else {
                    result = Constant.STATUS_INSTALLED;
                }
            }
        } catch (PackageManager.NameNotFoundException es) {
            isInstall = false;
            Log.e(TAG, "package name not found.", es);
        } catch (Exception e) {
            isInstall = false;
            Log.e(TAG, "query app exception.", e);
        }

        if (!isInstall ){
            result = getApkIsDownload(appname);
        }

        return result;
    }

    /***
     * 
     * @param appname
     * @return
     */
    private int getApkIsDownload(String appname){
        String filepath = Constant.DOWNLOAD_FILE_DIR + appname + Constant.APK_SUFFIX;
        int result = Constant.STATUS_UNKNOWN;
        File file = new File(filepath);
        if (file.exists()){
            // TODO 文件已经下载了，需要继续check是否完全下载了
            DownloadFileSizeSaver saver = DownloadFileSizeSaver.getInstance();
            long downloadsize = saver.getDownloadProgressSize(appname);
            if (downloadsize == 0){
                // downloaded
                result = Constant.STATUS_UNINSTALLED;
            }else {
                // download not complete
                result = Constant.STATUS_DOWNLOADED_UNCOMPLETED;
            }
        }else {
            // undownload
            result = Constant.STATUS_NOTDOWNLOAD;
        }
        return result;
    }

    public static String type2String(int type){
        String resultT = "";
        
        switch (type){
        case Constant.STATUS_DOWNLOADED_UNCOMPLETED:
        case Constant.STATUS_NOTDOWNLOAD:
            resultT = "download";
            break;
        case Constant.STATUS_INSTALLED:
            resultT = "installed";
            break;
        case Constant.STATUS_NEEDUPDATE:
            resultT = "update";
            break;
        case Constant.STATUS_UNINSTALLED:
            resultT = "not installed";
            break;
        }
        return resultT;
    }

    /**
     * 获取已安装 app icon
     * @param pkg
     * @return
     */
    public Drawable getAppIcon(String pkg){
        try {
            Drawable draw = mPm.getApplicationIcon(pkg);

            return draw;
        }catch (NameNotFoundException e){
            Log.e(TAG, "package:" + pkg + " not found.", e);
            return mContext.getResources().getDrawable(R.drawable.ic_default_picture);
        }
    }

    public static PackageInfo getPackageInfoByApkFile(Context ctx,
            String apkname) {
        String apk_path = Constant.DOWNLOAD_FILE_DIR + apkname;
        PackageManager pm = ctx.getPackageManager();
        PackageInfo pkgInfo = pm.getPackageArchiveInfo(apk_path,
                PackageManager.GET_ACTIVITIES);
        return pkgInfo;
    }

    private AppInfoManager() {

    }
}

class AppSimpleInfo {
    public String pkgname;
    public boolean isInstalled;
    public boolean isDownloaded;
    public int versionCode;
}