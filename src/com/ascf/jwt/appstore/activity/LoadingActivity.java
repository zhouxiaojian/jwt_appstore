package com.ascf.jwt.appstore.activity;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ascf.jwt.appstore.Constant;
import com.ascf.jwt.appstore.R;
import com.ascf.jwt.appstore.Utils;
import com.ascf.jwt.appstore.dirparser.AccountProvider;
import com.ascf.jwt.appstore.dirparser.DirXMLParse;
import com.ascf.jwt.appstore.dirparser.Directory;
import com.ascf.jwt.appstore.dirparser.INotifyComplete;
import com.ascf.jwt.appstore.dirparser.ServiceForAccount;

public class LoadingActivity extends Activity implements INotifyComplete {

    private ProgressDialog mProgressDialog;
    private AccountProvider mAccountProvider = null;
    private DirXMLParse mDirXMLParser = null;
    private int mLevel = -1;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mProgressDialog = new ProgressDialog(this);
	}

    public void go_on(Object obj) {
        if (null != obj && obj instanceof String){
            String goon = (String) obj;
            if ( goon.equals(Constant.GO_ON_AFTER_CHECKUSER)){
                go_on_checkuser();
            }else if (goon.equals(Constant.GO_ON_AFTER_PARSEDIRXML)){
                go_on_directory();
            }
        }
	}

    private void go_on_checkuser(){
        Intent intent = getIntent();
        String name = intent.getStringExtra(Constant.KEY_USERNAME);
        String pwd = intent.getStringExtra(Constant.KEY_PASSWORD);
        mLevel = mAccountProvider.queryAuth(name, pwd);
        if (mLevel == -1) {
            setResult(RESULT_CANCELED);
            mProgressDialog.setMessage(getResources().getString(R.string.check_user_fail));
            finish();
            return;
        }

        // start to load directories
        String dirctoryUrl = Constant.DIRECTORY_URL.replace(Constant.KEY_IP,
                Utils.getServerInfo(this, Constant.KEY_IP)).replace(Constant.KEY_PORT,
                Utils.getServerInfo(this, Constant.KEY_PORT));
        mDirXMLParser = new DirXMLParse(this, dirctoryUrl);
    }

    private void go_on_directory(){
        Directory dr = getDirectoryByLevel(mDirXMLParser.getDirectfromXML(), mLevel);
        Intent stIntent = new Intent(Constant.ACTIVITY_ACTION_DIRVIEW);
        mProgressDialog.setMessage(getResources().getString(R.string.load_app_data));
        stIntent.putExtra("mydata", dr);
        if (null == dr) Log.i("jibangguo:parse directory xml error", "");
        startActivity(stIntent);
        finish();
    }

    public void onStart() {
        mProgressDialog.setMessage(getResources().getString(R.string.check_user));
        mAccountProvider = new AccountProvider(this, getAccountXMLUrl());
        super.onStart();
    }

	private Directory getDirectoryByLevel(List<Directory> list, int lev) {
		if (null != list && list.size() > 0) {
			for (Directory dir : list) {
				if (dir.getLevel() == lev) {
					return dir;
				}
			}
		}
		return null;
	}

    private String getAccountXMLUrl() {
        return Constant.ACCOUNT_URL.replace(Constant.KEY_IP,
                Utils.getServerInfo(this, Constant.KEY_IP))
                .replace(Constant.KEY_PORT,
                        Utils.getServerInfo(this, Constant.KEY_PORT));
    }

}
