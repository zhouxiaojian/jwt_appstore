package com.ascf.jwt.appstore.activity;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;

import com.ascf.jwt.appstore.Constant;
import com.ascf.jwt.appstore.DirectoryAdapter;
import com.ascf.jwt.appstore.R;
import com.ascf.jwt.appstore.dirparser.Directory;
import com.ascf.jwt.appstore.dirparser.Item;

public class DirecotrMainActivity extends ListActivity implements DialogInterface.OnClickListener{

    public static final String TAG = "DirecotrMainActivity";
    private Directory mDirectory = null;
    private DirectoryAdapter mAdapter = null;
    private DialogInterface mExitDialog = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        Log.d(TAG, "DirecotrMainActivity:onCreate()");
        setContentView(R.layout.all_view_layout);
        mDirectory = (Directory) i.getSerializableExtra("mydata");

        mAdapter = new DirectoryAdapter(this, mDirectory.getItems());
        getListView().setAdapter(mAdapter);
    }

    @Override
    public void onListItemClick(ListView list, View view, int position, long id) {
        Log.d(TAG, "click position:" + position);
        Object obj = mAdapter.getItem(position);
        if (null != obj && obj instanceof Item) {
            Item im = (Item) obj;
            if (im.ismIsEnable()){
                String url = im.getmHttpUrl();
                Intent i = new Intent(Constant.ACTIVITY_MAIN);
                i.putExtra("url", url);
                i.putExtra("title", im.getmDisplayname());
                startActivity(i);
            }else {
                return;
            }
        }
    }

    @Override
    // Handle BACK key to pop-up dialog for exiting
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            new Runnable(){
                public void run() {
                    showExitDialog();
                }
            }.run();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showExitDialog() {
        mExitDialog = new AlertDialog.Builder(this)
            .setTitle("警告")
            .setIcon(null)
            .setMessage("是否退出登录？")
            .setPositiveButton("确认", this)
            .setNegativeButton("取消" ,null)
            .show();
    }

    public void onClick(DialogInterface dialog, int which) {
        if(mExitDialog == dialog){
            if( which == DialogInterface.BUTTON_POSITIVE){
                // exit the whole app
                android.os.Process.killProcess(android.os.Process.myPid());
            }else{
                return;
            }
        }
    }
}
