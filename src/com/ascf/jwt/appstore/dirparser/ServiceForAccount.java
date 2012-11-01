package com.ascf.jwt.appstore.dirparser;

import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ascf.jwt.appstore.Constant;

public class ServiceForAccount {

	private static SharedPreferences mSharedPre = null;

	private static ServiceForAccount service = new ServiceForAccount();

	public static ServiceForAccount getIntance(){
	    return service;
	}

	public void setContext(Context ctx){
	    if (mSharedPre == null){
	        mSharedPre = PreferenceManager.getDefaultSharedPreferences(ctx);
	    }
	}
	private ServiceForAccount() {
		
	}

	public Map<String, ?> getAllAccount() {
		return mSharedPre.getAll();
	}

	public boolean saveKeyValue(String key, String value) {
		SharedPreferences.Editor editor = mSharedPre.edit();
		editor.putString(key, value);
		return editor.commit();
	}

	public String getValueByKey(String key) {
		return mSharedPre.getString(key, "");
	}

	public String getServerIP() {
		return mSharedPre.getString(Constant.KEY_IP, Constant.DEFAULT_IP);
	}

	public String getServerPort() {
		return mSharedPre.getString(Constant.KEY_PORT, Constant.DEFAULT_PORT);
	}
}