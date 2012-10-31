package com.ascf.jwt.appstore.dirparser;

import java.util.Map;
import java.util.Set;

import com.ascf.jwt.appstore.Constant;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ServiceForAccount {

	public final static String KEY_IP = "ip";
	public final static String KEY_PORT = "port";
	public final static String KEY_USERNAME = "username";
	public final static String KEY_PASSWORD = "password";

	private SharedPreferences mSharedPre = null;

	public ServiceForAccount(Context context) {
		mSharedPre = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public Map<String, ?> getAllAccount() {
		return mSharedPre.getAll();
	}

	public void saveAllAccount(Map<String, ?> map) {
		SharedPreferences.Editor editor = mSharedPre.edit();
		Set keySet = map.keySet();
		String[] keys = (String[]) keySet.toArray(new String[0]);
	}

	public void saveAcount(String username, String pwd) {
		SharedPreferences.Editor editor = mSharedPre.edit();
		editor.putString(username, pwd);
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
		return mSharedPre.getString(KEY_IP, Constant.DEFAULT_IP);
	}

	public String getServerPort() {
		return mSharedPre.getString(KEY_PORT, Constant.DEFAULT_PORT);
	}
}