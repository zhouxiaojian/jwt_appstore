package com.ascf.jwt.appstore;

import com.ascf.jwt.appstore.dirparser.ServiceForAccount;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class LoadingActivity extends Activity {

	private String mUrl = "http://ip/Account.xml";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	public void onStart(){
		
	}

	private void startBusi(){
		Intent intent = getIntent();
		String name = intent.getStringExtra(ServiceForAccount.KEY_USERNAME);
		String pwd = intent.getStringExtra(ServiceForAccount.KEY_PASSWORD);
		AccountProvider accpro = new AccountProvider(mUrl);
		
	}

	private String getAccountXMLUrl(){
	
		ServiceForAccount c = new ServiceForAccount(this);
		c.getServerIP();
		http
	}

}
