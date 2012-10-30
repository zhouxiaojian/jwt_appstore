package com.ascf.jwt.appstore.action;

import java.io.File;

import android.os.AsyncTask;

public class InstallAction implements IClickBtnAction {

    @Override
    public void doit() {
        // TODO Auto-generated method stub
        
    }

}



class InstallTask extends AsyncTask<File, Void, Boolean> {

    @Override
    protected Boolean doInBackground(File... params) {
        return null;
    }

    @Override
    protected void onPostExecute(Boolean result) {

    }
}