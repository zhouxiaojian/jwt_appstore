package com.ascf.jwt.appstore.dirparser;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ascf.jwt.appstore.Constant;
import com.ascf.jwt.appstore.Utils;

public class AccountProvider {

	private Map<String, Account> mAccounts = new HashMap<String, Account>();

	public AccountProvider(String url){
		loadFromXML(url);
	}

	/**
	 * if check pass, return user level else return error code(-1)
	 * @param name
	 * @param pwd
	 * @return
	 */
	public int queryAuth(String name, String pwd){ 

		int result = -1;
		if (name != null && !name.equals("")){
			Account account = mAccounts.get(name);
			if (null != account){
				if (account.pwd.equals(pwd)){
					result = account.level;
				}
			}
		}
		return result;
	}

	private void loadFromXML(String url) {
		byte[] bytes = Utils.downloadFromServer2Buffer(url);
		try {
			JSONArray jsonArray = new JSONArray(new String(bytes));
			if (jsonArray != null) {
				int count = jsonArray.length();
				for (int i = 0; i < count; i++) {
					Account act = new Account();
					JSONObject json = jsonArray.optJSONObject(i);
					act.name = json.optString(Constant.KEY_ACCOUNT_NAME);
					act.level = json.optInt(Constant.KEY_ACCOUNT_LEVEL);
					act.pwd = json.optString(Constant.KEY_ACCOUNT_PWD);
					act.rscUrl = json.optString(Constant.KEY_ACCOUNT_APPLIST);
					mAccounts.put(act.name, act);
				}
			}
		} catch (JSONException e) {
			Log.e("AccountProvider", "reading from JSON file.", e);
		}
	
	}
}

class Account {
	protected String name;
	protected String pwd;
	protected int level;
	protected String rscUrl;
}
