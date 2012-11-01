package com.ascf.jwt.appstore.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ascf.jwt.appstore.Constant;

/**
 * 存储download file信息，为支持断点续传
 * @author XRFB74
 *
 */
public class DownloadFileSizeSaver {

    private static final String TAG = "DownloadFileSizeSaver";
    private JSONArray mJsonArray = null;

    private static final DownloadFileSizeSaver saver = new DownloadFileSizeSaver();

    public static DownloadFileSizeSaver getInstance(){
        return saver;
    }

    private DownloadFileSizeSaver(){
        load();
    }

    /**
     * 防止进程意外终止，写入文件的实际大小和记录的大小不一致
     * @param appname
     * @param realsize
     * @return
     */
    public synchronized void checkDownloadFileSize(String appname, long realsize) {
        // check if downloaded file size equals identified size
        long markSize = getInstance().getDownloadProgressSize(appname);
        if (markSize > 0 && markSize != realsize) {
            getInstance().putDownloadProgressSize(appname, realsize);
        }
    }

    /**
     * 保存已下载文件大小信息到 JSONArray
     * @param appname
     * @param size  已下载的文件大小
     */
    public synchronized void putDownloadProgressSize(String appname, long size) {
        try {
            int count = mJsonArray.length();
            boolean found = false;
            for (int i = 0; i < count; i++) {
                JSONObject obj = mJsonArray.optJSONObject(i);
                String apn = obj.optString(Constant.APPNAME_KEY);
                if (apn != null && apn.equals(appname)) {
                    obj.put(Constant.DOWNLOAD_SIZE_KEY, size);
                    found = true;
                    break;
                }
            }
            if (!found) {
                JSONObject json = new JSONObject();
                json.put(Constant.APPNAME_KEY, appname);
                json.put(Constant.DOWNLOAD_SIZE_KEY, size);
                mJsonArray.put(json);
            }

            persistent();
        } catch (JSONException e) {
            Log.e(TAG, "JSONException occure.", e);
        }
    }

    /**
     * 获取app下载进度，
     * >0 有效值，
     * <=0 无效值
     * @param appname
     * @return
     */
    public long getDownloadProgressSize(String appname){
        long result = 0;
        if (null != this.mJsonArray){
            int length = mJsonArray.length();
            try {
                JSONObject json = null;
                for (int i = 0; i < length; i++){
                    json = (JSONObject) mJsonArray.get(i);
                    String name = json.optString(Constant.APPNAME_KEY);
                    if (null != name && name.equals(appname)){
                        result = json.optInt(Constant.DOWNLOAD_SIZE_KEY);
                    }
                }
            }catch (JSONException e){
                Log.e(TAG, "get JSONObject error.", e);
                return result;
            }
        }
        return result;
    }

    public synchronized void delete(String appname) {
        if (mJsonArray != null) {
            int length = mJsonArray.length();
            JSONObject json = null;
            for (int i = 0; i < length; i++) {
                json = mJsonArray.optJSONObject(i);
                String name = (String) json.optString(Constant.APPNAME_KEY);
                if (null != name && name.equals(appname)) {
                    json.remove(Constant.APPNAME_KEY);
                    json.remove(Constant.DOWNLOAD_SIZE_KEY);
                    persistent();
                    return;
                }
            }

        }
    }

    private void persistent() {
        if (mJsonArray != null) {
            String str = mJsonArray.toString();
            File file = new File(Constant.DOWNLOAD_FILEINFO_SAVER);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    Log.e(TAG, "create new file", e);
                }
            }
            OutputStream outputstream = null;
            try {
                outputstream = new BufferedOutputStream(new FileOutputStream(file));
                byte[] bytes = str.getBytes();
                outputstream.write(bytes);
                outputstream.flush();
            } catch (FileNotFoundException e) {
                Log.e(TAG, "file:" + file.getAbsolutePath() + " not found.", e);
            } catch (IOException e) {
                Log.e(TAG, "write file: " + file.getAbsolutePath() + " fail.", e);
            } finally {
                try {
                    if (null != outputstream)
                        outputstream.close();
                } catch (IOException e) {
                    Log.e(TAG, "close file: " + file.getAbsolutePath()
                            + " fail.", e);
                }
            }

        }
    }

    private void load() {
        File file = new File(Constant.DOWNLOAD_FILEINFO_SAVER);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Log.e(TAG, "create new file", e);
            }
            mJsonArray = new JSONArray();
            return;
        }

        InputStream in = null;
        try {
            int size = (int) file.length();
            in = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[size];
            in.read(buffer);
            String str = new String(buffer);
            mJsonArray = new JSONArray(str);

        } catch (FileNotFoundException e) {
            Log.e(TAG, "file " + file.getAbsolutePath() + " not found.", e);
        } catch (JSONException e){
            Log.e(TAG, "loading JSONArray fail.", e);
    } catch (IOException e) {
            Log.e(TAG, "read file exception. ", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    Log.e(TAG, "", e);
                }
            }
        }
    }

}
