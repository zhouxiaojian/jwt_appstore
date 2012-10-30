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

package com.ascf.jwt.appstore;

import com.ascf.jwt.appstore.appmng.AppInfoManager;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

/**
 * This CursorBinder binds the provided image URL to an ImageView by downloading the image from the
 * Internet.
 */
public class UrlImageBinder extends Adapters.CursorBinder {

    private final ImageDownloader imageDownloader;

    public UrlImageBinder(Context context, Adapters.CursorTransformation transformation) {
        super(context, transformation);
        imageDownloader = new ImageDownloader();
        Log.i("jibangg:UrlImageBinder:", "create...");
    }

    @Override
    public boolean bind(View view, Cursor cursor, int columnIndex) {
        if (view instanceof ImageView) {
            final String url = mTransformation.transform(cursor, columnIndex);
            int indx = cursor.getColumnIndex(Constant.COLUMNS_PKGNAME);
            String pkgNm = cursor.getString(indx);
            AppInfoManager.getInstance().setContext(mContext);
            if (AppInfoManager.getInstance().apkisInstall(pkgNm)){
                ((ImageView) view).setImageDrawable(AppInfoManager.getInstance().getAppIcon(pkgNm));
            }else {
                imageDownloader.download(url, (ImageView) view);
            }
            return true;
        }
        return false;
    }
}
