package com.ascf.jwt.appstore;

import android.os.Environment;

public class Constant {

	public final static String DEFAULT_IP = "10.0.12.1";
	public final static String DEFAULT_PORT = "80";

    /** installed, don't need to update. */
    public final static int STATUS_INSTALLED = 1;

    /** downloaded, not installed */
    public final static int STATUS_UNINSTALLED = 2;

    /** installed, but need to update*/
    public final static int STATUS_NEEDUPDATE = 3;

    /** not download */
    public final static  int STATUS_NOTDOWNLOAD = 4;

    /** downloaded, but not completed*/
    public final static int STATUS_DOWNLOADED_UNCOMPLETED = 5;
    
    public final static int STATUS_DOWNLOAD_DOWNLOADING = 6;

    public final static  int STATUS_UNKNOWN = 0;

    public final static String APPNAME_KEY = "app_name";

    public final static String DOWNLOAD_SIZE_KEY = "download_size";

    public static final int DOWNLOAD_BUFFER_SIZE = 1024 * 5;

    public static final int DOWNLOAD_OUTPUT_BUFFER_SIZE = 1024 * 5 + 2;

    /***
     * 1:已经下载完全
     * 2：已开始下载，但是未完成
     * 0：未开始下载
     */
    public final static int DOWNLOAD_STATUS_DOWNLOADED = 1;
    public final static int DOWNLOAD_STATUS_UNCOMPLETED = 2;
    public final static int DOWNLOAD_STATUS_UNDOWNLOAD = 0;

    public final static String COLUMNS_APPID = "/app_id";
    public final static String COLUMNS_TITLE = "/title";
    public final static String COLUMNS_APPNAME = "/app_name";
    public final static String COLUMNS_ICONNAME = "/icon_image";
    public final static String COLUMNS_APKURL = "/apkurl";
    public final static String COLUMNS_VERSION = "/current_version";
    public final static String COLUMNS_PUBDATE = "/pubDate";
    public final static String COLUMNS_PKGNAME = "/package_name";
    public final static String COLUMNS_DSCP = "/description";

    public static final String DOWNLOAD_HOME = Environment.getExternalStorageDirectory() + "/ascf";
    public static final String DOWNLOAD_FILEINFO_SAVER =  DOWNLOAD_HOME + "/breakfile.txt";
    public static final String DOWNLOAD_FILE_DIR = DOWNLOAD_HOME + "/apps/";
    public static final String APK_SUFFIX = ".apk";

    
    /** for Account xml JSON**/

	public static final String KEY_ACCOUNT_NAME = "name";
	public static final String KEY_ACCOUNT_PWD = "pwd";
	public static final String KEY_ACCOUNT_LEVEL = "level";
	public static final String KEY_ACCOUNT_APPLIST = "applist";

	public static final int USER_CHECK_NOPASS = -1;

    private Constant(){
        
    }

}
