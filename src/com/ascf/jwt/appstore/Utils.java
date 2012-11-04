package com.ascf.jwt.appstore;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.util.Log;

import com.ascf.jwt.appstore.action.DownloadSaver;
import com.ascf.jwt.appstore.action.InstallCompleteReceiver;
import com.ascf.jwt.appstore.action.StatusObserver;
import com.ascf.jwt.appstore.dirparser.ServiceForAccount;

public class Utils {

    private static final int IO_BUFFER_SIZE = 4 * 1024;
    private static final String LOG_TAG = "Utils";

    private Utils(){
        
    }

    public static BroadcastReceiver registeInstallReceiver(Context ctx, String pkg, StatusObserver observer) {
        InstallCompleteReceiver receiver = new InstallCompleteReceiver(pkg, observer);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(InstallCompleteReceiver.INSTALL_INTENT);
        intentFilter.addAction(InstallCompleteReceiver.UNINSTALL_INTENT);
        intentFilter.addAction(InstallCompleteReceiver.REPLACED_INTENT);
        intentFilter.addDataScheme("package");
        ctx.registerReceiver(receiver, intentFilter);
        return receiver;
    }

    public static void unRegisteInstallReceiver(Context ctx, BroadcastReceiver broadcast) {
        ctx.unregisterReceiver(broadcast);
    }

    public static void putIsDownloaded(Context ctx, String appname, boolean down){
        DownloadSaver saver = DownloadSaver.getIntance();
        saver.setContext(ctx);
        saver.putAppIsDownload(appname, down);
    }

    public static boolean getIsDownloaded(Context ctx, String appname){
        DownloadSaver saver = DownloadSaver.getIntance();
        saver.setContext(ctx);
        return saver.getAppIsDownload(appname);
    }

    public static String getApkPathByApkName(String apname){
        return Constant.DOWNLOAD_FILE_DIR + apname + Constant.APK_SUFFIX;
    }

    public static void install(Context ctx, String filepath){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(filepath)),
                "application/vnd.android.package-archive");
        ctx.startActivity(intent);
    }

    public static void uninstall(Context ctx, String packagename){
        Uri packageURI = Uri.parse("package:" + packagename);     
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);     
        ctx.startActivity(uninstallIntent); 
    }

    public static String getServerInfo(Context ctx, String tag){
        ServiceForAccount act = ServiceForAccount.getIntance();
        act.setContext(ctx);
        if (Constant.KEY_IP.equals(tag)){
            return act.getServerIP();
        }else if (Constant.KEY_PORT.equals(tag)){
            return act.getServerPort();
        }
        return "";
    }

    public static InputStream getInputStreamFromServer(String url) {
        final AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
        HttpGet getRequest = null;
        getRequest = new HttpGet(url);
        try {
            HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.w("ImageDownloader", "Error " + statusCode
                        + " while retrieving bitmap from " + url);
                return null;
            }
            final HttpEntity entity = response.getEntity();
            if (client != null) client.close();
            if (entity != null) {
                return entity.getContent();
            }
        } catch (IOException e) {
            if (client != null) client.close();
            Log.e(LOG_TAG + "getInputStreamFromServer", " fail.");
        }
        return null;
    }

    public static byte[] downloadFromServer2Buffer(String url){
        AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
        HttpGet getRequest = null;
        ByteArrayOutputStream dataStream = null;
        try {
            getRequest = new HttpGet(url);

            HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.w("ImageDownloader", "Error " + statusCode
                        + " while retrieving bitmap from " + url);
                return null;
            }
            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                OutputStream outputStream = null;
                try {
                    inputStream = entity.getContent();
                    dataStream = new ByteArrayOutputStream();
                    outputStream = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
                    copy(inputStream, outputStream);
                    outputStream.flush();
                    return dataStream.toByteArray();
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (outputStream != null) {
                        outputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (IOException e) {
            if (getRequest != null)getRequest.abort();
            Log.w(LOG_TAG + "downloadFromServer2Buffer", "I/O error while retrieving bitmap from " + url, e);
        } catch (IllegalStateException e) {
            if (getRequest != null)getRequest.abort();
            Log.w(LOG_TAG + "downloadFromServer2Buffer", "Incorrect URL: " + url);
        } catch (Exception e) {
            if (getRequest != null) getRequest.abort();
            Log.w(LOG_TAG + "downloadFromServer2Buffer", "Error while retrieving bitmap from " + url, e);
        } finally {
            if (client != null) {
                client.close();
            }
        }
        if (dataStream != null){
            return dataStream.toByteArray();
        }
        return null;
    }

    private static void copy(InputStream in, OutputStream out)
            throws IOException {
        byte[] b = new byte[IO_BUFFER_SIZE];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }
}
