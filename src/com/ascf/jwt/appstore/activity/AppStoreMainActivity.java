/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ascf.jwt.appstore.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView.OnItemClickListener;

import com.ascf.jwt.appstore.Adapters;
import com.ascf.jwt.appstore.R;
import com.ascf.jwt.appstore.UrlIntentListener;
import com.ascf.jwt.appstore.XmlDocumentProvider;

/**
 * This example demonstrate the creation of a simple RSS feed reader using the XML adapter syntax.
 * The different elements of the feed are extracted using an {@link XmlDocumentProvider} and are
 * binded to the different views. An {@link OnItemClickListener} is also added, which will open a
 * browser on the associated news item page.
 */
public class AppStoreMainActivity extends ListActivity {
    //private static final String FEED_URI = "http://10.186.0.239/config_server.xml";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        String url = i.getStringExtra("url");
        String title = i.getStringExtra("title");
        this.setTitle(title);
        Log.i("AppStoreMainActivity", "enter into url:");
        setContentView(R.layout.rss_feeds_list);
        setListAdapter(Adapters.loadCursorAdapter(this, R.xml.rss_feed,
                "content://xmldocument/?url=" + Uri.encode(url)));

        getListView().setOnItemClickListener(new UrlIntentListener());
    }

    @Override
    public void onStart(){
        super.onStart();
        
    }

    @Override
    public void onStop(){
        //this.unregisterReceiver(mReveiver);
        super.onStop();
    }

}
