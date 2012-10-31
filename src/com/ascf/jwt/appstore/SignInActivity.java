package com.ascf.jwt.appstore;

import java.net.Socket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.ascf.jwt.appstore.dirparser.ServiceForAccount;


public class SignInActivity extends Activity implements View.OnClickListener{

	public static final String TAG = "SignInActivity";
	public static final int OK_BUTTON = R.id.login;
	public static final int CANCEL_BUTTON = R.id.cancel;
	private Button mConfirmBtn = null;
	private Button mCancleBtn = null;
	private EditText mAccountText = null;
	private EditText mPwdText = null;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signin_setup_basics);
		mAccountText = (EditText) findViewById(R.id.account_name);
		mPwdText = (EditText) findViewById(R.id.account_password);
		mConfirmBtn = (Button) findViewById(OK_BUTTON);
		mConfirmBtn.setOnClickListener(this);
		mCancleBtn = (Button) findViewById(CANCEL_BUTTON);
		mCancleBtn.setOnClickListener(this);
	}

	/*
	mPassword.setInputType(
            InputType.TYPE_CLASS_TEXT | (((CheckBox) view).isChecked() ?
            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD :
            InputType.TYPE_TEXT_VARIATION_PASSWORD));*/

	private void saveValue(String user, String pwd) {
		ServiceForAccount accout = new ServiceForAccount(this);
		accout.saveKeyValue(ServiceForAccount.KEY_USERNAME, user);
		accout.saveKeyValue(ServiceForAccount.KEY_PASSWORD, pwd);
	}

	@Override
	public void onResume() {
		restoreValue();
		super.onResume();
	}

	private void restoreValue() {
		ServiceForAccount accout = new ServiceForAccount(this);
		String user = accout.getValueByKey(ServiceForAccount.KEY_USERNAME);
		String pwd = accout.getValueByKey(ServiceForAccount.KEY_PASSWORD);
		if (user != null && !"".equals(user)) {
			this.mAccountText.setText(user);
		}
		if (pwd != null && !"".equals(pwd)) {
			this.mPwdText.setText(pwd);
		}
	}

	//@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case OK_BUTTON: 
		{
			String accname = mAccountText.getText().toString();
			String pwd = mPwdText.getText().toString();
			Intent i = new Intent("com.dyne.intent.action_checkuser");
			i.putExtra(ServiceForAccount.KEY_USERNAME, accname);
			i.putExtra(ServiceForAccount.KEY_PASSWORD, pwd);
			saveValue(accname, pwd);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_NO_USER_ACTION);
			this.startActivityForResult(i, 0);
		}
			break;
		case CANCEL_BUTTON:
			finish();
			break;
		}
	}
//
//	private void getResourceList() {
//		Log.d(TAG, "start request resource list.");
//		Intent intent = new Intent(this, GeoloAppService.class);
//		Bundle extras = new Bundle();
//		extras.putInt(GeoloAppService.OPCODE, GeoloAppService.OP_GETLIST_REQ);
//		extras.putInt("state", GEItemView.STATE_MAIN);
//		intent.putExtras(extras);
//		startService(intent);
//	}

	@Override
	public void onStop() {
		hideInputMethod();
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	protected void hideInputMethod() {
		View view = this.getCurrentFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	protected void showInputMethod() {
		View view = this.getCurrentFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.showSoftInput(view, 0);
		}

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data){
		//TODO ....
	}
}
