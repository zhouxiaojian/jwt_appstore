package com.ascf.jwt.appstore.action;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.ascf.jwt.appstore.Constant;
import com.ascf.jwt.appstore.Utils;

public class DownloadFileAction implements IClickBtnAction {

    public static final String TAG = "DownloadFileTask";
    private String mAppname = "";
    private String mUrl = "";
    private StatusObserver mCallBack;
    private Context mCtx;
    private DownloadOneAppTask mDownloadTask;

    public DownloadFileAction(Context ctx, String appname, String url, StatusObserver callback){
        this.mAppname = appname;
        this.mUrl = url;
        this.mCallBack = callback;
        this.mCtx = ctx;
    }

    //@Override
    public void doit() {
        // TODO Auto-generated method stub
        mDownloadTask = new DownloadOneAppTask(mCtx, mCallBack);
        mDownloadTask.execute(mAppname, mUrl);
    }

    public boolean cancelit() {
        // TODO Auto-generated method stub
        mDownloadTask.stop(true);
        return true;
    }
}

class DownloadOneAppTask extends AsyncTask<String, Integer, String> {

    private StatusObserver callbk = null;
    private Context mCtx = null;
    private boolean forceStop = false;

    public DownloadOneAppTask(Context ctx, StatusObserver c) {
        this.callbk = c;
        this.mCtx = ctx;
    }

    public void stop(boolean stop){
        this.forceStop = stop;
    }

    @Override
    /**
     * ( appname, url)
     */
    protected String doInBackground(String... urls) {
        String appname = urls[0];
        String urlS = urls[1];

        URL url = null;
        BufferedOutputStream output = null;
        InputStream istream = null;
        File file = null;
        long realSize = 0;
        try {
            url = new URL(urlS);
            // 打开到url的连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            istream = connection.getInputStream();
            long totalC = connection.getContentLength();
            File dir = new File(Constant.DOWNLOAD_FILE_DIR);
            if (!dir.exists()) {
                boolean f = dir.mkdirs();
                Log.i("DownloadFileTask", "Create new dir:" + f);
            }
            file = new File(Utils.getApkPathByApkName(appname));
            Log.i(DownloadFileAction.TAG, "save to file:" + file.getAbsolutePath());
            long skipSize = 0;
            //DownloadFileSizeSaver saver = DownloadFileSizeSaver.getInstance();
//            if (!file.exists() && saver.getDownloadProgressSize(appname) ==0){
//                // file not exist, also not download
//                boolean tf = file.createNewFile();
//                Log.i("", "Create new file:" + tf);
//            }else if (file.exists() && saver.getDownloadProgressSize(appname) > 0){
//                // download, but not completed.
//                realSize = file.length();
//                saver.checkDownloadFileSize(appname, realSize);
//                skipSize = saver.getDownloadProgressSize(appname);
//            }

            // Constructs a new BufferedOutputStream, providing out with size bytes of buffer.
            output = new BufferedOutputStream(new FileOutputStream(file, false), Constant.DOWNLOAD_OUTPUT_BUFFER_SIZE);
            byte[] buffer = new byte[Constant.DOWNLOAD_BUFFER_SIZE];

            Log.i(DownloadFileAction.TAG, "total size:" + totalC);
            istream.skip(skipSize);
            while (!forceStop) {
                int i = istream.read(buffer);
                if (i == -1) {
                    DownloadFileSizeSaver.getInstance().delete(appname);
                    Utils.putIsDownloaded(this.mCtx, appname, true);
                    publishProgress(100);
                    break;
                }
                output.write(buffer, 0, i);
                realSize += i;
                //DownloadFileSizeSaver.getInstance().putDownloadProgressSize(appname, realSize);
                int per = (int)((float)realSize/totalC * 100);

//                Log.i(DownloadFileAction.TAG, "downloaded size=" + realSize + ", downloaded percent:" + per);

                publishProgress(per);

            }
            output.flush();
            Log.i(DownloadFileAction.TAG, "file size=" + file.length() + ", saver size:" + realSize);
        } catch (IOException e) {
            Log.e(DownloadFileAction.TAG, "download file IO ERROR", e);
        }finally {
            try {
                if (null != output) output.close();
                if (null != istream) istream.close();
            }catch (IOException e){
                Log.e(DownloadFileAction.TAG, "close outputstream", e);
            }
        }
        if (file != null) {
            return file.getAbsolutePath();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        //update UI Progress bar
        //Log.i(DownloadFileAction.TAG, " post progress value:" + values[0]);
        this.callbk.setProgressValue(values[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        if (!forceStop){
            Log.i("start install apk", ":" + result);
            Utils.install(mCtx, result);
        }
    }

}
