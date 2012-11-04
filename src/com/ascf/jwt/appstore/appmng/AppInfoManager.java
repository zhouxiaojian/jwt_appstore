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
import com.ascf.jwt.appstore.Utils;

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

    public String getAppVersionName(String pkg){
        String result = mContext.getResources().getString( R.string.current_ver_tag)
                + mContext.getResources().getString(R.string.no_install_tag);
        try {
            PackageInfo info = mPm.getPackageInfo(pkg, PackageManager.GET_UNINSTALLED_PACKAGES);
            if (info != null){
                result = mContext.getResources().getString(
                        R.string.current_ver_tag) + info.versionName;
           }
        }catch (Exception e){
            return result;
        }
        return result;
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
            Log.e(TAG, "package name not found.");
        } catch (Exception e) {
            isInstall = false;
            Log.e(TAG, "query app exception.");
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
    private int getApkIsDownload(String appname) {
        String filepath = Constant.DOWNLOAD_FILE_DIR + appname
                + Constant.APK_SUFFIX;
        int result = Constant.STATUS_UNKNOWN;
        File file = new File(filepath);
        boolean downloaded = Utils.getIsDownloaded(mContext, appname);
        if (file.exists() && downloaded) {
            result = Constant.STATUS_UNINSTALLED;
            // // TODO 文件已经下载了，需要继续check是否完全下载了
            // DownloadFileSizeSaver saver =
            // DownloadFileSizeSaver.getInstance();
            // long downloadsize = saver.getDownloadProgressSize(appname);
            // if (downloaded == 0){
            // // downloaded
            // result = Constant.STATUS_UNINSTALLED;
            // }else {
            // // download not complete
            // result = Constant.STATUS_DOWNLOADED_UNCOMPLETED;
            // }
            // if (downloaded){
            // }
        } else {
            // undownload
            result = Constant.STATUS_NOTDOWNLOAD;
        }
        return result;
    }

    public static String type2String(Context ctx, int type){
        String resultT = "";
        switch (type){
        case Constant.STATUS_NOTDOWNLOAD:
            resultT = ctx.getResources().getString(R.string.status_download);
            break;
        case Constant.STATUS_INSTALLED:
            resultT = ctx.getResources().getString(R.string.status_installed);
            break;
        case Constant.STATUS_NEEDUPDATE:
            resultT = ctx.getResources().getString(R.string.status_update);
            break;
        case Constant.STATUS_DOWNLOADED_UNCOMPLETED:
        case Constant.STATUS_DOWNLOAD_PAUSE:
            resultT = ctx.getResources().getString(R.string.status_pause);
        case Constant.STATUS_UNINSTALLED:
            resultT = ctx.getResources().getString(R.string.status_notinstall);
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
            //return mContext.getResources().getDrawable(R.drawable.ic_default_picture);
            return null;
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