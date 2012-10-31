package com.ascf.jwt.appstore;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ascf.jwt.appstore.action.DownloadFileAction;
import com.ascf.jwt.appstore.action.IClickBtnAction;
import com.ascf.jwt.appstore.action.IDownloadObserver;
import com.ascf.jwt.appstore.appmng.AppInfoManager;

public class ButtonBinder extends Adapters.CursorBinder {

  
    public ButtonBinder(Context context, Adapters.CursorTransformation transformation) {
        super(context, transformation);
        Log.i("JIBANGGUO:ButtonBinder:", "created...");
    }

    @Override
    public boolean bind(View view, final Cursor cursor, final int columnIndex) {
        if (view instanceof Button) {
            final Button mBtn = (Button) view;
            Map<String, String> mMap = new HashMap<String, String>();
            //final String url = mTransformation.transform(cursor, columnIndex);
            final String appNm = cursor.getString(cursor.getColumnIndex(Constant.COLUMNS_APPNAME));
            final String apkUrl = cursor.getString(cursor.getColumnIndex(Constant.COLUMNS_APKURL));
            final String pkgNm = cursor.getString(cursor.getColumnIndex(Constant.COLUMNS_PKGNAME));
            final String versionC = cursor.getString(cursor.getColumnIndex(Constant.COLUMNS_VERSION));
            Log.i("ButtonBinder", "appname:" + appNm + ",package name:" + pkgNm + ",apkURL:" + apkUrl + ", version code:" + versionC);
            mMap.put(Constant.COLUMNS_APPNAME, appNm);
            mMap.put(Constant.COLUMNS_APKURL, apkUrl);
            mMap.put(Constant.COLUMNS_PKGNAME, pkgNm);
            AppInfoManager mng = AppInfoManager.getInstance();
            mng.setContext(this.mContext);
            final int mStatusType = mng.queryAppStatus(appNm, pkgNm, Integer.parseInt(versionC));
            final MyObserver myObserver = new MyObserver(mBtn, mStatusType);
            myObserver.setData(mMap);

            mBtn.setTag(myObserver);
            mBtn.setOnClickListener(new OnClickListener() {

                //@Override
                public void onClick(View v) {
                    switch (mStatusType) {
                    case Constant.STATUS_INSTALLED:
                        break;
                    case Constant.STATUS_DOWNLOAD_DOWNLOADING:
                        // TODO pause
                        break;
                    case Constant.STATUS_NEEDUPDATE:
                    case Constant.STATUS_NOTDOWNLOAD:
                    case Constant.STATUS_DOWNLOADED_UNCOMPLETED:
                        MyObserver observ = (MyObserver) mBtn.getTag();
                        Map<String, String> map = observ.getData();
                        // start download...
                        String appname = map.get(Constant.COLUMNS_APPNAME);
                        String urlpath = map.get(Constant.COLUMNS_APKURL);
                        Log.i("JIBANGGUO:click URL:", map.get(Constant.COLUMNS_APKURL) + ",app_name=" + appname + ",urlpath=" + urlpath);
                        IClickBtnAction download = new DownloadFileAction(mContext, appname, urlpath, myObserver);
                        download.doit();
                        break;
                    case Constant.STATUS_UNINSTALLED:

                        break;
                    }
                }
            });
            return true;
        }
        return false;
    }
}

class MyObserver implements IDownloadObserver {
    private Button myBtn;
    private int myStatus = Constant.STATUS_UNKNOWN;
    private Map<String, String> myData;
    public void setStsType(int type) {
        this.myStatus = type;
    }

    public void setData(Map<String, String> map){
        this.myData = map;
    }

    public void setMyBtn(Button btn) {
        this.myBtn = btn;
    }

    public Map<String, String> getData(){
        return this.myData;
    }

    public MyObserver(Button btn, int type) {
        this.myBtn = btn;
        this.myStatus = type;
        updateBtn();
    }

    //@Override
    public void setProgressValue(int value) {
        // TODO Auto-generated method stub
        myBtn.setText(value + "%");
        if (value == 100) {
            myStatus = Constant.STATUS_UNINSTALLED;
            updateBtn();
        }
        Log.i("ButtonBinder", "downloaded " + value + "%");

    }

    private void updateBtn() {
        myBtn.setText(AppInfoManager.type2String(myStatus));
    }
}
