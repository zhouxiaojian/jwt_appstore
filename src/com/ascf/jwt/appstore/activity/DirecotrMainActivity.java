package com.ascf.jwt.appstore.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.ascf.jwt.appstore.Constant;
import com.ascf.jwt.appstore.DirectoryAdapter;
import com.ascf.jwt.appstore.R;
import com.ascf.jwt.appstore.dirparser.Directory;
import com.ascf.jwt.appstore.dirparser.Item;

public class DirecotrMainActivity extends ListActivity {

    public static final String TAG = "DirecotrMainActivity";

    private Directory mDirectory = null;
    private DirectoryAdapter mAdapter = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        Log.d(TAG, "DirecotrMainActivity:onCreate()");
        setContentView(R.layout.all_view_layout);
        mDirectory = (Directory) i.getSerializableExtra("mydata");

        mAdapter = new DirectoryAdapter(this, mDirectory.getItems());
        this.getListView().setAdapter(mAdapter);
    }

    @Override
    public void onListItemClick(ListView list, View view, int position, long id) {
        Log.d(TAG, "click position:" + position);
        Object obj = mAdapter.getItem(position);
        if (null != obj && obj instanceof Item) {
            Item im = (Item) obj;
            String url = im.getmHttpUrl();
            Intent i = new Intent(Constant.ACTIVITY_MAIN);
            i.putExtra("url", url);
            i.putExtra("title", im.getmDisplayname());

            this.startActivity(i);
        }
    }
}
