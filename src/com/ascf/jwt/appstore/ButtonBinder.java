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
import com.ascf.jwt.appstore.action.StatusObserver;
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
            String appNm = cursor.getString(cursor.getColumnIndex(Constant.COLUMNS_APPNAME));
            String apkUrl = cursor.getString(cursor.getColumnIndex(Constant.COLUMNS_APKURL));
            apkUrl = apkUrl.replace(Constant.KEY_IP,
                    Utils.getServerInfo(mContext, Constant.KEY_IP)).replace(
                    Constant.KEY_PORT,
                    Utils.getServerInfo(mContext, Constant.KEY_PORT));
            String pkgNm = cursor.getString(cursor.getColumnIndex(Constant.COLUMNS_PKGNAME));
            String versionC = cursor.getString(cursor.getColumnIndex(Constant.COLUMNS_VERSION));
            Log.i("ButtonBinder", "appname:" + appNm + ",package name:" + pkgNm + ",apkURL:" + apkUrl + ", version code:" + versionC);
            mMap.put(Constant.COLUMNS_APPNAME, appNm);
            mMap.put(Constant.COLUMNS_APKURL, apkUrl);
            mMap.put(Constant.COLUMNS_PKGNAME, pkgNm);
            mMap.put(Constant.COLUMNS_VERSION, versionC);
            AppInfoManager mng = AppInfoManager.getInstance();
            mng.setContext(this.mContext);
            final int mStatusType = mng.queryAppStatus(appNm, pkgNm, Integer.parseInt(versionC));
            MyObserver myObserver = new MyObserver(mContext, mBtn, mStatusType);
            myObserver.setData(mMap);

            Utils.registeInstallReceiver(mContext, pkgNm, myObserver);
            mBtn.setTag(myObserver);
            mBtn.setOnClickListener(new OnClickListener() {
                protected IClickBtnAction mdownload;
                protected MyObserver mObserv;
                //@Override
                public void onClick(View v) {
                    mObserv = (MyObserver) mBtn.getTag();
                    Map<String, String> map = mObserv.getData();
                    switch (mObserv.getStatusType()) {
                    case Constant.STATUS_INSTALLED:
                        String pkgName = map.get(Constant.COLUMNS_PKGNAME);
                        Utils.uninstall(mContext, pkgName);
                        break;
                    case Constant.STATUS_DOWNLOAD_DOWNLOADING:
                        // TODO pause to dowoload
                        try {
                            if (mdownload != null && mdownload.cancelit()) {
                                Log.i("JIBANGGUO:Pause download", " successfully.");
                                mObserv.setStsType(Constant.STATUS_DOWNLOAD_PAUSE);
                                mObserv.updateBtn();
                            }
                        }catch (Exception e){
                            Log.e("JIBANGGUO:Pause download", " error.", e);
                        }

                        break;
                   case Constant.STATUS_NEEDUPDATE:
                   case Constant.STATUS_NOTDOWNLOAD:
                   case Constant.STATUS_DOWNLOAD_PAUSE:
                   case Constant.STATUS_DOWNLOADED_UNCOMPLETED:
                        // start download...
                        String appname = map.get(Constant.COLUMNS_APPNAME);
                        String urlpath = map.get(Constant.COLUMNS_APKURL);
                        Log.i("JIBANGGUO:click URL:", map.get(Constant.COLUMNS_APKURL) + ",app_name=" + appname + ",urlpath=" + urlpath);
                        mdownload = new DownloadFileAction(mContext, appname, urlpath, mObserv);
                        mdownload.doit();
                        mBtn.setEnabled(false);
                        break;
                   case Constant.STATUS_UNINSTALLED:
                        Utils.install(mContext, Utils.getApkPathByApkName(map.get(Constant.COLUMNS_APPNAME)));
                        break;
                    }
                }
            });
            return true;
        }
        return false;
    }
}

class MyObserver implements StatusObserver {
    private Button myBtn;
    private Context mCtx;
    private int myStatus = Constant.STATUS_UNKNOWN;
    private Map<String, String> myData;

    public void setStsType(int type) {
        this.myStatus = type;
    }

    public int getStatusType() {
        return myStatus;
    }

    public void setData(Map<String, String> map) {
        this.myData = map;
    }

    public void setMyBtn(Button btn) {
        this.myBtn = btn;
    }

    public Map<String, String> getData() {
        return this.myData;
    }

    public MyObserver(Context ctx, Button btn, int type) {
        this.myBtn = btn;
        this.mCtx = ctx;
        this.myStatus = type;
        updateBtn();
    }

    // @Override
    public void setProgressValue(int value) {
        // TODO Auto-generated method stub
        myBtn.setText(value + "%");
        myStatus = Constant.STATUS_DOWNLOAD_DOWNLOADING;
        if (value == 100) {
            myStatus = Constant.STATUS_UNINSTALLED;
            myBtn.setEnabled(true);
            updateBtn();
        }
        //Log.i("ButtonBinder", "downloaded " + value + "%");
    }

    public void updateBtn() {
        myBtn.setText(AppInfoManager.type2String(mCtx, myStatus));
    }

    public void setBtnStatus(int type) {
        // TODO Auto-generated method stub
        myStatus = type;
        myBtn.setEnabled(true);
        updateBtn();
    }
}
